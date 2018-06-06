package pl.niewiel.pracadyplomowa.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.orm.SugarRecord;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.database.model.User;
import pl.niewiel.pracadyplomowa.fragments.Main;
import pl.niewiel.pracadyplomowa.fragments.lists.BuildListView;
import pl.niewiel.pracadyplomowa.fragments.lists.BuildingListView;
import pl.niewiel.pracadyplomowa.fragments.lists.ComponentListView;
import pl.niewiel.pracadyplomowa.fragments.lists.ComponentTypeListView;
import pl.niewiel.pracadyplomowa.fragments.lists.token.TokenView;


public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "MainActivity";
    private static boolean ok = false;
    private User user;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onPostResume() {
        super.onPostResume();
        //splash
        if (!ok) {
            ok = true;
            startActivity(new Intent(getApplicationContext(), Splash.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //splash
        if (!ok) {
            ok = true;
            startActivity(new Intent(getApplicationContext(), Splash.class));
        }

        //topbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        myToolbar.setTitle(R.string.app_name);
        myToolbar.inflateMenu(R.menu.topbar_menu);

        setSupportActionBar(myToolbar);


        //menu
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        FrameLayout fragment_container = findViewById(R.id.fragment_container);
        if (fragment_container != null) {
            if (savedInstanceState != null) {
                return;
            }
            Fragment firstFragment = new Main();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
//        //check if user is logged
//        final MenuItem isLogged = findViewById(R.id.login);
//        if (isLoggedIn())
//            isLogged.setTitle(R.string.action_logout);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped

                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.menu_component_types_list:
                                Log.e("Menu", String.valueOf(menuItem.getTitle()));
                                Fragment types = new ComponentTypeListView();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, types).commit();
                                break;
                            case R.id.menu_components_list:
                                Log.e("Menu", String.valueOf(menuItem.getTitle()));
                                Fragment components = new ComponentListView();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, components).commit();
                                break;
                            case R.id.menu_builds_list:
                                Log.e("Menu", String.valueOf(menuItem.getTitle()));
                                Fragment builds = new BuildListView();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, builds).commit();
                                break;
                            case R.id.menu_building_list:
                                Log.e("Menu", String.valueOf(menuItem.getTitle()));
                                Fragment buildings = new BuildingListView();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, buildings).commit();
                                break;
                            case R.id.menu_tokens_list:
                                Log.e("Menu", String.valueOf(menuItem.getTitle()));
                                Fragment tokens = new TokenView();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, tokens).commit();
                                break;
                            case R.id.menu_login:
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.synchronize:
                Log.e(DEBUG_TAG, "synchronizacja");

                return true;
            case R.id.add_item:


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private boolean isLoggedIn() {
        try {
            user = SugarRecord.find(User.class, "is_logged_in=?", "1").get(0);
            Toast.makeText(getApplicationContext(), "hello " + user.getName(), Toast.LENGTH_LONG).show();
            return true;
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return false;
        }
    }


    private void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do You really want to quit?")
                .setTitle("Exit?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Utils.stopTokenService(getApplicationContext());
                Utils.stopOnlineCheckerService(getApplicationContext());
                finish();
                System.exit(0);

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
