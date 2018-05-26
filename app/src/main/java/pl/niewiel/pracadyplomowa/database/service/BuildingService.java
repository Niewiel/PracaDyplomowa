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
import pl.niewiel.pracadyplomowa.database.model.Building;
import pl.niewiel.pracadyplomowa.httpclient.ApiClient;

public class BuildingService {
    private static ApiClient apiClient = new ApiClient();
    private Context context;

    public BuildingService(Context context) {
        this.context = context;
    }

    public ArrayList<Building> getAll() {
        ArrayList<Building> buildings = new ArrayList<>();
        if (Utils.isOnline(context)) {
            try {
                HttpResponse<String> response = apiClient.get("building");
                JSONObject object = new JSONObject(response.getBody());

                JSONArray array = object.getJSONObject("result").getJSONObject("content").getJSONArray("Buildings");
                String status = object.getString("status");
                Log.e("body", status);
                if (status.equals("OK")) {
                    for (int i = 0; i < array.length(); i++) {
                        Building building = new Building();
                        JSONObject item = array.getJSONObject(i);
                        building.setBsId(item.getInt("id"));
                        building = getById(building.getBsId());
//                    build.setDateAdd(Utils.parseDate(item.getString("dateAdd")));
//                    build.setName(item.getString("name"));
//                    build.setSync(false);//"true".equals(item.getString("synchronized")));
                        buildings.add(building);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return buildings;
    }

    public Building getById(long id) {
        Building building;
        try {
            building = SugarRecord.find(Building.class, "bs_id=?", String.valueOf(id)).get(0);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            building = new Building();
        }
        if (!building.isSync() && Utils.isOnline(context)) {
            try {
                HttpResponse<String> response = apiClient.get("building/" + id);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                Log.e("status", status);
                if (status.equals("OK")) {
                    Log.e("Json", "start");
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content").getJSONObject("Building");
                    building.setDateAdd(Utils.parseDate(reader.getJSONObject("DateAdd").getString("date")));
                    building.setDateEdit(Utils.parseDate(reader.getString("DateEdit")));
                    building.setName(reader.getString("Name"));
                    Log.e("budynek", reader.getString("Name"));
                    building.setLatitude(reader.getString("Latitude"));
                    building.setLongitude(reader.getString("Longitude"));
                    building.setSync(true);
                    building.setmId(SugarRecord.save(building));
                    SugarRecord.save(building);
                    Log.e("saved", String.valueOf(SugarRecord.save(building)));


                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
            Log.e("record", String.valueOf(SugarRecord.findWithQuery(Building.class, "SELECT * FROM building WHERE bs_id=?", String.valueOf(1))));
        }
        return building;
    }
}
