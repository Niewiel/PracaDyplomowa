package pl.niewiel.pracadyplomowa.view.componentTypeList;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.view.componentType.ComponentTypeView;

public class ComponentTypeListAdapter extends ArrayAdapter<ComponentType> {
    public ComponentTypeListAdapter(@NonNull Context context, int resource, @NonNull List<ComponentType> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ComponentTypeListViewHolder viewHolder;
        final ComponentType componentType = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.build_list_row, parent, false);
            viewHolder = new ComponentTypeListViewHolder();
            viewHolder.mId = convertView.findViewById(R.id.row_bsId);
            viewHolder.name = convertView.findViewById(R.id.row_name);
            viewHolder.dateAdd = convertView.findViewById(R.id.row_date_add);
            viewHolder.sync = convertView.findViewById(R.id.row_sync);


            viewHolder.mId.setText(String.valueOf(componentType.getmId()));
            viewHolder.name.setText((componentType.getName()));
            viewHolder.dateAdd.setText(String.valueOf(componentType.getDateAdd()));
            viewHolder.sync.setText(String.valueOf(componentType.isSync()));
            if (componentType.isSync()) {
                viewHolder.sync.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sync_true));
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ComponentTypeListViewHolder) convertView.getTag();
            viewHolder.mId.setText(String.valueOf(componentType.getmId()));
            viewHolder.name.setText((componentType.getName()));
            viewHolder.dateAdd.setText(String.valueOf(componentType.getDateAdd()));
            viewHolder.sync.setText(String.valueOf(componentType.isSync()));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ComponentTypeView.class);
                if (componentType.getBsId() != 0)
                    intent.putExtra("mId", componentType.getBsId());
                intent.putExtra("id", componentType.getmId());
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }
}
