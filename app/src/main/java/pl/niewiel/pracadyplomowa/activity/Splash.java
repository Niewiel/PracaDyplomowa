package pl.niewiel.pracadyplomowa.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.orm.SugarContext;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.httpclient.ApiClient;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SugarContext.init(getApplicationContext());
        Toast.makeText(getApplicationContext(), "Zaczynam splash", Toast.LENGTH_LONG).show();
        new ApiClient().getToken();


    }

//    private void getToken() {
//        final Handler handler = new Handler();
//        final ApiClient apiClient = new ApiClient();
//        final Runnable r = new Runnable() {
//            public void run() {
//                apiClient.getToken();
//                handler.postDelayed(this, 1000);
//            }
//        };
//        handler.postDelayed(r, 1000);
//    }
}
