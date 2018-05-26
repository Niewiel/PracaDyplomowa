package pl.niewiel.pracadyplomowa.view.componentTypeList;

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
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.database.service.ComponentTypeService;

public class ComponentTypeListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.list);
        ComponentTypeService componentTypeService = new ComponentTypeService(getApplicationContext());
        componentTypeService.getAll();
        List<ComponentType> componentTypes = new LinkedList<>();
        componentTypes.addAll(SugarRecord.listAll(ComponentType.class));
        if (!componentTypes.isEmpty()) {
            Log.e("ComponentTypeListView", String.valueOf(componentTypes));
            ListView listView = findViewById(R.id.list);
            ComponentTypeListAdapter buildAdapter = new ComponentTypeListAdapter(this, R.layout.component_type_list_row, componentTypes);
            listView.setAdapter(buildAdapter);
        } else {
            Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
            Log.e("Builds", "no results");
            finish();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
