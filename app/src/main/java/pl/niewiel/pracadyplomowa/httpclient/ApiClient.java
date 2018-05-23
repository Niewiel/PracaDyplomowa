package pl.niewiel.pracadyplomowa.httpclient;


import android.os.StrictMode;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orm.SugarRecord;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import pl.niewiel.pracadyplomowa.database.model.Token;

public class ApiClient {
    private static String API_URL = "https://devdyplom.nuc-mleczko-pawel.pl/api/";

    public ApiClient() {
        List<Token> tokens = SugarRecord.listAll(Token.class);
        String token = tokens.get(tokens.size()).getAccess_token();
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

}

