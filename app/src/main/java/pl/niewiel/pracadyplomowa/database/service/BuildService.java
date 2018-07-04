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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.apiClients.ApiClient;
import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.database.model.Building;
import pl.niewiel.pracadyplomowa.database.model.BuildingToBuild;

public class BuildService implements Service<Build> {
    private static ApiClient apiClient;
    private BuildingService buildingService;
    private Context context;

    public BuildService(Context context) {
        buildingService = new BuildingService(context);
        apiClient = new ApiClient();
        this.context = context;
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
                        build.setSync("true".equals(item.getString("synchronized")));
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
//                    JSONArray array = object.getJSONObject("result").getJSONObject("content").getJSONArray("buildings");
                    BuildingToBuild buildings;
//                    for (int i = 0; i < array.length(); i++) {
//                        Log.e("type id", String.valueOf(array.getJSONObject(i).getInt("id")));
                    componentTypes.add(buildingService.getById(object.getJSONObject("result").getJSONObject("content").getJSONObject("buildings").getInt("id")));
//                    }
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
    public boolean create(Build item) {
        List<BuildingToBuild> buildsIDs = SugarRecord.find(BuildingToBuild.class, "build_id=?", String.valueOf(item.getmId()));
        List<Integer> builds = new ArrayList<>();


        for (BuildingToBuild btb : buildsIDs)
            builds.add((int) SugarRecord.findById(Build.class, btb.getBuildId()).getBsId());


        Map<String, Object> params = new HashMap<>();
        params.put("Name", item.getName());
        params.put("Latitude", item.getLatitude());
        params.put("Longitude", item.getLongitude());
        params.put("Builds", builds.toArray());
        String message = "no message";
        if (Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.post("build", params);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                message = object.getString("status");
                Log.e("create", String.valueOf(object));
                if (status.equals("OK")) {
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content").getJSONObject("Buildings");
                    item.setBsId(reader.getInt("id"));
                    item.setSync(true);
                    SugarRecord.save(item);
                }
                return true;
            } catch (JSONException e) {
                Log.e("BuildingService", message);
                return false;
            }
        } else
            return false;

    }

    @Override
    public boolean delete(Build item) {
        if (Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.delete("build/" + item.getBsId());
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                if (status.equals("OK")) {
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content");
                    Toast.makeText(context, reader.getString("message"), Toast.LENGTH_LONG).show();
                    SugarRecord.delete(item);
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
                    create(bu);
                }
            }

        }
    }
}
