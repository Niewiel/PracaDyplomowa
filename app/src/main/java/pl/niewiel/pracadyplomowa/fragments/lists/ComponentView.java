package pl.niewiel.pracadyplomowa.fragments.lists;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.adapters.ComponentAdapter;
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.database.service.ComponentService;
import pl.niewiel.pracadyplomowa.database.service.Service;

public class ComponentView extends AppCompatActivity {
    private static final String DEBUG_TAG = "ComponentView";
    ListView listView;
    LinearLayout progressBar;
    List<Component> list;
    ComponentAdapter adapter;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_list);
        progressBar = findViewById(R.id.progressbar_view);
        listView = findViewById(R.id.list);
        list = new LinkedList<>();
        adapter = new ComponentAdapter(this, R.layout.component_row, list);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra("mId")) {
            listView.setAdapter(adapter);
            new Task().execute();

            //fragment
            bundle = new Bundle();
            bundle.putLong("mId", getIntent().getExtras().getLong("mId"));
            Fragment fragment = new ComponentTypeListView();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.component_type_placeholder, fragment).commit();

        } else {
            Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
            Log.e("Component type", "no results");
            finish();
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private class Task extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            list.add(SugarRecord.findById(Component.class, getIntent().getExtras().getLong("mId")));
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Service service = new ComponentService(getApplicationContext());
            service.getById((int) SugarRecord.findById(Component.class, getIntent().getExtras().getLong("mId")).getBsId());
            return null;
        }
    }
}