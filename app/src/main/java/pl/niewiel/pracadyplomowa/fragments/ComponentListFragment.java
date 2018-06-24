package pl.niewiel.pracadyplomowa.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import pl.niewiel.pracadyplomowa.activity.add_edit.AddOrEditComponent;
import pl.niewiel.pracadyplomowa.adapters.ComponentListAdapter;
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.database.model.ComponentToBuilding;
import pl.niewiel.pracadyplomowa.database.service.ComponentService;
import pl.niewiel.pracadyplomowa.database.service.Service;
import pl.niewiel.pracadyplomowa.database.service.Synchronize;

public class ComponentListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MyFragment {
    private static final String DEBUG_TAG = "ComponentListFragment";
    ListView listView;
    LinearLayout progressBar;
    Service<Component> service;
    List<Component> list;
    List<ComponentToBuilding> ids;
    ComponentListAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Bundle bundle;

    @Override
    public void onResume() {
        new Task().execute();
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        listView = rootView.findViewById(R.id.list);
        progressBar = rootView.findViewById(R.id.progressbar_view);
        list = new LinkedList<>();
        adapter = new ComponentListAdapter(getContext(), R.layout.list_row, list, false);
        bundle = this.getArguments();

        listView.setAdapter(adapter);
        new Task().execute();
        return rootView;
    }


    @Override
    public void onRefresh() {
        new Synchronize(getContext()).execute();
        new Task().execute();
    }

    @Override
    public void refresh() {
        new Task().execute();
    }

    @Override
    public void add() {
        startActivity(new Intent(getContext(), AddOrEditComponent.class));

    }

    @SuppressLint("StaticFieldLeak")
    private class Task extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            list.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (bundle == null)
                list.addAll(Utils.getAllComponents());
            adapter.notifyDataSetChanged();

            if (list.isEmpty()) {
                Toast.makeText(getContext(), "No results", Toast.LENGTH_LONG).show();
                Log.e(DEBUG_TAG, "no results");
            }
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (Utils.IS_ONLINE) {
                service = new ComponentService(getContext());
                service.getAll();
                if (bundle != null) {
                    ids = new LinkedList<>();
                    if (bundle.getLong("components") != 0)
                        ids = SugarRecord.find(ComponentToBuilding.class, "building_id=?", String.valueOf(bundle.getLong("components")));
                    if (!ids.isEmpty())
                        for (ComponentToBuilding t : ids) {
                            list.add(SugarRecord.findById(Component.class, t.getComponentId()));
                        }
                }
            }
            return null;
        }
    }
}
