package pl.niewiel.pracadyplomowa.fragments.add_edit;

import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.orm.SugarRecord;

import java.util.List;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.database.service.ComponentTypeService;
import pl.niewiel.pracadyplomowa.database.service.Service;

public class AddOrEditComponentType extends AppCompatActivity {
    ComponentType componentType;
    Button buttonAdd;
    TextInputEditText name;
    Service<ComponentType> service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new ComponentTypeService(getApplicationContext());
        setContentView(R.layout.activity_add_or_edit_component_type);
        name = findViewById(R.id.name);
        buttonAdd = findViewById(R.id.add_button);

        if (getIntent().hasExtra("toUpdate")) {
            componentType = SugarRecord.findById(ComponentType.class, getIntent().getExtras().getLong("toUpdate"));
            if (componentType != null) {
                name.setText(componentType.getName());
                buttonAdd.setText(R.string.string_update);
                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        componentType.setName(name.getText().toString());
                        componentType.setDateEdit();
                        SugarRecord.save(componentType);
                        service.update(componentType);
                        if (!Utils.IS_ONLINE)
                            Utils.IS_SYNCHRONIZED = false;
                        finish();
                    }
                });
            }
        }


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNameValid()) {
                    componentType = new ComponentType(name.getText().toString());
                    componentType.setmId(SugarRecord.save(componentType));
                    SugarRecord.save(componentType);
                    service.add(componentType);
                    if (!Utils.IS_ONLINE)
                        Utils.IS_SYNCHRONIZED = false;
                    finish();
                }
            }
        });

    }


    private boolean isNameValid() {
        List<ComponentType> list;
        if (!name.getText().toString().equals("")) {
            try {
                list = SugarRecord.find(ComponentType.class, "name=?", name.getText().toString());
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
    protected void onPostResume() {
        super.onPostResume();
    }


}
