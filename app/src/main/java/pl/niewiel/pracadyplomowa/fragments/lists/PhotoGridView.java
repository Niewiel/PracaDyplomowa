package pl.niewiel.pracadyplomowa.fragments.lists;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.adapters.FileListAdapter;
import pl.niewiel.pracadyplomowa.database.model.Photo;
import pl.niewiel.pracadyplomowa.database.model.PhotoToComponent;

public class PhotoGridView extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String DEBUG_TAG = "File adapter";
    GridView listView;
    ArrayList<Photo> list;
    ArrayList<PhotoToComponent> ids;
    FileListAdapter adapter;
    Bundle bundle;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onRefresh() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        bundle = this.getArguments();
        list = new ArrayList<>();
        adapter = new FileListAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        new Task().execute();
        if (list.isEmpty()) {
            Toast.makeText(getContext(), "No results", Toast.LENGTH_LONG).show();
            Log.e(DEBUG_TAG, "no results");
        }

        return rootView;
    }

    class Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
