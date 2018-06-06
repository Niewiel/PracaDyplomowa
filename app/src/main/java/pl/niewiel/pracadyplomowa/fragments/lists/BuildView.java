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
    Service<Build> service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.detail_list);
        list = new LinkedList<>();
        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressbar_view);
        adapter = new BuildAdapter(this, R.layout.build_row, list);
        service = new BuildService(getApplicationContext());

        if (getIntent().hasExtra("mId")) {
                listView.setAdapter(adapter);
            new Task().execute();

        } else {
            Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
            Log.e(DEBUG_TAG, "no results");
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
            list.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            list.add(SugarRecord.findById(Build.class, getIntent().getExtras().getLong("mId")));
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            service.getById((int) SugarRecord.findById(Build.class, getIntent().getExtras().getLong("mId")).getBsId());
            return null;
        }
    }
}
