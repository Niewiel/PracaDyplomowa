package pl.niewiel.pracadyplomowa.fragments;

import android.annotation.SuppressLint;
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

import java.util.LinkedList;
import java.util.List;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.activity.add_edit.AddOrEditBuilding;
import pl.niewiel.pracadyplomowa.adapters.BuildingListAdapter;
import pl.niewiel.pracadyplomowa.database.model.Building;
import pl.niewiel.pracadyplomowa.database.service.BuildingService;
import pl.niewiel.pracadyplomowa.database.service.Service;
import pl.niewiel.pracadyplomowa.database.service.Synchronize;

public class BuildingListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MyFragment {
    private static final String DEBUG_TAG = "BuildingListFragment";
    ListView listView;
    LinearLayout progressBar;
    List<Building> list;
    BuildingListAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        listView = rootView.findViewById(R.id.list);
        progressBar = rootView.findViewById(R.id.progressbar_view);
        list = new LinkedList<>();
        adapter = new BuildingListAdapter(getContext(), R.layout.list_row, list, false);
        listView.setAdapter(adapter);
        new Task().execute();

        return rootView;
    }


    @Override
    public void onRefresh() {
        Log.e("DUPA", "DUPA");
        new Synchronize(getContext()).execute();
        new Task().execute();

    }

    @Override
    public void refresh() {
        new Task().execute();
    }

    @Override
    public void add() {
        startActivity(new Intent(getContext(), AddOrEditBuilding.class));
    }

    @SuppressLint("StaticFieldLeak")
    private class Task extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            list.clear();
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            list.addAll(Utils.getAllBuildings());
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
                Service service = new BuildingService(getContext());
                service.getAll();
            }
            return null;
        }
    }
}
