package pl.niewiel.pracadyplomowa.activity.add_edit;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.orm.SugarRecord;

import java.util.LinkedList;
import java.util.List;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.activity.CameraActivity;
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.database.model.TypesToComponent;
import pl.niewiel.pracadyplomowa.database.service.ComponentService;
import pl.niewiel.pracadyplomowa.database.service.Service;

public class AddOrEditComponent extends AppCompatActivity {
    private static final int PICKFILE_REQUEST_CODE = 3;
    Service<Component> service;
    private Component component;
    private TextInputEditText name;

    Spinner spinner;
    ListView componentList;
    ArrayAdapter<ComponentType> spinnerAdapter;
    ArrayAdapter<ComponentType> listAdapter;
    private List<ComponentType> types;
    private List<ComponentType> selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_component);
        final NumberPicker procentPicker = findViewById(R.id.procent);

        procentPicker.setMinValue(0);
        procentPicker.setMaxValue(100);
        procentPicker.computeScroll();

        service = new ComponentService(getApplicationContext());

        name = findViewById(R.id.name);
        spinner = findViewById(R.id.spinner);
        componentList = findViewById(R.id.component_list);
        types = SugarRecord.listAll(ComponentType.class);
        selected = new LinkedList<>();


        spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_spinner_item);
        spinnerAdapter.addAll(types);
        listAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_spinner_item);

        componentList.setAdapter(listAdapter);
        spinner.setAdapter(spinnerAdapter);
        selected.clear();
        if (getIntent().hasExtra("component")) {
            fillList();
            listAdapter.addAll(selected);
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!selected.contains(spinnerAdapter.getItem(position))) {
                    selected.add(spinnerAdapter.getItem(position));
                    listAdapter.add(spinnerAdapter.getItem(position));
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        componentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected.remove(listAdapter.getItem(position));
                listAdapter.remove(listAdapter.getItem(position));
                listAdapter.notifyDataSetChanged();
            }
        });



        Button add_photo = findViewById(R.id.add_photo_button);
        Button add = findViewById(R.id.add_button);
        Button chose = findViewById(R.id.chose_file_button);


        chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*.");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });
        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                if (getIntent().hasExtra("component"))
                    intent.putExtra("component", SugarRecord.findById(Component.class, getIntent().getExtras().getLong("component")).getmId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });


        if (getIntent().hasExtra("component")) {
            component = SugarRecord.findById(Component.class, getIntent().getExtras().getLong("component"));

            Log.d("CompToUpd", component.toString());
            if (component != null) {
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("UPDATE", String.valueOf(Utils.IS_ONLINE));
                        updateTypes(component);
                        component.setSync(false);
                        component.setName(name.getText().toString());
                        component.setDateEdit();
                        component.setStatus(procentPicker.getValue());
                        if (!Utils.IS_ONLINE) {

                            Utils.IS_SYNCHRONIZED = false;
                            SugarRecord.save(component);
                        } else {
                            Log.e("UPDATE", "");
                            SugarRecord.save(component);
                            service.update(component);
                        }
                        finish();

                    }
                });
                Log.e("Component update", String.valueOf(component));
                name.setText(component.getName());
                procentPicker.setValue(component.getStatus());
                add.setText(R.string.string_update);


            }
        } else {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("online", String.valueOf(Utils.IS_ONLINE));

                    if (isNameValid()) {
                        component = new Component();
                        component.setName(name.getText().toString());
                        component.setStatus(procentPicker.getValue());
                        component.setmId(SugarRecord.save(component));
                        saveTypes(component);
                        if (Utils.IS_ONLINE)
                            service.create(component);
                        else {
                            Utils.IS_SYNCHRONIZED = false;
                            SugarRecord.save(component);
                        }
                        finish();
                    }
                }
            });
        }
    }

    private void saveTypes(Component component) {
        for (ComponentType t :
                selected) {
            SugarRecord.save(new TypesToComponent(component, t));
        }

    }

    private void fillList() {
        component = SugarRecord.findById(Component.class, getIntent().getExtras().getLong("component"));
        List<TypesToComponent> typesToComponents = SugarRecord.find(TypesToComponent.class, "component_id=?", String.valueOf(component.getmId()));
        for (TypesToComponent ttc :
                typesToComponents) {
            selected.add(SugarRecord.findById(ComponentType.class, ttc.getTypeId()));


        }
        listAdapter.notifyDataSetChanged();
        System.out.println(selected.toString());
    }

    private void updateTypes(Component component) {
        List<TypesToComponent> toDelete = SugarRecord.find(TypesToComponent.class, "component_id=?", String.valueOf(component.getmId()));
        for (TypesToComponent ttc :
                toDelete) {

            SugarRecord.executeQuery("DELETE FROM types_to_component where mid=?", String.valueOf(ttc.getMid()));
            Log.d("DELETED", ttc.toString());

        }

        for (ComponentType t : selected) {
            SugarRecord.save(new TypesToComponent(component, t));
        }
    }

    private boolean isNameValid() {
        List<Component> list;
        if (!name.getText().toString().equals("")) {
            try {
                list = SugarRecord.find(Component.class, "name=?", name.getText().toString());
                if (list.isEmpty())
                    return true;
                else {
                    name.setError("this component type already exists");
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            name.setError("this field is required");
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("DATA", data.toString());
    }


}
