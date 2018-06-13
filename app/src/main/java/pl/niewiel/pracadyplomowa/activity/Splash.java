package pl.niewiel.pracadyplomowa.activity;

import android.annotation.SuppressLint;
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
        Utils.startOnlineCheckerService(getApplicationContext());
//        Utils.dropBase(getApplicationContext());
//        Utils.fillDatabase();
        if (Utils.IS_ONLINE) {
            Log.e("Internet", "connected " + Utils.IS_ONLINE);
            Toast.makeText(getApplicationContext(), "connected to system", Toast.LENGTH_LONG);
            Utils.startTokenService(this);
        } else {
            Log.e("Internet", "connected " + Utils.IS_ONLINE);
            Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG);
        }
        Toast.makeText(getApplicationContext(), "Zaczynam splash", Toast.LENGTH_LONG).show();
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Stop", getLocalClassName());
    }


}
