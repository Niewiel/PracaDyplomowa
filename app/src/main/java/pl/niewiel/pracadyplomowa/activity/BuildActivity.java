package pl.niewiel.pracadyplomowa.activity;

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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.activity.add_edit.AddOrEditBuild;
import pl.niewiel.pracadyplomowa.adapters.BuildAdapter;
import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.database.service.BuildService;
import pl.niewiel.pracadyplomowa.database.service.Service;
import pl.niewiel.pracadyplomowa.fragments.BuildingListFragment;

public class BuildActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "BuildActivity";
    ListView listView;
    LinearLayout progressBar;
    List<Build> list;
    BuildAdapter adapter;
    Button addButton;
    Button deleteButton;
    Button editButton;
    Service<Build> service;
    Bundle bundle;

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
        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressbar_view);
        adapter = new BuildAdapter(this, R.layout.build_row, list);
        service = new BuildService(getApplicationContext());
        addButton = findViewById(R.id.add_button);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);


        if (getIntent().hasExtra("build")) {
            listView.setAdapter(adapter);


            bundle = new Bundle();
            bundle.putLong("buildings", getIntent().getExtras().getLong("build"));
            FrameLayout frameLayout = findViewById(R.id.subelement_placeholder);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            frameLayout.setLayoutParams(layoutParams);
            Fragment fragment = new BuildingListFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.subelement_placeholder, fragment).commit();

        } else {
            Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
            Log.e(DEBUG_TAG, "no results");
            finish();
        }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

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
        startActivity(new Intent(getApplicationContext(), AddOrEditBuild.class));
    }

    private void edit() {
        Intent intent = new Intent(getApplicationContext(), AddOrEditBuild.class);
        intent.putExtra("toUpdate", SugarRecord.findById(Build.class, getIntent().getExtras().getLong("build")).getmId());
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
                SugarRecord.executeQuery("DELETE FROM building WHERE mid=?", String.valueOf(Objects.requireNonNull(getIntent().getExtras()).getLong("building")));
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
            list.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            list.add(SugarRecord.findById(Build.class, getIntent().getExtras().getLong("build")));
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            service.getById((int) SugarRecord.findById(Build.class, getIntent().getExtras().getLong("build")).getBsId());
            return null;
        }
    }
}
