package pl.niewiel.pracadyplomowa.fragments.token;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import pl.niewiel.pracadyplomowa.database.model.Token;

public class TokenView extends Fragment {

    ListView listView;
    LinearLayout progressBar;
    TokenAdapter adapter;
    List<Token> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container, false);

        progressBar = rootView.findViewById(R.id.progressbar_view);
        listView = rootView.findViewById(R.id.list);
//        progressBar.setVisibility(View.GONE);
//        listView.setVisibility(View.VISIBLE);
        list = new LinkedList<>();


        adapter = new TokenAdapter(getContext(), R.layout.token_row, list);
        new Task().execute();
        list.addAll(Utils.getAllTokens());
        if (!list.isEmpty()) {
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), "No results", Toast.LENGTH_LONG).show();
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    private class Task extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Log.e("list", String.valueOf(list));
            Log.e("do", "in background");
            return null;
        }
    }


}
