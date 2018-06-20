package pl.niewiel.pracadyplomowa.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class FileListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Bitmap> list;

    public FileListAdapter(Context c, ArrayList<Bitmap> list) {
        mContext = c;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(25, 50, 25, 25);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(list.get(position), 250, 250));


        return imageView;
    }
}

