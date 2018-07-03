package pl.niewiel.pracadyplomowa.database.service;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.apiClients.ApiClient;
import pl.niewiel.pracadyplomowa.database.model.Photo;

public class PhotoService implements Service<Photo> {
    private ApiClient apiClient = new ApiClient();
    private Context context;


    public PhotoService(Context context) {
        this.context = context;
    }

    @Override
    public ArrayList<Photo> getAll() {
        return null;
    }


    @Override
    public Photo getById(int id) {
        Photo photo = new Photo();
        try {
            photo = SugarRecord.find(Photo.class, "bs_id=?", String.valueOf(id)).get(0);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            photo.setBsId(id);
        }
        if (!photo.isSync() && Utils.IS_ONLINE) {
            try {
                HttpResponse<String> response = apiClient.get("file/" + id);
                JSONObject object = new JSONObject(response.getBody());
                String status = object.getString("status");
                Log.e("status", status);
                if (status.equals("OK")) {
                    JSONObject reader = object.optJSONObject("result").getJSONObject("content").getJSONObject("file");
                    photo.setName(reader.getString("name"));
                    photo.setSync(true);
                    photo.setPath(reader.getString("name"));
                    String path = reader.getString("path");
                    download(path, reader.getString("name"));
                    photo.setmId(SugarRecord.save(photo));
                    SugarRecord.save(photo);
                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return photo;
    }

    private void download(String url, String name) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url.replace("https", "http")));
        request.setDescription("Some descrition");
        request.setTitle("Some title");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_PICTURES, name);

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Objects.requireNonNull(manager).enqueue(request);
    }

    @Override
    public boolean create(Photo item) {
        return false;
    }

    @Override
    public boolean delete(Photo item) {
        return false;
    }

    @Override
    public boolean update(Photo item) {
        return false;
    }

    @Override
    public void synchronize() {

    }
}
