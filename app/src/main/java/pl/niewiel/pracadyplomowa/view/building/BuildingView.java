package pl.niewiel.pracadyplomowa.view.building;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.database.service.BuildService;

public class BuildingView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.list);
        final List<Build> builds = new LinkedList<>();
        if (getIntent().hasExtra("mId")) {
            BuildService buildService = new BuildService(getApplicationContext());
            builds.add(buildService.getById((int) getIntent().getExtras().getLong("mId")));
            if (!builds.isEmpty()) {
                final ListView listView = findViewById(R.id.list);
                BuildingAdapter buildAdapter = new BuildingAdapter(this, R.layout.build_row, builds);
                listView.setAdapter(buildAdapter);
            } else {
                Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
                Log.e("Builds", "no results");
                finish();
            }
        } else if (getIntent().hasExtra("id")) {
            builds.add(SugarRecord.findById(Build.class, getIntent().getExtras().getLong("id")));
            if (!builds.isEmpty()) {
                Log.e("check", String.valueOf(builds));

                final ListView listView = findViewById(R.id.list);
                BuildingAdapter buildAdapter = new BuildingAdapter(this, R.layout.build_row, builds);
                listView.setAdapter(buildAdapter);
            } else {
                Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
                Log.e("Builds", "no results");
                finish();
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
}
