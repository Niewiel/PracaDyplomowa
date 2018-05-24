package pl.niewiel.pracadyplomowa.view.build;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.List;
import java.util.Objects;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.database.model.Build;

public class BuildView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.list);
        if (getIntent().hasExtra("id")) {
            final List<Build> builds = SugarRecord.find(Build.class, "bs_id=?", String.valueOf(getIntent().getExtras().getLong("id")));
            if (!builds.isEmpty()) {
                final ListView listView = findViewById(R.id.list);
                BuildAdapter buildAdapter = new BuildAdapter(this, R.layout.build_row, builds);
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
