package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

/**
 *
 *
 * @autor Brayan González
 * @since 24/08/2016.
 */


import android.content.res.Configuration;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.StrictMode;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;



import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.services.EventService;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;



public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private EventService eventService;

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    private static int FRAGMENT = 0;


    public MainActivity(){
        this.eventService = EventService.getInstance(MainActivity.this);
        Runnable myRunnable = new Runnable() {
            public void run() {
                synchronizeEvents();
            }
        };
        Thread thread = new Thread(myRunnable);
        thread.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(policy);


        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            Toast.makeText(this, "Large screen", Toast.LENGTH_LONG).show();
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            Toast.makeText(this, "Normal sized screen", Toast.LENGTH_LONG).show();
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            Toast.makeText(this, "Small sized screen", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Screen size is neither large, normal or small", Toast.LENGTH_LONG).show();
        }


        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        new Thread(new Runnable() {// Thread to refresh the home fragment
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                    }
                    //Log.e(TAG,"Has Windows Focus"+ hasWindowFocus());
                    if(hasWindowFocus()) {
                        try {
                            displayView(FRAGMENT);
                        } catch (NullPointerException e) {
                            Log.i(TAG, "Refresh fragment not completed");
                        }
                    }
                }
            }
        }).start();

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor(getApplication(),R.color.colorPrimaryDark));
            }
            public void onFinish() {
                displayView(0);
                pDialog.cancel();
            }
        }.start();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


    }

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_search){
            //Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                FRAGMENT = 0;
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new WeeklyCalendarFragment();
                title = getString(R.string.title_calendar);
                FRAGMENT = 1;
                break;
            case 2:
                fragment = new DeviceSettingFragment();
                title = getString(R.string.title_device_settings);
                FRAGMENT = 3;
                break;
            default:
                break;
        }

        if (fragment != null) {
            try {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container_body, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }catch (IllegalStateException e){
                displayView(position);
            }

            //getSupportActionBar().setTitle(title);
            //getActionBar().setTitle(title);


        }


    }

    public static boolean hasOpenedDialogs(Fragment activity) {
        List<Fragment> fragments = activity.getFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof DialogFragment) {
                    Log.e(TAG,"Hay fragments");

                    return true;
                }
            }
        }
        Log.e(TAG,"No hay fragments");
        return false;
    }



    /**
     * Method to be updating the events
     */
    private void  synchronizeEvents(){
        //noinspection InfiniteLoopStatement
        for(;;) {
            try {
                eventService.updateEvents();
                Thread.sleep(20000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }catch (NullPointerException e) {
                Log.e(TAG, "Internet connection refused. Events were not updated");
            }catch (IllegalStateException e){
                Log.e(TAG,"IllegalStateException: Content has been consumed");
            }
        }
    }
}