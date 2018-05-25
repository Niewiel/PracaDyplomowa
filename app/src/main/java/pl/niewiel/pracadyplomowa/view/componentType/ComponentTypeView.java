package pl.niewiel.pracadyplomowa.view.componentType;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.database.service.BuildService;

public class ComponentTypeView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.list);
        if (getIntent().hasExtra("mId")) {
            BuildService buildService = new BuildService();
            Log.e("check", String.valueOf(buildService.getById((int) getIntent().getExtras().getLong("mId"))));
            final List<Build> builds = SugarRecord.find(Build.class, "bs_id=?", String.valueOf(getIntent().getExtras().getLong("mId")));
            if (!builds.isEmpty()) {
                final ListView listView = findViewById(R.id.list);
                ComponentTypeAdapter buildAdapter = new ComponentTypeAdapter(this, R.layout.build_list_row, builds);
                listView.setAdapter(buildAdapter);
            } else {
                Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
                Log.e("Builds", "no results");
                finish();
            }
        } else if (getIntent().hasExtra("id")) {

            final List<Build> builds = new ArrayList<>();
            builds.add(SugarRecord.findById(Build.class, getIntent().getExtras().getLong("id")));
            if (!builds.isEmpty()) {
                Log.e("check", String.valueOf(builds));

                final ListView listView = findViewById(R.id.list);
                ComponentTypeAdapter buildAdapter = new ComponentTypeAdapter(this, R.layout.build_list_row, builds);
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
