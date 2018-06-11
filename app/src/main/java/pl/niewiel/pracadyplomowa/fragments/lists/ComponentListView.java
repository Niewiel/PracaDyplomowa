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
import com.orm.SugarRecord;
import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.adapters.ComponentListAdapter;
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.database.model.ComponentToBuilding;
import pl.niewiel.pracadyplomowa.database.service.ComponentService;
import pl.niewiel.pracadyplomowa.database.service.Service;
import pl.niewiel.pracadyplomowa.database.service.Synchronize;
import pl.niewiel.pracadyplomowa.fragments.MyFragment;

import java.util.LinkedList;
import java.util.List;

public class ComponentListView extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MyFragment {
    private static final String DEBUG_TAG = "ComponentListView";
    ListView listView;
    LinearLayout progressBar;
    List<Component> list;
    List<ComponentToBuilding> ids;
    ComponentListAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Bundle bundle;

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
        bundle = this.getArguments();

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

    }

    @Override
    public void refresh() {
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
                list.addAll(Utils.getAllComponents());
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (Utils.IS_ONLINE) {
                Service service = new ComponentService(getContext());
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
