package pl.niewiel.pracadyplomowa.view.buildList;

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
import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.view.build.BuildView;

public class BuildListAdapter extends ArrayAdapter<Build> {
    BuildListAdapter(@NonNull Context context, int resource, @NonNull List<Build> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final BuildListViewHolder viewHolder;
        final Build build = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.build_row, parent, false);
            viewHolder = new BuildListViewHolder();
            viewHolder.mId = convertView.findViewById(R.id.row_bsId);
            viewHolder.name = convertView.findViewById(R.id.row_name);
            viewHolder.dateAdd = convertView.findViewById(R.id.row_date_add);
            viewHolder.sync = convertView.findViewById(R.id.row_sync);


            viewHolder.mId.setText(String.valueOf(build.getmId()));
            viewHolder.name.setText((build.getName()));
            viewHolder.dateAdd.setText(String.valueOf(build.getDateAdd()));
            viewHolder.sync.setText(String.valueOf(build.isSync()));
            if (build.isSync()) {
                viewHolder.sync.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sync_true));
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BuildListViewHolder) convertView.getTag();
            viewHolder.mId.setText(String.valueOf(build.getmId()));
            viewHolder.name.setText((build.getName()));
            viewHolder.dateAdd.setText(String.valueOf(build.getDateAdd()));
            viewHolder.sync.setText(String.valueOf(build.isSync()));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Build row", build.toString());
                Intent intent = new Intent(getContext(), BuildView.class);
                if (build.getBsId() != 0)
                    intent.putExtra("mId", build.getBsId());
                intent.putExtra("id", build.getmId());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
