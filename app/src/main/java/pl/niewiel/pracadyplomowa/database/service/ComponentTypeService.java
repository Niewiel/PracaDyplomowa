package pl.niewiel.pracadyplomowa.database.service;

import android.content.Context;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.httpclient.ApiClient;

public class ComponentTypeService {
    private ApiClient apiClient = new ApiClient();
    private Context context;

    public ComponentTypeService(Context context) {
        this.context = context;
    }

    public ArrayList<ComponentType> getAll() {
        ArrayList<ComponentType> componentTypes = new ArrayList<>();
        if (Utils.isOnline(context)) {
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
                        componentType.setBsId(item.getInt("id"));
                        componentType.setDateAdd(Utils.parseDate(item.getString("dateAdd")));
                        componentType.setName(item.getString("name"));
                        componentType.setSync("true".equals(item.getString("synchronized")));
//                    componentType=getById((int) componentType.getBsId());
                        componentTypes.add(componentType);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return componentTypes;
    }

    public ComponentType getById(int id) {
        ComponentType componentType;
        try {
            componentType = SugarRecord.find(ComponentType.class, "bs_id=?", String.valueOf(id)).get(0);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            componentType = new ComponentType();
            componentType.setBsId(id);
        }
        if (!componentType.isSync() && Utils.isOnline(context)) {
            try {
                HttpResponse<String> response = apiClient.get("component-type/" + id);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                Log.e("status", status);
                if (status.equals("OK")) {
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content").getJSONObject("ComponentType");
                    componentType.setDateAdd(Utils.parseDate(reader.getJSONObject("DateAdd").getString("date")));
                    componentType.setDateEdit(Utils.parseDate(reader.getString("DateEdit")));
                    componentType.setName(reader.getString("Name"));
                    componentType.setSync(true);
                    componentType.setmId(SugarRecord.save(componentType));
                    componentType.setBsId(id);
                    SugarRecord.save(componentType);
                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return componentType;
    }
}
