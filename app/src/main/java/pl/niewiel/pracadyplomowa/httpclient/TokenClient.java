package pl.niewiel.pracadyplomowa.httpclient;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.database.model.Token;

public class TokenClient extends AsyncTask<String, Void, HttpResponse<String>> {


    public TokenClient() {
    }

    @Override
    protected HttpResponse<String> doInBackground(String... strings) {

        Unirest.clearDefaultHeaders();
        HttpResponse<String> response = null;
        if (Utils.IS_ONLINE) {
            try {
                response = Unirest.post("http://devdyplom.nuc-mleczko-pawel.pl/oauth/getToken")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Authorization", "Basic " + encrypt())
                        .header("Cache-Control", "no-cache")
                        .body("grant_type=password&username=user123&password=user123")
                        .asString();
                Unirest.shutdown();
            } catch (UnirestException | IOException e) {
                e.printStackTrace();

            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(HttpResponse<String> response) {
        if (response.getCode() == 200) {
            JSONObject reader;
            try {
                reader = new JSONObject(response.getBody());
                Token token = new Token(
                        reader.getString("access_token"),
                        reader.getString("expires_in"),
                        reader.getString("token_type"));
                token.setRefresh_token(reader.getString("refresh_token"));
                SugarRecord.save(token);
                Log.e("Token", token.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            Log.e("Response code", String.valueOf(response.getCode()));

    }

    private String encrypt() {
        String text = "01aa12304a745d7fb2cfb051b31de22f" + ":" + "$2y$11$zuGIO8nFkyIqMP1PBcEZUeGPEeZYtzRNi1RN3JRVyjaWa7FFYTdBK";
        byte[] data = new byte[0];
        try {
            data = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }


}

