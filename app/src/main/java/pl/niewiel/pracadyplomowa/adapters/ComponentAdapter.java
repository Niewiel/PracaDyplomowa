package pl.niewiel.pracadyplomowa.adapters;

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
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.placeholders.ComponentViewHolder;

public class ComponentAdapter extends ArrayAdapter<Component> {
    public ComponentAdapter(@NonNull Context context, int resource, @NonNull List<Component> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ComponentViewHolder viewHolder;
        final Component component = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.component_row, parent, false);
            viewHolder = new ComponentViewHolder();
            viewHolder.mId = convertView.findViewById(R.id.row_bsId);
            viewHolder.name = convertView.findViewById(R.id.row_name);
            viewHolder.dateAdd = convertView.findViewById(R.id.row_date_add);
            viewHolder.dateEdit = convertView.findViewById(R.id.row_date_edit);
            viewHolder.sync = convertView.findViewById(R.id.row_sync);


            viewHolder.mId.setText(String.valueOf(component.getmId()));
            viewHolder.name.setText((component.getName()));
            viewHolder.dateAdd.setText(String.valueOf(component.getDateAdd()));
            if (!String.valueOf(component.getDateEdit()).equals("null"))
                viewHolder.dateEdit.setText(String.valueOf(component.getDateEdit()));
            else
                viewHolder.dateEdit.setText(R.string.never_edited);
            viewHolder.sync.setText(String.valueOf(component.getStatus()));
            if (component.isSync()) {
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sync_true));
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ComponentViewHolder) convertView.getTag();
            viewHolder.mId.setText(String.valueOf(component.getmId()));
            viewHolder.name.setText((component.getName()));
            viewHolder.dateAdd.setText(String.valueOf(component.getDateAdd()));
            if (!String.valueOf(component.getDateEdit()).equals("null"))
                viewHolder.dateEdit.setText(String.valueOf(component.getDateEdit()));
            else
                viewHolder.dateEdit.setText(R.string.never_edited);
            viewHolder.sync.setText(String.valueOf(component.getStatus() + "%"));
        }
        return convertView;
    }
}
