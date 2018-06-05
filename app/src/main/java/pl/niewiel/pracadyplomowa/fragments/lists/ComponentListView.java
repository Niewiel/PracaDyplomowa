package pl.niewiel.pracadyplomowa.fragments.lists;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.adapters.ComponentListAdapter;
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.database.service.ComponentService;
import pl.niewiel.pracadyplomowa.database.service.Service;
import pl.niewiel.pracadyplomowa.database.service.Synchronize;

public class ComponentListView extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String DEBUG_TAG = "ComponentListView";
    ListView listView;
    LinearLayout progressBar;
    List<Component> list;
    ComponentListAdapter adapter;
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
        adapter = new ComponentListAdapter(getContext(), R.layout.list_row, list);

        listView.setAdapter(adapter);
        new Task().execute();
        if (list.isEmpty()) {
            Toast.makeText(getContext(), "No results", Toast.LENGTH_LONG).show();
            Log.e(DEBUG_TAG, "no results");
        }
        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onRefresh() {
        new Synchronize(getContext()).execute();
        new Task().execute();
        swipeRefreshLayout.setRefreshing(false);
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
            list.addAll(Utils.getAllComponents());
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (Utils.IS_ONLINE) {
                Service service = new ComponentService(getContext());
                service.getAll();
            }
            return null;
        }
    }
}
