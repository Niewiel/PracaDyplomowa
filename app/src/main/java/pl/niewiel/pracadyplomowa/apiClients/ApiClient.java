package pl.niewiel.pracadyplomowa.apiClients;


import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.database.model.Token;

public class ApiClient {
    private static String API_URL = "http://devdyplom.nuc-mleczko-pawel.pl/api/v1/";
    public ApiClient() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        new TryToken().execute();

    }


    private static HttpResponse<String> test() {

        HttpResponse<String> response = null;

        try {
            response = Unirest.get("http://devdyplom.nuc-mleczko-pawel.pl/api/test")
                    .asString();
            Unirest.shutdown();

            Log.e("Status", String.valueOf(response.getCode()));

        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public HttpResponse<String> get(String path) {

        HttpResponse<String> response = null;

        try {
            response = Unirest.get(API_URL + path)
                    .asString();
            Unirest.shutdown();
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public HttpResponse<String> post(String path, Map<String, Object> fields) {
        HttpResponse<String> response = null;

        try {
            response = Unirest.post(API_URL + path)
                    .fields(fields)
                    .asString();
            Unirest.shutdown();
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public HttpResponse<String> delete(String path) {
        HttpResponse<String> response = null;

        try {
            response = Unirest.delete(API_URL + path)
                    .asString();
            Unirest.shutdown();
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public HttpResponse<String> put(String path, Map<String, Object> fields) {
        HttpResponse<String> response = null;

        try {
            response = Unirest.put(API_URL + path)
                    .fields(fields)
                    .asString();
            Unirest.shutdown();
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    static class TryToken extends AsyncTask<Token, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Token... tokens) {
            String status = "";
            if (Utils.IS_ONLINE) {


                if (!SugarRecord.listAll(Token.class).isEmpty()) {
                    String token = SugarRecord.last(Token.class).getAccess_token();
                    Log.e("token", token);
                    Unirest.setDefaultHeader("Authorization", "Bearer " + token);
                } else
                    new TokenClient().execute();
                try {
                    HttpResponse<String> response = test();
                    JSONObject object = new JSONObject(response.getBody());
                    status = object.getString("status");
                    if (!status.equals("OK"))
                        new TokenClient().execute();
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
//                 if(tokenClient.getStatus()!=Status.FINISHED) {
//                     try {
//                         wait(1000);
//                     } catch (InterruptedException e) {
//                         e.printStackTrace();
//                     }
//                 }
            }
            return null;
        }

    }

}

