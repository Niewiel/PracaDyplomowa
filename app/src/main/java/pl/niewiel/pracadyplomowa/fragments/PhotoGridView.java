package pl.niewiel.pracadyplomowa.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.ArrayList;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.adapters.FileListAdapter;
import pl.niewiel.pracadyplomowa.database.model.Photo;
import pl.niewiel.pracadyplomowa.database.model.PhotoToComponent;

public class PhotoGridView extends Fragment {
    private static final String DEBUG_TAG = "PhotoGridView";
    GridView gridView;
    LinearLayout progressBar;
    ArrayList<Photo> list;
    ArrayList<Bitmap> bmps;
    ArrayList<PhotoToComponent> ids;
    FileListAdapter adapter;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid_layout, container, false);
        gridView = rootView.findViewById(R.id.photo_list);
        progressBar = rootView.findViewById(R.id.progressbar_view);
        bundle = this.getArguments();
        list = new ArrayList<>();
        bmps = new ArrayList<>();
        adapter = new FileListAdapter(getContext(), bmps);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Click", "");

            }
        });

        new Task().execute();
        return rootView;
    }


    class Task extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            gridView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            list.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ids = new ArrayList<>();
            ids.addAll(SugarRecord.find(PhotoToComponent.class, "component_id=?", String.valueOf(bundle.getLong("component"))));
            final BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
            options.inScaled = true;
            for (PhotoToComponent id :
                    ids) {
                list.add(SugarRecord.findById(Photo.class, id.getPhotoId()));
            }
            for (Photo p :
                    list) {
                bmps.add(BitmapFactory.decodeFile(p.getPath(), options));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
            if (list.isEmpty()) {
                Toast.makeText(getContext(), "No results", Toast.LENGTH_LONG).show();
                Log.e(DEBUG_TAG, "no results");
            }
            progressBar.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            super.onPostExecute(aVoid);
        }
    }


}
