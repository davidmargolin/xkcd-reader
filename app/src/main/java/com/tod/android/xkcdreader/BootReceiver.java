package com.tod.android.xkcdreader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Margolin on 7/15/2014.
 */
public class BootReceiver extends BroadcastReceiver {
    public static final String BROADCAST = "com.tod.android.xkcdreader.android.action.broadcast";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                    new Intent(BROADCAST),
                    PendingIntent.FLAG_NO_CREATE) != null);
            if (!alarmUp) {
                IntentFilter intentFilter = new IntentFilter(BROADCAST);
                Receiver myreceiver = new Receiver();
                context.registerReceiver(myreceiver , intentFilter);
                Intent bintent = new Intent(BROADCAST);
                PendingIntent operation = PendingIntent.getBroadcast(context, 0, bintent, 0);
                AlarmManager alrm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                AlarmManager alrm1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                AlarmManager alrm2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY,24);
                Calendar moncalendar = Calendar.getInstance();
                moncalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                Calendar wedcalendar = Calendar.getInstance();
                wedcalendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                Calendar fricalendar = Calendar.getInstance();
                fricalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                if (moncalendar.getTimeInMillis() < today.getTimeInMillis()) {
                    moncalendar.add(Calendar.DATE, 7);
                    Log.d("timemon", "added 7 days");
                }
                if (wedcalendar.getTimeInMillis() < today.getTimeInMillis()) {
                    wedcalendar.add(Calendar.DATE, 7);
                    Log.d("timewed","added 7 days");
                }
                if (fricalendar.getTimeInMillis() < today.getTimeInMillis()) {
                    fricalendar.add(Calendar.DATE, 7);
                    Log.d("timefri","added 7 days");
                }
                alrm.setRepeating(AlarmManager.RTC_WAKEUP, moncalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, operation);
                alrm1.setRepeating(AlarmManager.RTC_WAKEUP, wedcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, operation);
                alrm2.setRepeating(AlarmManager.RTC_WAKEUP, fricalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, operation);
                Log.d("alarm status", "started");
            }
        }
    }
}
