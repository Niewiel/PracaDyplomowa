package pl.niewiel.pracadyplomowa.httpclient;


import android.os.StrictMode;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orm.SugarRecord;

import java.io.IOException;
import java.util.Map;

import pl.niewiel.pracadyplomowa.database.model.Token;

public class ApiClient {
    private static String API_URL = "http://devdyplom.nuc-mleczko-pawel.pl/api/v1/";

    public ApiClient() {
        String token = SugarRecord.last(Token.class).getAccess_token();
        Log.e("token", token);
        Unirest.setDefaultHeader("Authorization", "Bearer " + token);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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

    public HttpResponse<String> post(Map<String, Object> fields, String path) {
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

    public HttpResponse<String> delete(Map<String, Object> fields, String path) {
        HttpResponse<String> response = null;

        try {
            response = Unirest.delete(API_URL + path)
                    .fields(fields)
                    .asString();
            Unirest.shutdown();
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public HttpResponse<String> put(Map<String, Object> fields, String path) {
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

}

