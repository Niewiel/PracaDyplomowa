package pl.niewiel.pracadyplomowa.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.database.service.Synchronize;

public class OnlineChecker extends Service {
    public OnlineChecker() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        final boolean[] previous = {false};
        super.onCreate();
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            @SuppressLint("ShowToast")
            public void run() {
                new CheckOnline().execute();
                if (Utils.IS_ONLINE) {

                    //onlineListener
                    if (previous[0] != Utils.IS_ONLINE)
                        if (Utils.IS_SYNCHRONIZED)
                            new Synchronize(getApplicationContext()).execute();
                    previous[0] = true;
                    Toast.makeText(getApplicationContext(), "You are online", Toast.LENGTH_SHORT);
                } else {
                    Utils.IS_ONLINE = false;
                    previous[0] = false;

                }

                handler.postDelayed(this, 10 * 1000 - 1);
            }
        };
        handler.postDelayed(r, 1);
    }

    private class CheckOnline extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Utils.IS_ONLINE = aBoolean;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                int timeoutMs = 1500;
                Socket sock = new Socket();
                SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

                sock.connect(sockaddr, timeoutMs);
                sock.close();

                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }


}
