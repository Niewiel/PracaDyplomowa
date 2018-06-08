package pl.niewiel.pracadyplomowa.activity;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

import java.util.List;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.Utils;
import pl.niewiel.pracadyplomowa.database.model.User;
import pl.niewiel.pracadyplomowa.database.service.Synchronize;
import pl.niewiel.pracadyplomowa.fragments.Main;
import pl.niewiel.pracadyplomowa.fragments.MyFragment;
import pl.niewiel.pracadyplomowa.fragments.lists.BuildListView;
import pl.niewiel.pracadyplomowa.fragments.lists.BuildingListView;
import pl.niewiel.pracadyplomowa.fragments.lists.ComponentListView;
import pl.niewiel.pracadyplomowa.fragments.lists.ComponentTypeListView;
import pl.niewiel.pracadyplomowa.fragments.lists.token.TokenView;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String DEBUG_TAG = "MainActivity";
    private static final int STORAGE = 2;
    private static final int STORAGE_R = 3;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static boolean ok = false;
    private User user;
    private DrawerLayout mDrawerLayout;
    private MyFragment currentFragment;


    @Override
    protected void onPostResume() {
        super.onPostResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            extras.clear();
        currentFragment();

        //splash
        if (!ok) {
            ok = true;
            startActivity(new Intent(getApplicationContext(), Splash.class));
        }
        if (currentFragment != null)
            currentFragment.refresh();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
//        super.onAttachFragment(fragment);
        if (currentFragment == fragment)
            currentFragment.refresh();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case STORAGE_R:// If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void permissionsCheck() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_R);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionsCheck();
        //splash
        if (!ok) {
            ok = true;
            startActivity(new Intent(getApplicationContext(), Splash.class));
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
                        currentFragment.refresh();


                    default:
                        // If we got here, the user's action was not recognized.
                        // Invoke the superclass to handle it.
                        return false;

                }
            }
        });

//        setSupportActionBar(myToolbar);


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
                                Fragment types = new ComponentTypeListView();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, types, "TYPES").commit();
                                break;
                            case R.id.menu_components_list:
                                Log.e("Menu", String.valueOf(menuItem.getTitle()));
                                Fragment components = new ComponentListView();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, components, "COMPONENTS").commit();
                                break;
                            case R.id.menu_builds_list:

                                Log.e("Menu", String.valueOf(menuItem.getTitle()));
                                Fragment builds = new BuildListView();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, builds, "BUILDS").commit();
                                break;
                            case R.id.menu_building_list:
                                Log.e("Menu", String.valueOf(menuItem.getTitle()));
                                Fragment buildings = new BuildingListView();
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

    private void currentFragment() {
        MyFragment fragment;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f :
                fragments) {
            if (f != null && f.isVisible())
                currentFragment = (MyFragment) f;

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
