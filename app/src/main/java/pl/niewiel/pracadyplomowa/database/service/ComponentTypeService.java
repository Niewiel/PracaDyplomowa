package pl.niewiel.pracadyplomowa.database.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mashape.unirest.http.HttpResponse;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.apiClients.ApiClient;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;

public class ComponentTypeService implements Service<ComponentType> {
    private static final String DEBUG_TAG = "ComponentTypeService";
    private ApiClient apiClient = new ApiClient();
    private Context context;

    public ComponentTypeService(Context context) {
        this.context = context;
    }

    public ArrayList<ComponentType> getAll() {
        ArrayList<ComponentType> componentTypes = new ArrayList<>();
        if (Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.get("component-type");
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                Log.e("body", status);
                if (status.equals("OK")) {
                    JSONArray array = object.getJSONObject("result").getJSONObject("content").getJSONArray("ComponentTypes");

                    for (int i = 0; i < array.length(); i++) {
                        ComponentType componentType = new ComponentType();
                        JSONObject item = array.getJSONObject(i);
                        componentType.setBsId(item.getInt("id"));
                        componentType.setName(item.getString("name"));
                        componentType.setSync("true".equals(item.getString("synchronized")));
                        componentType = getById((int) componentType.getBsId());
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
        ComponentType componentType = new ComponentType();
        try {
            componentType = SugarRecord.find(ComponentType.class, "bs_id=?", String.valueOf(id)).get(0);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            componentType.setBsId(id);
        }
        if (!componentType.isSync() && Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.get("component-type/" + id);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                Log.e("status", status);
                if (status.equals("OK")) {
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content").getJSONObject("ComponentType");
                    componentType.setDateAdd(Utils.parseDate(reader.getJSONObject("DateAdd").getString("date")));
                    if (!reader.getString("DateEdit").equals("null"))
                        componentType.setDateEdit(Utils.parseDate(reader.getJSONObject("DateEdit").getString("date")));
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

    public boolean add(ComponentType componentType) {
        Map<String, Object> params = new HashMap<>();
        params.put("Name", componentType.getName());
        String message = "no message";
        if (Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.post("component-type", params);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                message = object.getString("status");
                Log.e("add", String.valueOf(object));
                if (status.equals("OK")) {
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content").getJSONObject("ComponentType");
                    componentType.setBsId(reader.getInt("id"));
                    componentType.setSync(true);
                    SugarRecord.save(componentType);
                }
                return true;
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, message);
                return false;
            }
        } else
            return false;

    }

    public boolean delete(ComponentType componentType) {
        if (Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.delete("component-type/" + componentType.getBsId());
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                if (status.equals("OK")) {
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content");
                    Toast.makeText(context, reader.getString("message"), Toast.LENGTH_LONG).show();
                    SugarRecord.delete(componentType);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        } else
            return false;

    }


    @Override
    public boolean update(ComponentType componentType) {
        Map<String, Object> params = new HashMap<>();
        params.put("Name", componentType.getName());
        String message = "no message";
        if (Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.put("component-type/" + (int) componentType.getBsId(), params);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                message = object.getString("status");
                Log.e("updated", String.valueOf(object));
                if (status.equals("OK")) {
                    ComponentType tmp = SugarRecord.findById(ComponentType.class, componentType.getmId());
                    tmp.setSync(true);
                    SugarRecord.update(tmp);
                }
                return true;
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, message);
                return false;
            }
        } else
            return false;
    }

    @Override
    public void synchronize() {
        List<ComponentType> toSynchronize = SugarRecord.find(ComponentType.class, "SYNC=?", "0");

        if (Utils.IS_ONLINE) {
            if (!toSynchronize.isEmpty()) {
                for (ComponentType ct :
                        toSynchronize) {
                    add(ct);
                }
            }
        }
    }
}
