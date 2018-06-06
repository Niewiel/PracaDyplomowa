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
import pl.niewiel.pracadyplomowa.adapters.BuildingListAdapter;
import pl.niewiel.pracadyplomowa.database.model.Building;
import pl.niewiel.pracadyplomowa.database.service.BuildingService;
import pl.niewiel.pracadyplomowa.database.service.Service;

public class BuildingView extends AppCompatActivity {
    ListView listView;
    LinearLayout progressBar;
    List<Building> list;
    BuildingListAdapter adapter;
    Service<Building> service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_list);
        final List<Building> buildings = new LinkedList<>();
        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressbar_view);
        list = new LinkedList<>();
        adapter = new BuildingListAdapter(getApplicationContext(), R.layout.list_row, list);
        service = new BuildingService(getApplicationContext());
        if (getIntent().hasExtra("mId")) {
            listView.setAdapter(adapter);
            new Task().execute();
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
            list.add(SugarRecord.findById(Building.class, getIntent().getExtras().getLong("mId")));
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            service.getById((int) SugarRecord.findById(Building.class, getIntent().getExtras().getLong("mId")).getBsId());
            return null;
        }
    }
}
