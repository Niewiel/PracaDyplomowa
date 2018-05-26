package pl.niewiel.pracadyplomowa.view.componentType;

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

public class ComponentTypeView extends AppCompatActivity {
    final List<ComponentType> componentTypes = new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.list);
        if (getIntent().hasExtra("id")) {
            ComponentTypeService componentTypeService = new ComponentTypeService(getApplicationContext());

            componentTypes.add(componentTypeService.getById((int) getIntent().getExtras().getLong("id")));
            if (!componentTypes.isEmpty()) {
                ListView listView = findViewById(R.id.list);
                ComponentTypeAdapter buildAdapter = new ComponentTypeAdapter(this, R.layout.component_type_row, componentTypes);
                listView.setAdapter(buildAdapter);
            } else {
                Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
                Log.e("Component type", "no results");
                finish();
            }
        } else if (getIntent().hasExtra("mId")) {


            componentTypes.add(SugarRecord.findById(ComponentType.class, getIntent().getExtras().getLong("mId")));
            if (!componentTypes.isEmpty()) {
                Log.e("check", String.valueOf(componentTypes));

                ListView listView = findViewById(R.id.list);
                ComponentTypeAdapter buildAdapter = new ComponentTypeAdapter(this, R.layout.component_type_row, componentTypes);
                listView.setAdapter(buildAdapter);
            } else {
                Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
                Log.e("Component type", "no results");
                finish();
            }
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
}
