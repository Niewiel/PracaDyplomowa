package pl.niewiel.pracadyplomowa.database.service;

import android.content.Context;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.apiClients.ApiClient;
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.database.model.middleTables.TypesToComponent;

public class ComponentService implements Service<Component> {
    private static final String DEBUG_TAG = "ComponentService";
    private ApiClient apiClient;
    private ComponentTypeService componentTypeService;


    public ComponentService(Context context) {
        componentTypeService = new ComponentTypeService(context);
        apiClient = new ApiClient();
    }

    public ArrayList<Component> getAll() {
        ArrayList<Component> builds = new ArrayList<>();
        if (Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.get("component");
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                Log.e("body", status);
                if (status.equals("OK")) {
                    JSONArray array = object.getJSONObject("result").getJSONObject("content").getJSONArray("Components");

                    for (int i = 0; i < array.length(); i++) {
                        Component component = new Component();
                        JSONObject item = array.getJSONObject(i);
                        component.setBsId(item.getInt("id"));
                        Log.e("id", String.valueOf(item.getInt("id")));
                        component.setSync("true".equals(item.getString("synchronized")));
                        component = getById((int) component.getBsId());
                        builds.add(component);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(DEBUG_TAG, "getAll JSON");

            }
        }

        return builds;
    }

    public Component getById(int id) {
        Component component = new Component();

        Log.e("getcomponentById", String.valueOf(id));
        try {
            component = SugarRecord.find(Component.class, "bs_id=?", String.valueOf(id)).get(0);
            Log.e("wczytany Component", component.toString());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            component.setBsId(id);
        }
        if (!component.isSync() && Utils.IS_ONLINE) {
            try {

                HttpResponse<String> response = apiClient.get("component/" + id);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                Log.e("status", status);
                if (status.equals("OK")) {
                    Log.e("Json", "start");
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content").getJSONObject("Component");
                    component.setDateAdd(Utils.parseDate(reader.getJSONObject("DateAdd").getString("date")));
                    if (!reader.getString("DateEdit").equals("null"))
                        component.setDateEdit(Utils.parseDate(reader.getJSONObject("DateEdit").getString("date")));
                    component.setName(reader.getString("Name"));
                    Log.e("name", reader.getString("Name"));
                    component.setStatus(reader.getInt("Status"));
                    component.setSync(true);
                    component.setmId(SugarRecord.save(component));
                    SugarRecord.save(component);
                    //types to component
                    List<ComponentType> componentTypes = new LinkedList<>();
                    JSONArray array = object.getJSONObject("result").getJSONObject("content").getJSONArray("types");
                    TypesToComponent types;
                    for (int i = 0; i < array.length(); i++) {
                        Log.e("type id", String.valueOf(array.getJSONObject(i).getInt("id")));
                        componentTypes.add(componentTypeService.getById(array.getJSONObject(i).getInt("id")));
                    }
                    for (ComponentType type : componentTypes) {
                        types = new TypesToComponent(component, type);
                        SugarRecord.save(types);

                    }
                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return component;
    }

    @Override
    public boolean add(Component item) {
        Map<String, Object> params = new HashMap<>();
        List<TypesToComponent> ids = SugarRecord.find(TypesToComponent.class, "component_id=?", String.valueOf(item.getmId()));
        List<Integer> list = new LinkedList<>();
        for (TypesToComponent t : ids) {
            list.add((int) SugarRecord.findById(ComponentType.class, t.getTypeId()).getBsId());
        }
        params.put("Name", item.getName());
        params.put("Status", item.getStatus());
        params.put("Types", list);
        String message = "no message";
        if (Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.post("component", params);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                message = object.getString("status");
                Log.i("add", String.valueOf(object));
                if (status.equals("OK")) {
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content").getJSONObject("ComponentType");
                    item.setBsId(reader.getInt("id"));
                    item.setSync(true);
                    SugarRecord.save(item);
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
    public boolean delete(Component item) {
        return false;
    }

    @Override
    public boolean update(Component item) {
        return false;
    }

    @Override
    public void synchronize() {
        List<Component> toSynchronize = SugarRecord.find(Component.class, "SYNC=?", String.valueOf(0));

        if (Utils.IS_ONLINE) {
            if (!toSynchronize.isEmpty()) {
                for (Component c :
                        toSynchronize) {
                    add(c);
                }
            }

        }
    }
}