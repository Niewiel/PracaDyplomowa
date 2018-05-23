package pl.niewiel.pracadyplomowa.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.Utility;
import pl.niewiel.pracadyplomowa.httpclient.ApiClient;
import pl.niewiel.pracadyplomowa.httpclient.TokenClient;

public class Splash extends AppCompatActivity {


    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SugarContext.init(getApplicationContext());
//        Log.e("Internet", "connected " + Utility.isOnline(getApplicationContext()));

        if (Utility.isOnline(getApplicationContext())) {
            Log.e("Internet", "connected " + Utility.isOnline(getApplicationContext()));
            Toast.makeText(getApplicationContext(), "connected to system", Toast.LENGTH_LONG);
//            dropBase();
            getToken();
        } else {
            Log.e("Internet", "connected " + Utility.isOnline(getApplicationContext()));
            Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG);
        }

//        Toast.makeText(getApplicationContext(), "Zaczynam splash", Toast.LENGTH_LONG).show();
        goToMain();
    }

    private void dropBase() {
        SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
        schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
        SugarContext.init(getApplicationContext());
        schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());
        Toast.makeText(getApplicationContext(), "database dropped", Toast.LENGTH_LONG).show();

    }

    private void getToken() {
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                new TokenClient().execute();
                handler.postDelayed(this, 6*6*1000);
            }
        };
        handler.postDelayed(r, 1);

    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
