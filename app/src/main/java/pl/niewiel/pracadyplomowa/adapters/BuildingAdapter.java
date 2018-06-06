package pl.niewiel.pracadyplomowa.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.database.model.Building;
import pl.niewiel.pracadyplomowa.fragments.lists.BuildingView;
import pl.niewiel.pracadyplomowa.placeholders.BuildingViewHolder;

public class BuildingAdapter extends ArrayAdapter<Building> {
    public BuildingAdapter(@NonNull Context context, int resource, @NonNull List<Building> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final BuildingViewHolder viewHolder;
        final Building building = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.build_row, parent, false);
            viewHolder = new BuildingViewHolder();
            viewHolder.mId = convertView.findViewById(R.id.row_bsId);
            viewHolder.name = convertView.findViewById(R.id.row_name);
            viewHolder.dateAdd = convertView.findViewById(R.id.row_date_add);
            viewHolder.dateEdit = convertView.findViewById(R.id.row_date_edit);
            viewHolder.dateStart = convertView.findViewById(R.id.row_date_start);
            viewHolder.dateEnd = convertView.findViewById(R.id.row_date_end);
            viewHolder.latitude = convertView.findViewById(R.id.row_latitude);
            viewHolder.longitude = convertView.findViewById(R.id.row_longitude);
            viewHolder.sync = convertView.findViewById(R.id.row_sync);


            viewHolder.mId.setText(String.valueOf(building.getmId()));
            viewHolder.name.setText((building.getName()));
            viewHolder.dateAdd.setText(String.valueOf(building.getDateAdd()));
            if (building.isSync()) {
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sync_true));
            }
            viewHolder.dateStart.setText(String.valueOf(building.getDateStart()));
            viewHolder.dateEnd.setText(String.valueOf(building.getDateEnd()));
            viewHolder.latitude.setText((building.getLatitude()));
            viewHolder.longitude.setText((building.getLongitude()));
            viewHolder.sync.setText(String.valueOf(building.isSync()));
            if (building.isSync()) {
                viewHolder.sync.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sync_true));
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BuildingViewHolder) convertView.getTag();
            viewHolder.mId.setText(String.valueOf(building.getmId()));
            viewHolder.name.setText((building.getName()));
            viewHolder.dateAdd.setText(String.valueOf(building.getDateAdd()));
            if (building.getDateEdit() != null)
                viewHolder.dateEdit.setText(String.valueOf(building.getDateEdit()));
            viewHolder.dateStart.setText(String.valueOf(building.getDateStart()));
            viewHolder.dateEnd.setText(String.valueOf(building.getDateEnd()));
            viewHolder.latitude.setText((building.getLatitude()));
            viewHolder.longitude.setText((building.getLongitude()));
            viewHolder.sync.setText(String.valueOf(building.isSync()));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Build row", building.toString());
                Intent intent = new Intent(getContext(), BuildingView.class);
                intent.putExtra("mId", building.getmId());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
