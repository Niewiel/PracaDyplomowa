package pl.niewiel.pracadyplomowa.fragments.lists;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.LinkedList;
import java.util.List;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.adapters.BuildingAdapter;
import pl.niewiel.pracadyplomowa.database.model.Building;
import pl.niewiel.pracadyplomowa.database.service.BuildingService;

public class BuildingView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_list);
        final List<Building> buildings = new LinkedList<>();
        if (getIntent().hasExtra("mId")) {
            BuildingService buildingService = new BuildingService(getApplicationContext());
            buildings.add(buildingService.getById((int) getIntent().getExtras().getLong("mId")));
            if (!buildings.isEmpty()) {
                final ListView listView = findViewById(R.id.list);
                BuildingAdapter buildAdapter = new BuildingAdapter(this, R.layout.building_row, buildings);
                listView.setAdapter(buildAdapter);
            } else {
                Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
                Log.e("Builds", "no results");
                finish();
            }
        } else if (getIntent().hasExtra("id")) {
            buildings.add(SugarRecord.findById(Building.class, getIntent().getExtras().getLong("id")));
            if (!buildings.isEmpty()) {
                Log.e("check", String.valueOf(buildings));

                final ListView listView = findViewById(R.id.list);
                BuildingAdapter buildAdapter = new BuildingAdapter(this, R.layout.building_row, buildings);
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
