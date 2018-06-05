package pl.niewiel.pracadyplomowa.fragments.lists;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.LinkedList;
import java.util.List;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.adapters.ComponentTypeListAdapter;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.database.model.middleTables.TypesToComponent;
import pl.niewiel.pracadyplomowa.database.service.ComponentTypeService;
import pl.niewiel.pracadyplomowa.database.service.Service;
import pl.niewiel.pracadyplomowa.database.service.Synchronize;

public class ComponentTypeListView extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String DEBUG_TAG = "ComponentTypeListView";
    ListView listView;
    LinearLayout progressBar;
    List<ComponentType> list;
    List<TypesToComponent> ids;
    ComponentTypeListAdapter adapter;
    Bundle bundle;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        bundle = this.getArguments();
        listView = rootView.findViewById(R.id.list);
        progressBar = rootView.findViewById(R.id.progressbar_view);
        list = new LinkedList<>();
        adapter = new ComponentTypeListAdapter(getContext(), R.layout.list_row, list);
        listView.setAdapter(adapter);
        new Task().execute();
        if (list.isEmpty()) {
            Toast.makeText(getContext(), "No results", Toast.LENGTH_LONG).show();
            Log.e(DEBUG_TAG, "no results");
        }

        return rootView;
    }


    @Override
    public void onRefresh() {
        new Synchronize(getContext()).execute();
        new Task().execute();

    }

    private class Task extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            list.clear();
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            if (bundle == null)
                list.addAll(Utils.getAllComponentTypes());
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (Utils.IS_ONLINE) {
                Service service = new ComponentTypeService(getContext());
                service.getAll();
                if (bundle != null) {
                    if (bundle.getLong("mId") != 0)
                        ids = SugarRecord.find(TypesToComponent.class, "component_id=?", String.valueOf(bundle.getLong("mId")));
                    for (TypesToComponent t : ids) {
                        list.add(SugarRecord.findById(ComponentType.class, t.getTypeId()));
                    }
                }
            }
            return null;
        }
    }
}
