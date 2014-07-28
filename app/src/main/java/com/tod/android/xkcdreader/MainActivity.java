package com.tod.android.xkcdreader;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

import java.util.Calendar;


public class MainActivity extends Activity {
Boolean reader;
    private static long back_pressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean firsttime = sharedPreferences.getBoolean("firsttime", true);
        String theme = sharedPreferences.getString("themepref", "LIGHT");
        if (theme.contains("DARK")){
            setTheme(R.style.DarkTheme);
        }else{
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        reader = true;
        getActionBar().setTitle("xkcd");
            ReaderFragment rFragment = new ReaderFragment();
/*					// Creating a Bundle object
					Bundle data = new Bundle();

					// Setting the index of the currently selected item of mDrawerList
					data.putInt("position", posit
					// Setting the position to the fragment
					rFragment.setArguments(data);*/

            // Getting reference to the FragmentManager
            FragmentManager fragmentManager = getFragmentManager();

            // Creating a fragment transaction
            FragmentTransaction ft = fragmentManager.beginTransaction();

            // Adding a fragment to the fragment transaction
            ft.replace(R.id.container, rFragment);


            // Committing the transaction
            ft.commit();
    if (firsttime){


        Intent intent = new Intent(this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alrm = (AlarmManager)getSystemService(ALARM_SERVICE);
        AlarmManager alrm1 = (AlarmManager)getSystemService(ALARM_SERVICE);
        AlarmManager alrm2 = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar moncalendar = Calendar.getInstance();
        moncalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Calendar wedcalendar = Calendar.getInstance();
        wedcalendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        Calendar fricalendar = Calendar.getInstance();
        fricalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        if(moncalendar.getTimeInMillis() < (System.currentTimeMillis()+AlarmManager.INTERVAL_DAY)) {
            moncalendar.add(Calendar.DATE, 7);
        }if(wedcalendar.getTimeInMillis() < (System.currentTimeMillis()+AlarmManager.INTERVAL_DAY)) {
            wedcalendar.add(Calendar.DATE, 7);
        }if(fricalendar.getTimeInMillis() < (System.currentTimeMillis()+AlarmManager.INTERVAL_DAY)) {
            fricalendar.add(Calendar.DATE, 7);
        }
            alrm.setInexactRepeating(AlarmManager.RTC, moncalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY *7, pendingIntent);
            alrm1.setInexactRepeating(AlarmManager.RTC, wedcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY *7, pendingIntent);
            alrm2.setInexactRepeating(AlarmManager.RTC, fricalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY *7, pendingIntent);
        SharedPreferences.Editor   editor = sharedPreferences.edit();
        editor.putBoolean("firsttime", false).apply();
    }

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
    public void onBackPressed() {
        if (!reader) {
            reader = true;
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setHomeButtonEnabled(false);
            getActionBar().setTitle("xkcd");
            ReaderFragment rFragment = new ReaderFragment();
/*					// Creating a Bundle object
					Bundle data = new Bundle();

					// Setting the index of the currently selected item of mDrawerList
					data.putInt("position", posit
					// Setting the position to the fragment
					rFragment.setArguments(data);*/

            // Getting reference to the FragmentManager
            FragmentManager fragmentManager = getFragmentManager();

            // Creating a fragment transaction
            FragmentTransaction ft = fragmentManager.beginTransaction();

            // Adding a fragment to the fragment transaction
            ft.replace(R.id.container, rFragment);

            // Committing the transaction
            ft.commit();
        } else if (reader) {
            if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
            else Toast.makeText(getBaseContext(), "Press back again to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
            // or just go back to main activity
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.

        if (reader) {
            getMenuInflater().inflate(R.menu.global, menu);
            getMenuInflater().inflate(R.menu.main,menu);
        }
            return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

if (item.getItemId()== android.R.id.home) {
            //called when the up affordance/carat in actionbar is pressed

            onBackPressed();
        }
            return super.onOptionsItemSelected(item);
    }
}
