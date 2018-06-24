package pl.niewiel.pracadyplomowa.fragments;

import android.annotation.SuppressLint;
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
import pl.niewiel.pracadyplomowa.adapters.BuildListAdapter;
import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.database.service.BuildService;
import pl.niewiel.pracadyplomowa.database.service.Service;

public class BuildListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MyFragment {

    private static final String DEBUG_TAG = "BuildListFragment";
    ListView listView;
    LinearLayout progressBar;
    List<Build> list;
    BuildListAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container, false);

        listView = rootView.findViewById(R.id.list);
        progressBar = rootView.findViewById(R.id.progressbar_view);
        list = new LinkedList<>();
        adapter = new BuildListAdapter(getContext(), R.layout.list_row, list);
        swipeRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
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
//        new Synchronize(getContext()).execute();
        new Task().execute();

    }

    @Override
    public void refresh() {
        new Task().execute();
    }

    @Override
    public void add() {

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
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            list.addAll(Utils.getAllBuild());
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Service service = new BuildService(getContext());
            service.getAll();
            return null;
        }
    }
}
