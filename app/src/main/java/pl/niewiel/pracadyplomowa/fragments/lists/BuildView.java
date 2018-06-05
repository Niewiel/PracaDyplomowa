package pl.niewiel.pracadyplomowa.fragments.lists;

import android.os.AsyncTask;
import android.os.Bundle;
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

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.adapters.BuildAdapter;
import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.database.service.BuildService;
import pl.niewiel.pracadyplomowa.database.service.Service;

public class BuildView extends AppCompatActivity {
    private static final String DEBUG_TAG = "BuildView";
    ListView listView;
    LinearLayout progressBar;
    List<Build> list;
    BuildAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.detail_list);
        list = new LinkedList<>();
        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressbar_view);
        adapter = new BuildAdapter(this, R.layout.build_row, list);
        new Task().execute();
        if (getIntent().hasExtra("mId")) {
            BuildService buildService = new BuildService(getApplicationContext());
            list.add(buildService.getById((int) getIntent().getExtras().getLong("mId")));
            if (!list.isEmpty()) {
                listView.setAdapter(adapter);
            }
        } else if (getIntent().hasExtra("id")) {
            list.add(SugarRecord.findById(Build.class, getIntent().getExtras().getLong("id")));
            if (!list.isEmpty()) {
                Log.e("check", String.valueOf(list));
                listView.setAdapter(adapter);
            }
        } else {
            Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
            Log.e("Builds", "no results");
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
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Service service = new BuildService(getApplicationContext());
            service.getAll();
            return null;
        }
    }
}
