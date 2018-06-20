package pl.niewiel.pracadyplomowa.adapters;

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
import pl.niewiel.pracadyplomowa.activity.ComponentActivity;
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.placeholders.ComponentListViewHolder;

public class ComponentListAdapter extends ArrayAdapter<Component> {
    private boolean selectable;

    public ComponentListAdapter(@NonNull Context context, int resource, @NonNull List<Component> objects, boolean selectable) {
        super(context, resource, objects);
        this.selectable = selectable;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ComponentListViewHolder viewHolder;
        final Component component = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.list_row, parent, false);
            viewHolder = new ComponentListViewHolder();
            viewHolder.mId = convertView.findViewById(R.id.row_bsId);
            viewHolder.name = convertView.findViewById(R.id.row_name);
            viewHolder.dateAdd = convertView.findViewById(R.id.row_date_add);
            viewHolder.sync = convertView.findViewById(R.id.row_sync);
            if (selectable) {
                viewHolder.checkBox = convertView.findViewById(R.id.checkBox);
                viewHolder.checkBox.setVisibility(View.VISIBLE);
            }


            viewHolder.mId.setText(String.valueOf(component.getmId()));
            viewHolder.name.setText((component.getName()));
            viewHolder.dateAdd.setText(String.valueOf(component.getDateAdd()));
            viewHolder.sync.setText(String.valueOf(component.isSync()));
            if (viewHolder.sync.getText().toString().equals("true")) {
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sync_true));
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ComponentListViewHolder) convertView.getTag();
            viewHolder.mId.setText(String.valueOf(component.getmId()));
            viewHolder.name.setText((component.getName()));
            viewHolder.dateAdd.setText(String.valueOf(component.getDateAdd()));
            viewHolder.sync.setText(String.valueOf(component.isSync()));
            if (viewHolder.sync.getText().toString().equals("true")) {
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sync_true));
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ComponentActivity.class);
                intent.putExtra("component", component.getmId());
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }
}
