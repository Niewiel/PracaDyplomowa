package pl.niewiel.pracadyplomowa.view.builds;

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
import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.view.build.BuildView;

public class BuildsAdapter extends ArrayAdapter<Build> {
    public BuildsAdapter(@NonNull Context context, int resource, @NonNull List<Build> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final BuildsViewHolder viewHolder;
        final Build build = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.builds_row, parent, false);
            viewHolder = new BuildsViewHolder();
            viewHolder.bsId = convertView.findViewById(R.id.row_bsId);
            viewHolder.name = convertView.findViewById(R.id.row_name);
            viewHolder.dateAdd = convertView.findViewById(R.id.row_date_add);
            viewHolder.sync = convertView.findViewById(R.id.row_sync);


            viewHolder.bsId.setText(String.valueOf(build.getBsId()));
            viewHolder.name.setText((build.getName()));
            viewHolder.dateAdd.setText(String.valueOf(build.getDateAdd()));
            viewHolder.sync.setText(String.valueOf(build.isSync()));
            if (build.isSync()) {
                viewHolder.sync.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sync_true));
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BuildsViewHolder) convertView.getTag();
            viewHolder.bsId.setText(String.valueOf(build.getBsId()));
            viewHolder.name.setText((build.getName()));
            viewHolder.dateAdd.setText(String.valueOf(build.getDateAdd()));
            viewHolder.sync.setText(String.valueOf(build.isSync()));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BuildView.class);
                intent.putExtra("id", build.getBsId());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
