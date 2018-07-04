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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.activity.ComponentTypeActivity;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.placeholders.ComponentTypeListViewHolder;

public class ComponentTypeListAdapter extends ArrayAdapter<ComponentType> {
    private boolean selectable;
    private Set<ComponentType> selected = new HashSet<>();

    public ComponentTypeListAdapter(@NonNull Context context, int resource, @NonNull List<ComponentType> objects, boolean selectable) {
        super(context, resource, objects);
        this.selectable = selectable;
    }

    public Set<ComponentType> getSelected() {
        return selected;
    }

    public void setSelected(Set<ComponentType> selected) {
        Log.e("Adapter", selected.toString());
        this.selected = selected;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ComponentTypeListViewHolder viewHolder;
        final ComponentType componentType = getItem(position);
//        Log.e("Contains", String.valueOf(selected.contains(componentType)) + " " + componentType.toString());

        if (convertView == null) {
            if (componentType.getmId() != 0) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                convertView = layoutInflater.inflate(R.layout.list_row, parent, false);
                viewHolder = new ComponentTypeListViewHolder();
                viewHolder.mId = convertView.findViewById(R.id.row_bsId);
                viewHolder.name = convertView.findViewById(R.id.row_name);
                viewHolder.dateAdd = convertView.findViewById(R.id.row_date_add);
                viewHolder.sync = convertView.findViewById(R.id.row_sync);
                viewHolder.checkBox = convertView.findViewById(R.id.checkBox);


                viewHolder.mId.setText(String.valueOf(componentType.getmId()));
                viewHolder.name.setText((componentType.getName()));
                viewHolder.dateAdd.setText(String.valueOf(componentType.getDateAdd()));
                viewHolder.sync.setText(String.valueOf(componentType.isSync()));
                if (componentType.isSync()) {
                    convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sync_true));
                }

                convertView.setTag(viewHolder);
            }
        } else {
            if (componentType.getmId() != 0) {
                viewHolder = (ComponentTypeListViewHolder) convertView.getTag();
                viewHolder.mId.setText(String.valueOf(componentType.getmId()));
                viewHolder.name.setText((componentType.getName()));
                viewHolder.dateAdd.setText(String.valueOf(componentType.getDateAdd()));
                viewHolder.sync.setText(String.valueOf(componentType.isSync()));
                if (selectable) {
                    if (equals(componentType))
                        viewHolder.checkBox.setChecked(true);
                }
            }


        }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ComponentTypeActivity.class);
                    intent.putExtra("type", componentType.getmId());
                    getContext().startActivity(intent);
                }
            });

        Log.i("List", selected.toString());

        return convertView;
    }

    private boolean equals(ComponentType componentType) {
        boolean eq = false;
        for (ComponentType c :
                selected) {
            eq = c.equals(componentType);
        }
        return eq;
    }

}
