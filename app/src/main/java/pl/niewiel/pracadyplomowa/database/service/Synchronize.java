package pl.niewiel.pracadyplomowa.database.service;

import android.content.Context;
import android.os.AsyncTask;

import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.database.model.Building;
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;

public class Synchronize extends AsyncTask<Void, Void, Void> {
    private Service<ComponentType> s1;
    private Service<Component> s2;
    private Service<Build> s3;
    private Service<Building> s4;

    public Synchronize(Context context) {
        s1 = new ComponentTypeService(context);
        s2 = new ComponentService(context);
        s3 = new BuildService(context);
        s4 = new BuildingService(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        s1.synchronize();
        s2.synchronize();
        s3.synchronize();
        s4.synchronize();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Utils.IS_SYNCHRONIZED = true;
    }
}
