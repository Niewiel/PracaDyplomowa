package pl.niewiel.pracadyplomowa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.orm.SugarRecord;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pl.niewiel.pracadyplomowa.database.model.Build;
import pl.niewiel.pracadyplomowa.database.model.Building;
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;
import pl.niewiel.pracadyplomowa.database.model.Token;
import pl.niewiel.pracadyplomowa.services.OnlineChecker;
import pl.niewiel.pracadyplomowa.services.TokenService;

public class Utils {
    public static boolean IS_ONLINE = false;
    public static boolean IS_SYNCHRONIZED = false;
    private static Class<?> serviceMonitor = TokenService.class;
    private static Class<?> onlineChecker = OnlineChecker.class;


    public static Timestamp parseDate(String date) {
        Timestamp timestamp = null;
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(date);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return timestamp;
    }

    public static void fillDatabase() {
        Build build = new Build();
        build.setName("aaa");
        build.setBsId(1);
        build.setmId(SugarRecord.save(build));
        SugarRecord.save(build);
        build = new Build();
        build.setName("bbb");
        build.setmId(SugarRecord.save(build));
        SugarRecord.save(build);
        Log.e("new Build", String.valueOf(SugarRecord.listAll(Build.class)));
        ComponentType componentType;
        componentType = new ComponentType(1, "ściana", false);
        componentType.setmId(SugarRecord.save(componentType));
        SugarRecord.save(componentType);
        Component component = new Component();
        component.setName("ściana a");
        component.setmId(SugarRecord.save(component));
        SugarRecord.save(component);
        Building building = new Building();
        building.setName("Budowa A");
        building.setmId(SugarRecord.save(building));
        SugarRecord.save(building);
        IS_SYNCHRONIZED = false;
    }

    public static void dropBase(Context context) {
        SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(context);
        schemaGenerator.deleteTables(new SugarDb(context).getDB());
        SugarContext.init(context);
        schemaGenerator.createDatabase(new SugarDb(context).getDB());
        Toast.makeText(context, "database dropped", Toast.LENGTH_LONG).show();

    }

    public static void startTokenService(Context context) {
        context.startService(new Intent(context, serviceMonitor));
    }

    public static void stopTokenService(Context context) {
        context.stopService(new Intent(context, serviceMonitor));
    }

    public static void startOnlineCheckerService(Context context) {
        context.startService(new Intent(context, onlineChecker));
    }

    public static void stopOnlineCheckerService(Context context) {
        context.stopService(new Intent(context, onlineChecker));
    }


    public static List<ComponentType> getcomponentTypes(long id) {
        return SugarRecord.find(ComponentType.class, "mid=?", String.valueOf(id));
    }

    public static List<ComponentType> getAllComponentTypes() {
        return SugarRecord.listAll(ComponentType.class);
    }

    public static List<Component> getComponents(long id) {
        return SugarRecord.find(Component.class, "mid=?", String.valueOf(id));
    }

    public static List<Component> getAllComponents() {
        return SugarRecord.listAll(Component.class);
    }

    public static List<Building> getBuildings(long id) {
        return SugarRecord.find(Building.class, "mid=?", String.valueOf(id));
    }

    public static List<Building> getAllBuildings() {
        return SugarRecord.listAll(Building.class);
    }

    public static List<Build> getBuild(long id) {
        return SugarRecord.find(Build.class, "mid=?", String.valueOf(id));
    }

    public static List<Build> getAllBuild() {
        return SugarRecord.listAll(Build.class);
    }

    public static List<Token> getAllTokens() {
        return SugarRecord.listAll(Token.class);
    }

}
