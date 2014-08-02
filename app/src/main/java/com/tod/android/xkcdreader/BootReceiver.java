package com.tod.android.xkcdreader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
                Intent bintent = new Intent(BROADCAST);
                PendingIntent operation = PendingIntent.getBroadcast(context, 0, bintent, PendingIntent.FLAG_UPDATE_CURRENT);
                PendingIntent operation1 = PendingIntent.getBroadcast(context, 1, bintent, PendingIntent.FLAG_UPDATE_CURRENT);
                PendingIntent operation2 = PendingIntent.getBroadcast(context, 2, bintent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alrm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                AlarmManager alrm1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                AlarmManager alrm2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
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
                Log.d("alarm status", "started");
            }
        }
    }
}
