package pl.niewiel.pracadyplomowa.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.adapters.ComponentTypeAdapter;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.database.service.ComponentTypeService;
import pl.niewiel.pracadyplomowa.fragments.add_edit.AddOrEditComponentType;

public class ComponentTypeActivity extends AppCompatActivity {
    ListView listView;
    LinearLayout progressBar;
    List<ComponentType> list;
    ComponentTypeAdapter adapter;
    Button addButton;
    Button deleteButton;
    Button editButton;
    ComponentTypeService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new LinkedList<>();

        service = new ComponentTypeService(getApplicationContext());
        setContentView(R.layout.detail_list);
        progressBar = findViewById(R.id.progressbar_view);
        listView = findViewById(R.id.list);
        addButton = findViewById(R.id.add_button);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);


        //wyświetlanie elementu
        adapter = new ComponentTypeAdapter(getApplicationContext(), R.layout.list_row, list);
        if (getIntent().hasExtra("mId")) {
            list.add(SugarRecord.findById(ComponentType.class, getIntent().getExtras().getLong("mId")));
            listView.setAdapter(adapter);
            new Task().execute();
        } else {
            Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
            Log.e("Component type", "no results");
            finish();
        }

        addButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             startActivity(new Intent(getApplicationContext(), AddOrEditComponentType.class));
                                         }
                                     }
        );

        Log.e("element", list.get(0).toString());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do You really want delete this item?")
                .setTitle("Delete?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                service.delete(list.get(0));
                SugarRecord.executeQuery("DELETE FROM component_type WHERE mid=?", String.valueOf(Objects.requireNonNull(getIntent().getExtras()).getLong("mId")));
                list.clear();
                finish();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {


            service.getAll();

            return null;
        }
    }
}
