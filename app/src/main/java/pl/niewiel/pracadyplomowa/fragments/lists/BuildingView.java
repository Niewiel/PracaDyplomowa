package pl.niewiel.pracadyplomowa.fragments.lists;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import pl.niewiel.pracadyplomowa.adapters.BuildingAdapter;
import pl.niewiel.pracadyplomowa.database.model.Building;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.database.service.BuildingService;
import pl.niewiel.pracadyplomowa.database.service.Service;
import pl.niewiel.pracadyplomowa.fragments.add_edit.AddOrEditComponentType;

public class BuildingView extends AppCompatActivity {
    ListView listView;
    LinearLayout progressBar;
    List<Building> list;
    BuildingAdapter adapter;
    Service<Building> service;
    Bundle bundle;
    Button addButton;
    Button deleteButton;
    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_list);
        list = new LinkedList<>();
        listView = findViewById(R.id.list);


        progressBar = findViewById(R.id.progressbar_view);
        listView = findViewById(R.id.list);
        addButton = findViewById(R.id.add_button);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);


        adapter = new BuildingAdapter(getApplicationContext(), R.layout.list_row, list);
        service = new BuildingService(getApplicationContext());


        if (getIntent().hasExtra("building")) {
            listView.setAdapter(adapter);
            new Task().execute();

            bundle = new Bundle();
            bundle.putLong("buildings", getIntent().getExtras().getLong("building"));
            Fragment fragment = new ComponentTypeListView();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.subelement_placeholder, fragment).commit();

        } else {
            Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
            Log.e("Builds", "no results");
            finish();
        }

        Fragment fragment = new ComponentListView();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.subelement_placeholder, fragment).commit();


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
            list.add(SugarRecord.findById(Building.class, getIntent().getExtras().getLong("building")));
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            service.getById((int) SugarRecord.findById(Building.class, getIntent().getExtras().getLong("building")).getBsId());
            return null;
        }
    }
}
