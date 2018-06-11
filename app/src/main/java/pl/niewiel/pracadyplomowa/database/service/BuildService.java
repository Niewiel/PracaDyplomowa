package pl.niewiel.pracadyplomowa.database.service;

import android.content.Context;
import android.util.Log;
import com.mashape.unirest.http.HttpResponse;
import com.orm.SugarRecord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.apiClients.ApiClient;
import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.database.model.Building;
import pl.niewiel.pracadyplomowa.database.model.BuildingToBuild;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BuildService implements Service<Build> {
    private static ApiClient apiClient;
    private BuildingService buildingService;

    public BuildService(Context context) {
        buildingService = new BuildingService(context);
        apiClient = new ApiClient();
    }

    public ArrayList<Build> getAll() {
        ArrayList<Build> builds = new ArrayList<>();
        if (Utils.IS_ONLINE) {
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
                        build.setBsId(item.getInt("id"));
                        build = getById((int) build.getBsId());
//                    build.setDateAdd(Utils.parseDate(item.getString("dateAdd")));
//                    build.setName(item.getString("name"));
//                    build.setSync(false);//"true".equals(item.getString("synchronized")));
                        builds.add(build);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return builds;
    }

    public Build getById(int id) {
        Build build = new Build();
        try {
            build = SugarRecord.find(Build.class, "bs_id=?", String.valueOf(id)).get(0);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            SugarRecord.delete(build);
            build.setBsId(id);
        }

        if (!build.isSync() && Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.get("build/" + id);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                Log.e("status", status);
                if (status.equals("OK")) {
                    Log.e("Json", "start");
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content").getJSONObject("Build");
                    build.setDateAdd(Utils.parseDate(reader.getJSONObject("DateAdd").getString("date")));
                    if (!reader.getString("DateEdit").equals("null"))
                        build.setDateEdit(Utils.parseDate(reader.getJSONObject("DateEdit").getString("date")));
                    build.setName(reader.getString("Name"));
                    Log.e("name", reader.getString("Name"));
                    build.setLatitude(reader.getString("Latitude"));
                    build.setLongitude(reader.getString("Longitude"));
                    build.setSync(true);
                    build.setmId(SugarRecord.save(build));
                    SugarRecord.save(build);
                    Log.e("saved", String.valueOf(SugarRecord.save(build)));
                    List<Building> componentTypes = new LinkedList<>();
                    JSONArray array = object.getJSONObject("result").getJSONObject("content").getJSONArray("buildings");
                    BuildingToBuild buildings;
                    for (int i = 0; i < array.length(); i++) {
                        Log.e("type id", String.valueOf(array.getJSONObject(i).getInt("id")));
                        componentTypes.add(buildingService.getById(array.getJSONObject(i).getInt("id")));
                    }
                    for (Building building : componentTypes) {
                        buildings = new BuildingToBuild(building, build);
                        SugarRecord.save(buildings);

                    }


                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return build;
    }

    @Override
    public boolean add(Build item) {
        return false;
    }

    @Override
    public boolean delete(Build item) {
        return false;
    }

    @Override
    public boolean update(Build item) {
        return false;
    }

    @Override
    public void synchronize() {
        List<Build> toSynchronize = SugarRecord.find(Build.class, "SYNC=?", String.valueOf(0));

        if (Utils.IS_ONLINE) {
            if (!toSynchronize.isEmpty()) {
                for (Build bu :
                        toSynchronize) {
                    add(bu);
                }
            }

        }
    }
}
