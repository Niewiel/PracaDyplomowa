package pl.niewiel.pracadyplomowa.database.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mashape.unirest.http.HttpResponse;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.apiClients.ApiClient;
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.database.model.Photo;
import pl.niewiel.pracadyplomowa.database.model.PhotoToComponent;
import pl.niewiel.pracadyplomowa.database.model.TypesToComponent;

public class ComponentService implements Service<Component> {
    private static final String DEBUG_TAG = "ComponentService";
    private ApiClient apiClient;
    private ComponentTypeService componentTypeService;
    private Context context;


    public ComponentService(Context context) {
        componentTypeService = new ComponentTypeService(context);
        apiClient = new ApiClient();
        this.context = context;
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
                    if (component.getmId() == 0)
                        component.setmId(SugarRecord.save(component));
                    SugarRecord.save(component);

                    //photo to component
                    PhotoService ps = new PhotoService(context);
                    Photo photo;
                    JSONArray files = object.getJSONObject("result").getJSONObject("content").getJSONArray("files");
                    for (int i = 0; i < files.length(); i++) {
                        photo = ps.getById(files.getJSONObject(i).getInt("id"));
                        Log.d("PHOTO", photo.toString());
                        SugarRecord.save(new PhotoToComponent(photo, component));
                    }

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
                        types.setMid(SugarRecord.save(types));
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
        List<PhotoToComponent> photos = SugarRecord.find(PhotoToComponent.class, "component_id=?", String.valueOf(item.getmId()));
        //types
        Set<Integer> list = new HashSet<>();
        for (TypesToComponent t : ids) {
            list.add((int) SugarRecord.findById(ComponentType.class, t.getTypeId()).getBsId());
        }
//        files
        Set<File> files = new HashSet<>();
        for (PhotoToComponent p :
                photos) {
            files.add(new File(SugarRecord.findById(Photo.class, p.getPhotoId()).getPath()));
        }
        params.put("Name", item.getName());
        params.put("Status", item.getStatus());
        params.put("Types", new LinkedList<>(list));
        params.put("File", new LinkedList<>(files));
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
//                    item.setmId(SugarRecord.save(item));
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
        if (Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.delete("component/" + item.getBsId());
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
    public boolean update(Component item) {
        Map<String, Object> params = new HashMap<>();
        List<TypesToComponent> ids = SugarRecord.find(TypesToComponent.class, "component_id=?", String.valueOf(item.getmId()));
        List<PhotoToComponent> photos = SugarRecord.find(PhotoToComponent.class, "component_id=?", String.valueOf(item.getmId()));
        //types
        Set<Integer> list = new HashSet<>();
        for (TypesToComponent t : ids) {
            list.add((int) SugarRecord.findById(ComponentType.class, t.getTypeId()).getBsId());
        }
//        files
        Set<File> files = new HashSet<>();
        for (PhotoToComponent p :
                photos) {
            files.add(new File(SugarRecord.findById(Photo.class, p.getPhotoId()).getPath()));
        }
        Log.d("FILES", files.toString());
        params.put("Name", item.getName());
        params.put("Status", item.getStatus());
        params.put("Types", new LinkedList<>(list));
        params.put("File", new LinkedList<>(files));
        String message = "no message";
        if (Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.put("component/" + (int) item.getBsId(), params);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                message = object.getString("status");
                Log.i("update", message);
                if (status.equals("OK")) {
                    item.setSync(true);
                    SugarRecord.save(item);
                    Toast.makeText(context, "Update successful", Toast.LENGTH_LONG).show();
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