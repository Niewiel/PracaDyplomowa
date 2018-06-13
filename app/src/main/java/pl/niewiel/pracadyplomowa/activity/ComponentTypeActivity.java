package pl.niewiel.pracadyplomowa.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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
import pl.niewiel.pracadyplomowa.database.service.Service;
import pl.niewiel.pracadyplomowa.fragments.add_edit.AddOrEditComponentType;

public class ComponentTypeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String DEBUG_TAG = "Component Type Activity";

    List<ComponentType> list;
    ComponentTypeAdapter adapter;
    Service<ComponentType> service;
    ComponentType componentType;

    ListView listView;
    LinearLayout progressBar;
    Button addButton;
    Button deleteButton;
    Button editButton;


    @Override
    protected void onPostResume() {
        super.onPostResume();
        new Task().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_list);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        list = new LinkedList<>();

        service = new ComponentTypeService(getApplicationContext());

        progressBar = findViewById(R.id.progressbar_view);
        listView = findViewById(R.id.list);
        addButton = findViewById(R.id.add_button);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);


        //wyświetlanie elementu
        adapter = new ComponentTypeAdapter(getApplicationContext(), R.layout.list_row, list);
        if (getIntent().hasExtra("type")) {
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
            Log.e("Component type", "no results");
            finish();
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void add() {
        startActivity(new Intent(getApplicationContext(), AddOrEditComponentType.class));
    }

    private void edit() {
        Intent intent = new Intent(getApplicationContext(), AddOrEditComponentType.class);
        intent.putExtra("toUpdate", SugarRecord.findById(ComponentType.class, getIntent().getExtras().getLong("type")).getmId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do You really want delete this item?")
                .setTitle("Delete?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                service.delete(list.get(0));
                SugarRecord.executeQuery("DELETE FROM component_type WHERE mid=?", String.valueOf(Objects.requireNonNull(getIntent().getExtras()).getLong("type")));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onRefresh() {
        new Task().execute();
    }

    private class Task extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            list.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            list.add(SugarRecord.findById(ComponentType.class, getIntent().getExtras().getLong("type")));
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            componentType = SugarRecord.findById(ComponentType.class, getIntent().getExtras().getLong("type"));
            service.getById((int) componentType.getBsId());

            return null;
        }
    }
}
