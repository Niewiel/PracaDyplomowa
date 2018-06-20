package pl.niewiel.pracadyplomowa.activity;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import pl.niewiel.pracadyplomowa.database.service.Synchronize;
import pl.niewiel.pracadyplomowa.fragments.BuildListFragment;
import pl.niewiel.pracadyplomowa.fragments.BuildingListFragment;
import pl.niewiel.pracadyplomowa.fragments.ComponentListFragment;
import pl.niewiel.pracadyplomowa.fragments.ComponentTypeListFragment;
import pl.niewiel.pracadyplomowa.fragments.Main;
import pl.niewiel.pracadyplomowa.fragments.MyFragment;
import pl.niewiel.pracadyplomowa.fragments.token.TokenView;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String DEBUG_TAG = "MainActivity";
    private static final int STORAGE = 2;
    private static final int STORAGE_R = 3;
    private static boolean ok = false;
    private User user;
    private DrawerLayout mDrawerLayout;
    private MyFragment currentFragment;


    @Override
    protected void onPostResume() {
        super.onPostResume();
        //splash
        if (!ok) {
            ok = true;
            startActivity(new Intent(getApplicationContext(), Splash.class));
        }

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        currentFragment = (MyFragment) fragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length <= 0
                || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            finish();
        }
    }

//    private void permissionsCheck() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.CAMERA)) {
//                int MY_PERMISSIONS_REQUEST_CAMERA = 1;
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.CAMERA},
//                        MY_PERMISSIONS_REQUEST_CAMERA);
//            }
//        }
//
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        STORAGE);
//            }
//        }
//
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        STORAGE_R);
//            }
//        }
//    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        permissionsCheck();
        //splash
        if (!ok) {
            ok = true;
            startActivity(new Intent(getApplicationContext(), Splash.class));
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }


        //topbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.app_name);
        myToolbar.inflateMenu(R.menu.topbar_menu);
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.synchronize:
                        new Synchronize(getApplicationContext()).execute();
                        Log.e(DEBUG_TAG, "synchronizacja");
                        currentFragment.refresh();

                        return true;
                    case R.id.add_item:
                        Log.e("Add", currentFragment.toString());
                        currentFragment.add();


                    default:
                        // If we got here, the user's action was not recognized.
                        // Invoke the superclass to handle it.
                        return false;

                }
            }
        });


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
                    .add(R.id.fragment_container, firstFragment, "FIRST").commit();
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
                                Fragment types = new ComponentTypeListFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, types, "TYPES").commit();
                                currentFragment.refresh();
                                break;
                            case R.id.menu_components_list:
                                Log.e("Menu", String.valueOf(menuItem.getTitle()));
                                Fragment components = new ComponentListFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, components, "COMPONENTS").commit();
                                currentFragment.refresh();

                                break;
                            case R.id.menu_builds_list:

                                Log.e("Menu", String.valueOf(menuItem.getTitle()));
                                Fragment builds = new BuildListFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, builds, "BUILDS").commit();
                                break;
                            case R.id.menu_building_list:
                                Log.e("Menu", String.valueOf(menuItem.getTitle()));
                                Fragment buildings = new BuildingListFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, buildings, "BUILDINGS").commit();
                                break;
                            case R.id.menu_tokens_list:
                                Log.e("Menu", String.valueOf(menuItem.getTitle()));
                                Fragment tokens = new TokenView();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, tokens, "TOKENS").commit();
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
