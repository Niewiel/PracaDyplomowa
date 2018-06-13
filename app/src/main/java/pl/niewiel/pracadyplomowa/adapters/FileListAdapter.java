package pl.niewiel.pracadyplomowa.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import pl.niewiel.pracadyplomowa.database.model.Photo;

public class FileListAdapter extends BaseAdapter {

    private Activity _activity;
    private ArrayList<Photo> _filePaths;
    private int imageWidth = 50;

    public FileListAdapter(Activity _activity, ArrayList<Photo> _filePaths) {
        this._activity = _activity;
        this._filePaths = _filePaths;

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(_activity);
        } else {
            imageView = (ImageView) convertView;
        }

        // get screen dimensions


        imageView.setImageURI(_filePaths.get(position).getPath());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
                imageWidth));


        // image view click listener
//        imageView.setOnClickListener(new OnImageClickListener(position));

        return imageView;
    }
}
