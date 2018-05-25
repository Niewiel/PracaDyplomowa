package pl.niewiel.pracadyplomowa.database.service;

import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.httpclient.ApiClient;

public class BuildService {
    private static ApiClient apiClient = new ApiClient();

    public BuildService() {
    }



    public ArrayList<Build> getAll() {
        ArrayList<Build> builds = new ArrayList<>();
        try {
            HttpResponse<String> response = apiClient.get("build");
            JSONObject object = new JSONObject(response.getBody());

            JSONArray array = object.getJSONObject("result").getJSONObject("content").getJSONArray("Builds");
            String status = object.getString("status");
            Log.e("body", status);
            if (status.equals("OK")) {
                for (int i = 0; i < array.length(); i++) {
                    Build build = new Build();
                    JSONObject item = array.getJSONObject(i);
                    build.setBsId(i + 1);
                    build.setDateAdd(Utils.parseDate(item.getString("dateAdd")));
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
        Build build;
        try {
            build = SugarRecord.find(Build.class, "bs_id=?", String.valueOf(id)).get(0);
            Log.e("wczytany", build.toString());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            build = new Build();
            build.setBsId(id);
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
                    build.setDateAdd(Utils.parseDate(reader.getJSONObject("DateAdd").getString("date")));
                    build.setDateEdit(Utils.parseDate(reader.getString("DateEdit")));
                    build.setName(reader.getString("Name"));
                    Log.e("name", reader.getString("Name"));
                    build.setLatitude(reader.getString("Latitude"));
                    build.setLongitude(reader.getString("Longitude"));
                    build.setSync(true);
                    build.setmId(SugarRecord.save(build));
                    SugarRecord.save(build);
                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
            Log.e("record", String.valueOf(SugarRecord.findWithQuery(Build.class, "SELECT * FROM build WHERE bs_id=?", String.valueOf(1))));
        }
        return build;
    }
}
