package pl.niewiel.pracadyplomowa.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.activity.add_edit.AddOrEditComponentType;
import pl.niewiel.pracadyplomowa.adapters.ComponentTypeListAdapter;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.database.model.TypesToComponent;
import pl.niewiel.pracadyplomowa.database.service.ComponentTypeService;
import pl.niewiel.pracadyplomowa.database.service.Service;

public class ComponentTypeListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MyFragment {
    private static final String DEBUG_TAG = "ComponentTypeFragment";
    ListView listView;
    LinearLayout progressBar;
    List<ComponentType> list;
    List<TypesToComponent> ids;
    Set<ComponentType> set;
    ComponentTypeListAdapter adapter;
    Bundle bundle;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

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
        set = new HashSet<>();
        adapter = new ComponentTypeListAdapter(getContext(), R.layout.list_row, list, false);

        if (bundle != null) {
            if (bundle.containsKey("selectable")) {
                adapter = new ComponentTypeListAdapter(getContext(), R.layout.list_row, list, true);
                adapter.setSelected(set);
            }
        }
        new Task().execute();
        adapter.notifyDataSetChanged();
        return rootView;
    }


    @Override
    public void onRefresh() {

        new Task().execute();
    }

    @Override
    public void refresh() {
        Log.e("refresh", "");
        new Task().execute();
    }

    @Override
    public void add() {
        startActivity(new Intent(getContext(), AddOrEditComponentType.class));
    }

    public LinkedList<ComponentType> getSelected() {
        return new LinkedList<>(adapter.getSelected());
    }

    @SuppressLint("StaticFieldLeak")
    private class Task extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(false);
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            list.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            if (bundle == null || !bundle.containsKey("types"))
                list.addAll(Utils.getAllComponentTypes());
            adapter.notifyDataSetChanged();
            if (list.isEmpty()) {
                Toast.makeText(getContext(), "No results", Toast.LENGTH_LONG).show();
                Log.e(DEBUG_TAG, "no results");
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (Utils.IS_ONLINE) {
                Service service = new ComponentTypeService(getContext());
                service.getAll();
            }
            ids = new LinkedList<>();
            if (bundle != null) {
                if (bundle.containsKey("types"))
                    ids = SugarRecord.find(TypesToComponent.class, "component_id=?", String.valueOf(bundle.getLong("types")));
                if (!ids.isEmpty()) {
                    for (TypesToComponent t : ids) {
                        list.add(SugarRecord.findById(ComponentType.class, t.getTypeId()));
                    }
                }
                if (bundle.containsKey("selected"))
                    ids = SugarRecord.find(TypesToComponent.class, "component_id=?", String.valueOf(bundle.getLong("selected")));
                if (!ids.isEmpty()) {
                    for (TypesToComponent t : ids) {
                        set.add(SugarRecord.findById(ComponentType.class, t.getTypeId()));
                    }
                }
            }

            return null;
        }
    }
}
