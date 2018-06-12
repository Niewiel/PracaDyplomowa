package pl.niewiel.pracadyplomowa.database.service;

import android.content.Context;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.apiClients.ApiClient;
import pl.niewiel.pracadyplomowa.database.model.Building;
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.database.model.ComponentToBuilding;

public class BuildingService implements Service<Building> {
    private static ApiClient apiClient = new ApiClient();
    private Context context;
    private ComponentService componentService;

    public BuildingService(Context context) {
        this.context = context;
        componentService = new ComponentService(context);
    }

    public ArrayList<Building> getAll() {
        ArrayList<Building> buildings = new ArrayList<>();
        if (Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.get("building");
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                Log.e("body", status);
                if (status.equals("OK")) {
                    JSONArray array = object.getJSONObject("result").getJSONObject("content").getJSONArray("Buildings");
                    for (int i = 0; i < array.length(); i++) {
                        Building building = new Building();
                        JSONObject item = array.getJSONObject(i);
                        building.setBsId(item.getInt("id"));
                        building.setSync("true".equals(item.getString("synchronized")));
                        building = getById((int) building.getBsId());
                        buildings.add(building);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return buildings;
    }

    public Building getById(int id) {
        Building building;
        try {
            building = SugarRecord.find(Building.class, "bs_id=?", String.valueOf(id)).get(0);
            Log.e("wczytany budynek", building.toString());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            building = new Building();
            building.setBsId(id);
        }
        if (!building.isSync() && Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.get("building/" + id);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                Log.e("status", status);
                if (status.equals("OK")) {
                    Log.e("Json", "start");
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content").getJSONObject("Building");
                    building.setDateAdd(Utils.parseDate(reader.getJSONObject("DateAdd").getString("date")));
                    building.setDateStart(Utils.parseDate(reader.getJSONObject("DateStart").getString("date")));
                    building.setDateEnd(Utils.parseDate(reader.getJSONObject("DateEnd").getString("date")));
                    if (!reader.getString("DateEdit").equals("null"))
                        building.setDateEdit(Utils.parseDate(reader.getJSONObject("DateEdit").getString("date")));
                    building.setName(reader.getString("Name"));
                    Log.e("budynek", reader.getString("Name"));
                    building.setLatitude(reader.getString("Latitude"));
                    building.setLongitude(reader.getString("Longitude"));
                    building.setSync(true);

                    building.setmId(SugarRecord.save(building));
                    SugarRecord.save(building);
                    Log.e("saved", String.valueOf(SugarRecord.save(building)));
                    //components to build
                    List<Component> componentTypes = new LinkedList<>();
                    JSONArray array = object.getJSONObject("result").getJSONObject("content").getJSONArray("components");
                    ComponentToBuilding components;
                    for (int i = 0; i < array.length(); i++) {
                        Log.e("type id", String.valueOf(array.getJSONObject(i).getInt("id")));
                        componentTypes.add(componentService.getById(array.getJSONObject(i).getInt("id")));
                    }
                    for (Component component : componentTypes) {
                        components = new ComponentToBuilding(component, building);
                        SugarRecord.save(components);

                    }

                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return building;
    }

    @Override
    public boolean add(Building item) {
        return false;
    }

    @Override
    public boolean delete(Building item) {
        return false;
    }

    @Override
    public boolean update(Building item) {
        return false;
    }

    @Override
    public void synchronize() {
        List<Building> toSynchronize = SugarRecord.find(Building.class, "SYNC=?", String.valueOf(0));

        if (Utils.IS_ONLINE) {
            if (!toSynchronize.isEmpty()) {
                for (Building bi :
                        toSynchronize) {
                    add(bi);
                }
            }
        }
    }

}
