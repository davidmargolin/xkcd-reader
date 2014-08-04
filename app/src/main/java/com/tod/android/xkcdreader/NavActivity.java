package com.tod.android.xkcdreader;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

import java.util.Calendar;

/**
 * Created by Margolin on 7/9/2014.
 */
public class NavActivity extends Activity
    implements NavigationDrawerFragment.NavigationDrawerCallbacks {
     static long back_pressed;
    FragmentManager fragmentManager;
    static final String BROADCAST = "com.tod.android.xkcdreader.android.action.broadcast";
    static Boolean hidemenu=true;
        /**
         * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
         */
        private NavigationDrawerFragment mNavigationDrawerFragment;
        private static CharSequence mTitle;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String theme = sharedPreferences.getString("themepref", "LIGHT");
            Boolean notifs = sharedPreferences.getBoolean("notifs", true);
            if (theme.contains("DARK")){
                setTheme(R.style.DarkTheme);
            }else{
                setTheme(R.style.AppTheme);
            }
            super.onCreate(savedInstanceState);
            fragmentManager = getFragmentManager();
            setContentView(R.layout.activity_nav);
            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getFragmentManager().findFragmentById(R.id.navigation_drawer);
            mTitle = getTitle();
            // Set up the drawer.
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
            if (notifs) {
                boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
                        new Intent(BROADCAST),
                        PendingIntent.FLAG_NO_CREATE) != null);

                if (alarmUp){
                    Log.d("Alarm Info", "Alarm is already active");
                }else {
                    Intent intent = new Intent(BROADCAST);
                    PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent operation1 = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    PendingIntent operation2 = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alrm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                    AlarmManager alrm1 = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                    AlarmManager alrm2 = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                    Calendar today = Calendar.getInstance();
                    today.set(Calendar.HOUR_OF_DAY,24);
                    Calendar moncalendar = Calendar.getInstance();
                    moncalendar.add(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    moncalendar.set(Calendar.HOUR_OF_DAY, 12);
                    moncalendar.set(Calendar.MINUTE, 0);
                    moncalendar.set(Calendar.SECOND,0);
                    moncalendar.set(Calendar.MILLISECOND,0);
                    long firstTime = moncalendar.getTimeInMillis();
                    Log.e("", "Monday firstTime: " + firstTime);
                    Calendar wedcalendar = Calendar.getInstance();
                    wedcalendar.add(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    wedcalendar.set(Calendar.HOUR_OF_DAY, 12);
                    wedcalendar.set(Calendar.MINUTE, 0);
                    wedcalendar.set(Calendar.SECOND,0);
                    wedcalendar.set(Calendar.MILLISECOND,0);
                    long firstTimewed = wedcalendar.getTimeInMillis();
                    Log.e("", "Wednesday firstTime: " + firstTimewed);
                    Calendar fricalendar = Calendar.getInstance();
                    fricalendar.add(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    fricalendar.set(Calendar.HOUR_OF_DAY, 12);
                    fricalendar.set(Calendar.MINUTE, 0);
                    fricalendar.set(Calendar.SECOND,0);
                    fricalendar.set(Calendar.MILLISECOND,0);
                    long firstTimefri = fricalendar.getTimeInMillis();
                    Log.e("", "Friday firstTime: " + firstTimefri);
                    if (moncalendar.before(today)) {
                        moncalendar.add(Calendar.DATE, 7);
                        Log.d("timemon","added 7 days");
                    }
                    if (wedcalendar.before(today)) {
                        wedcalendar.add(Calendar.DATE, 7);
                        Log.d("timewed","added 7 days");

                    }
                    if (fricalendar.before(today)) {
                        fricalendar.add(Calendar.DATE, 7);
                        Log.d("timefri","added 7 days");
                    }
                    alrm.setRepeating(AlarmManager.RTC_WAKEUP, moncalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, operation);
                    alrm1.setRepeating(AlarmManager.RTC_WAKEUP, wedcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, operation1);
                    alrm2.setRepeating(AlarmManager.RTC_WAKEUP, fricalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, operation2);
                    Log.d("alarm status","started");
                }
                }
            }
        @Override
        public void onNavigationDrawerItemSelected(int position) {
            // update the main content by replacing fragments
            switch (position) {
                case 0:
                    mTitle = getString(R.string.title_section1);
                    // Creating a fragment object
                    ReaderFragment aFragment = new ReaderFragment();
                    Bundle data = new Bundle();
                    // Setting the index of the currently selected item of mDrawerList
                    data.putString("LINK", "http://xkcd.com");
                    // Setting the position to the fragment
                    aFragment.setArguments(data);
                    // Creating a fragment transaction
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        fragmentManager.popBackStack();
                    }
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    // Adding a fragment to the fragment transaction
                    ft.replace(R.id.container, aFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    // Committing the transaction
                    ft.commit();
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        fragmentManager.popBackStack();
                    }
                    break;
                case 1:
                    mTitle = getString(R.string.title_section2);
                    BlagLoaderFragment bFragment = new BlagLoaderFragment();
                    // Creating a fragment transaction
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        fragmentManager.popBackStack();
                    }
                    ft = fragmentManager.beginTransaction();
                    // Getting reference to the FragmentManager
                    // Adding a fragment to the fragment transaction
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    // Adding a fragment to the fragment transaction
                    ft.replace(R.id.container, bFragment);
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        fragmentManager.popBackStack();
                    }
                    // Committing the transaction
                    ft.commit();
                    break;
                case 2:
                    mTitle = getString(R.string.title_section3);
                    WhatIfLoaderFragment cFragment = new WhatIfLoaderFragment();
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        fragmentManager.popBackStack();
                    }
                    // Creating a fragment transaction
                    ft = fragmentManager.beginTransaction();
                    // Getting reference to the FragmentManager
                    // Adding a fragment to the fragment transaction
                    ft.replace(R.id.container, cFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    // Committing the transaction
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        fragmentManager.popBackStack();
                    }
                    ft.commit();
                    break;
                case 3:
                    mTitle = getString(R.string.title_section4);
                    SettingsFragment dFragment = new SettingsFragment();
                    // Getting reference to the FragmentManager
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        fragmentManager.popBackStack();
                    }
                    // Creating a fragment transaction
                    ft = fragmentManager.beginTransaction();
                    // Adding a fragment to the fragment transaction
                    ft.replace(R.id.container, dFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    // Committing the transaction
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        fragmentManager.popBackStack();
                    }
                    ft.commit();

                    break;
            }

        }

    void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);

                break;
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);  // Add this method.
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            if (mTitle== getString(R.string.title_section1)){
                getMenuInflater().inflate(R.menu.main,menu);

            }
            if (hidemenu){
                if (menu.hasVisibleItems()) {
                    menu.clear();
                }
            }
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
    public static void setlistfrag(Boolean enabled){
        hidemenu = enabled;
    }


    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
            NavActivity.setlistfrag(false);
            invalidateOptionsMenu();
        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
            else
                Toast.makeText(getBaseContext(), "Press back again to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
            // or just go back to main activity
        }
    }
}

