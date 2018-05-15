package pl.niewiel.pracadyplomowa.httpclient;

import android.os.Handler;
import android.os.StrictMode;
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

import pl.niewiel.pracadyplomowa.database.model.Token;

public class ApiClient  {
    private Token token;
    private Handler handler;

    public ApiClient() {
    }


    public void getToken() {

        try {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse<String> response = Unirest.post("http://devdyplom.nuc-mleczko-pawel.pl/oauth/getToken")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic MDFhYTEyMzA0YTc0NWQ3ZmIyY2ZiMDUxYjMxZGUyMmY6JDJ5JDExJDAuSWwwUE1NU1VtSENEZjVTRU1WOC5KYWpNZ3lFaHlyNXdtc2pURkdjemhOazJva2FTVkl5")
                    .header("Cache-Control", "no-cache")
                    .header("Postman-Token", "a220f296-72e2-44fe-b00e-8c00f5218b1c")
                    .body("grant_type=password&username=user123&password=user123")
                    .asString();
            Unirest.shutdown();
            if (response.getCode() == 200) {
                JSONObject reader = new JSONObject(response.getBody());

                token = new Token(
                        reader.getString("access_token"),
                        reader.getString("expires_in"),
                        reader.getString("token_type"));
                token.setRefresh_token(reader.getString("refresh_token"));
                SugarRecord.save(token);
                Log.i("TOKEN", token.toString());
            } else
                Log.e("Response code", String.valueOf(response.getCode()));

            }
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

