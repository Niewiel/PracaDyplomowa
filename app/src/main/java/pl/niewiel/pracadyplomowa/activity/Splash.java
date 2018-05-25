package pl.niewiel.pracadyplomowa.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.orm.SugarContext;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.Utils;

public class Splash extends AppCompatActivity {


    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SugarContext.init(getApplicationContext());
//        Log.e("Internet", "connected " + Utility.isOnline(getApplicationContext()));

        if (Utils.isOnline(getApplicationContext())) {
            Log.e("Internet", "connected " + Utils.isOnline(getApplicationContext()));
            Toast.makeText(getApplicationContext(), "connected to system", Toast.LENGTH_LONG);
            Utils.dropBase(getApplicationContext());
            Utils.fillDatabase();
            Utils.getToken();
        } else {
            Log.e("Internet", "connected " + Utils.isOnline(getApplicationContext()));
            Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG);
        }

//        Toast.makeText(getApplicationContext(), "Zaczynam splash", Toast.LENGTH_LONG).show();
        goToMain();
    }



    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
