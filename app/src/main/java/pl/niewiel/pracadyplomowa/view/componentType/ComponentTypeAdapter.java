package pl.niewiel.pracadyplomowa.view.componentType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.database.model.Build;

public class ComponentTypeAdapter extends ArrayAdapter<Build> {
    public ComponentTypeAdapter(@NonNull Context context, int resource, @NonNull List<Build> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ComponentTypeViewHolder viewHolder;
        final Build build = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.build_list_row, parent, false);
            viewHolder = new ComponentTypeViewHolder();
            viewHolder.mId = convertView.findViewById(R.id.row_bsId);
            viewHolder.name = convertView.findViewById(R.id.row_name);
            viewHolder.dateAdd = convertView.findViewById(R.id.row_date_add);
            viewHolder.dateEdit = convertView.findViewById(R.id.row_date_edit);
            viewHolder.latitude = convertView.findViewById(R.id.row_latitude);
            viewHolder.longitude = convertView.findViewById(R.id.row_longitude);
            viewHolder.sync = convertView.findViewById(R.id.row_sync);


            viewHolder.mId.setText(String.valueOf(build.getmId()));
            viewHolder.name.setText((build.getName()));
            viewHolder.dateAdd.setText(String.valueOf(build.getDateAdd()));
            if (build.getDateEdit() != null) {
                viewHolder.dateEdit.setText(String.valueOf(build.getDateEdit()));
            }
            viewHolder.latitude.setText((build.getLatitude()));
            viewHolder.longitude.setText((build.getLongitude()));
            viewHolder.sync.setText(String.valueOf(build.isSync()));
            if (build.isSync()) {
                viewHolder.sync.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sync_true));
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ComponentTypeViewHolder) convertView.getTag();
            viewHolder.mId.setText(String.valueOf(build.getmId()));
            viewHolder.name.setText((build.getName()));
            viewHolder.dateAdd.setText(String.valueOf(build.getDateAdd()));
            if (build.getDateEdit() != null)
                viewHolder.dateEdit.setText(String.valueOf(build.getDateEdit()));

            viewHolder.latitude.setText((build.getLatitude()));
            viewHolder.longitude.setText((build.getLongitude()));
            viewHolder.sync.setText(String.valueOf(build.isSync()));
        }
        return convertView;
    }
}
