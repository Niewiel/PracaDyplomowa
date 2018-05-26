package pl.niewiel.pracadyplomowa.view.buildList;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.database.service.BuildService;

public class BuildListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.list);
        BuildService buildService = new BuildService(getApplicationContext());
        List<Build> builds = new LinkedList<>();
        builds.addAll(buildService.getAll());
        if (!builds.isEmpty()) {
            ListView listView = findViewById(R.id.list);
            BuildListAdapter buildAdapter = new BuildListAdapter(this, R.layout.build_list_row, builds);
            listView.setAdapter(buildAdapter);
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
}
