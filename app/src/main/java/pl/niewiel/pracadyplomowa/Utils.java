package pl.niewiel.pracadyplomowa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.orm.SugarRecord;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.httpclient.TokenClient;

public class Utils {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static Timestamp parseDate(String date) {
        Timestamp timestamp = null;
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(date);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
        } catch (Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }
        return timestamp;
    }

    public static void fillDatabase() {
        Build build = new Build();
        build.setName("aaa");
        build.setMid(SugarRecord.save(build));
        SugarRecord.save(build);
        build = new Build();
        build.setName("bbb");
        build.setMid(SugarRecord.save(build));
        SugarRecord.save(build);
        Log.e("new Build", String.valueOf(SugarRecord.listAll(Build.class)));
    }

    public static void dropBase(Context context) {
        SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(context);
        schemaGenerator.deleteTables(new SugarDb(context).getDB());
        SugarContext.init(context);
        schemaGenerator.createDatabase(new SugarDb(context).getDB());
        Toast.makeText(context, "database dropped", Toast.LENGTH_LONG).show();

    }

    public static void getToken() {
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                new TokenClient().execute();
                handler.postDelayed(this, 6 * 6 * 10000);
            }
        };
        handler.postDelayed(r, 1);

    }


}
