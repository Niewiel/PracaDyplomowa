package pl.niewiel.pracadyplomowa.database.service;

import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.httpclient.ApiClient;

public class ComponentService {
    ApiClient apiClient = new ApiClient();

    public ComponentService() {
    }

    public ArrayList<Component> getAll() {
        ArrayList<Component> builds = new ArrayList<>();
        try {
            HttpResponse<String> response = apiClient.get("component");
            JSONObject object = new JSONObject(response.getBody());

            JSONArray array = object.getJSONObject("result").getJSONObject("content").getJSONArray("Components");
            String status = object.getString("status");
            Log.e("body", status);
            if (status.equals("OK")) {
                for (int i = 0; i < array.length(); i++) {
                    Component component = new Component();
                    JSONObject item = array.getJSONObject(i);
                    component.setDateAdd(Utils.parseDate(item.getString("dateAdd")));
                    component.setName(item.getString("name"));
                    component.setSync("true".equals(item.getString("synchronized")));
                    builds.add(component);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return builds;
    }

    public Component getById(int id) {
        Component component;
        try {
            component = SugarRecord.find(Component.class, "bs_id=?", String.valueOf(id)).get(0);
            Log.e("wczytany Component", component.toString());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            component = new Component();
            component.setBsId(id);
        }
        if (!component.isSync()) {
            try {
                HttpResponse<String> response = apiClient.get("build/" + id);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                Log.e("status", status);
                if (status.equals("OK")) {
                    Log.e("Json", "start");
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content").getJSONObject("Build");
                    component.setDateAdd(Utils.parseDate(reader.getJSONObject("DateAdd").getString("date")));
                    component.setDateEdit(Utils.parseDate(reader.getString("DateEdit")));
                    component.setName(reader.getString("Name"));
                    Log.e("name", reader.getString("Name"));
                    component.setStatus(reader.getInt("Status"));
                    component.setSync(true);
                    SugarRecord.save(component);
                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
            Log.e("record Component", String.valueOf(SugarRecord.findWithQuery(Component.class, "SELECT * FROM component WHERE bs_id=?", String.valueOf(1))));
        }
        return component;
    }
}
