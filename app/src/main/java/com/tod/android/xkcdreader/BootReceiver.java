package com.tod.android.xkcdreader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Margolin on 7/15/2014.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent rebootintent = new Intent(context, Receiver.class);
            Intent rebootintent1 = new Intent(context, Receiver.class);
            Intent rebootintent2 = new Intent(context, Receiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 7339684, rebootintent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 7339684, rebootintent1, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 7339684, rebootintent2, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alrm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            AlarmManager alrm1 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            AlarmManager alrm2 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Calendar moncalendar = Calendar.getInstance();
            moncalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            moncalendar.set(Calendar.HOUR_OF_DAY, 1);
            moncalendar.set(Calendar.MINUTE, 1);
            Calendar wedcalendar = Calendar.getInstance();
            wedcalendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            wedcalendar.set(Calendar.HOUR_OF_DAY, 1);
            wedcalendar.set(Calendar.MINUTE, 1);
            Calendar fricalendar = Calendar.getInstance();
            fricalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            fricalendar.set(Calendar.HOUR_OF_DAY, 1);
            fricalendar.set(Calendar.MINUTE, 1);
            if (moncalendar.getTimeInMillis() < (System.currentTimeMillis())) {
                moncalendar.add(Calendar.DATE, 7);
            }
            if (wedcalendar.getTimeInMillis() < (System.currentTimeMillis())) {
                wedcalendar.add(Calendar.DATE, 7);
            }
            if (fricalendar.getTimeInMillis() < (System.currentTimeMillis())) {
                fricalendar.add(Calendar.DATE, 7);
            }
            alrm.setRepeating(AlarmManager.RTC_WAKEUP, moncalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            alrm1.setRepeating(AlarmManager.RTC_WAKEUP, wedcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent1);
            alrm2.setRepeating(AlarmManager.RTC_WAKEUP, fricalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent2);

        }
    }
}
