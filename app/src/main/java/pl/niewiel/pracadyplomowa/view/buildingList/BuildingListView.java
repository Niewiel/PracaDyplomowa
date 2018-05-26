package pl.niewiel.pracadyplomowa.view.buildingList;

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
import pl.niewiel.pracadyplomowa.database.model.Building;
import pl.niewiel.pracadyplomowa.database.service.BuildingService;

public class BuildingListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.list);
        BuildingService buildingService = new BuildingService(getApplicationContext());
        List<Building> buildings = new LinkedList<>();
        buildings.addAll(buildingService.getAll());
        if (!buildings.isEmpty()) {
            ListView listView = findViewById(R.id.list);
            BuildingListAdapter buildAdapter = new BuildingListAdapter(this, R.layout.building_list_row, buildings);
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
