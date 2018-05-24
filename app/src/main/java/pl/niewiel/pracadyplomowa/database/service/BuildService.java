package pl.niewiel.pracadyplomowa.database.service;

import android.annotation.SuppressLint;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.httpclient.ApiClient;

public class BuildService {
    private static ApiClient apiClient = new ApiClient();

    public BuildService() {
    }

    public Timestamp parse(String date) {
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

    public ArrayList<Build> getAll() {
        Build build;
        ArrayList<Build> builds = new ArrayList<>();
        try {
            HttpResponse<String> response = apiClient.get("build");
            JSONObject object = new JSONObject(response.getBody());

            JSONArray array = object.getJSONObject("result").getJSONObject("content").getJSONArray("Builds");
            String status = object.getString("status");
            Log.e("body", status);
            if (status.equals("OK")) {
                for (int i = 0; i < array.length(); i++) {
                    build = new Build();
                    JSONObject item = array.getJSONObject(i);
                    build.setDateAdd(parse(item.getString("dateAdd")));
                    build.setName(item.getString("name"));
                    build.setSync("true".equals(item.getString("synchronized")));
                    builds.add(build);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return builds;
    }

    public Build getById(int id) {
        Build build = null;
        try {
            build = SugarRecord.find(Build.class, "bs_id=?", String.valueOf(id)).get(0);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            build = new Build();
        }
        if (!build.isSync()) {
            try {
                HttpResponse<String> response = apiClient.get("build/"+id);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                Log.e("status", status);
                if (status.equals("OK")) {
                    Log.e("Json","start");
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content").getJSONObject("Build");
                    build.setDateAdd(parse(reader.getJSONObject("DateAdd").getString("date")));
                    build.setDateEdit(parse(reader.getString("DateEdit")));
                    build.setName(reader.getString("Name"));
                    Log.e("name", reader.getString("Name"));
                    build.setLatitude(reader.getString("Latitude"));
                    build.setLongitude(reader.getString("Longitude"));
                    build.setSync(true);
                    SugarRecord.save(build);
                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return build;
    }
}
