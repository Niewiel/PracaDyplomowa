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
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.httpclient.ApiClient;

public class ComponentTypeService {
    private ApiClient apiClient = new ApiClient();

    public ComponentTypeService() {
    }

    public ArrayList<ComponentType> getAll() {
        ArrayList<ComponentType> componentTypes = new ArrayList<>();
        try {
            HttpResponse<String> response = apiClient.get("component-type");
            JSONObject object = new JSONObject(response.getBody());

            JSONArray array = object.getJSONObject("result").getJSONObject("content").getJSONArray("ComponentTypes");
            String status = object.getString("status");
            Log.e("body", status);
            if (status.equals("OK")) {
                for (int i = 0; i < array.length(); i++) {
                    ComponentType componentType = new ComponentType();
                    JSONObject item = array.getJSONObject(i);
                    componentType.setDateAdd(Utils.parseDate(item.getString("dateAdd")));
                    componentType.setName(item.getString("name"));
                    componentType.setSync("true".equals(item.getString("synchronized")));
                    componentTypes.add(componentType);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return componentTypes;
    }

    public ComponentType getById(int id) {
        ComponentType componentType;
        try {
            componentType = SugarRecord.find(ComponentType.class, "bs_id=?", String.valueOf(id)).get(0);
            Log.e("wczytany component-type", componentType.toString());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            componentType = new ComponentType();
            componentType.setBsId(id);
        }
        if (!componentType.isSync()) {
            try {
                HttpResponse<String> response = apiClient.get("component-type/" + id);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                Log.e("status", status);
                if (status.equals("OK")) {
                    Log.e("Json", "start");
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content").getJSONObject("ComponentType");
                    componentType.setDateAdd(Utils.parseDate(reader.getJSONObject("DateAdd").getString("date")));
                    componentType.setDateEdit(Utils.parseDate(reader.getString("DateEdit")));
                    componentType.setName(reader.getString("Name"));
                    Log.e("name", reader.getString("Name"));
                    componentType.setSync(true);
                    componentType.setmId(SugarRecord.save(componentType));
                    SugarRecord.save(componentType);
                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
            Log.e("record Component", String.valueOf(SugarRecord.findWithQuery(Component.class, "SELECT * FROM component_type WHERE bs_id=?", String.valueOf(1))));
        }
        return componentType;
    }
}
