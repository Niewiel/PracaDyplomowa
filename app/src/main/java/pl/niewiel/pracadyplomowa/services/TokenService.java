package pl.niewiel.pracadyplomowa.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.httpclient.TokenClient;

public class TokenService extends Service {
    public TokenService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        if (Utils.IS_ONLINE) {
            super.onCreate();
            final Handler handler = new Handler();
            final Runnable r = new Runnable() {
                public void run() {
                    new TokenClient().execute();
                    handler.postDelayed(this, 60 * 60 * 1000 - 1);
                }
            };
            handler.postDelayed(r, 1);
        }
    }
}
