package com.tod.android.xkcdreader;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * Created by Margolin on 7/9/2014.
 */
public class SettingsFragment extends PreferenceFragment {
    private SharedPreferences sharedPreferences;
    boolean alarmUp;
    public static final String BROADCAST = "com.tod.android.xkcdreader.android.action.broadcast";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
        NavActivity.setlistfrag(true);
        getActivity().invalidateOptionsMenu();
        CheckBoxPreference notifications = (CheckBoxPreference) findPreference("notifs");
        Preference prefereces=findPreference("about");
        final Preference themeswitcheroo=findPreference("themepref");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        themeswitcheroo.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                getActivity().recreate();
                return true;
            }
        });
        prefereces.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.about_dialog);
                dialog.setTitle("About");
                dialog.show();
                return true;
            }


        });
        notifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = Boolean.valueOf(newValue.toString());
                ComponentName receiver = new ComponentName(getActivity(), BootReceiver.class);
                ComponentName notifreceiver = new ComponentName(getActivity(), Receiver.class);
                PackageManager pm = getActivity().getPackageManager();
                AlarmManager alrm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                AlarmManager alrm1 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                AlarmManager alrm2 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                alarmUp = (PendingIntent.getBroadcast(getActivity(), 0,
                        new Intent(BROADCAST),
                        PendingIntent.FLAG_NO_CREATE) != null);
                Intent intent = new Intent(BROADCAST);
                PendingIntent operation = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                PendingIntent operation1 = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                PendingIntent operation2 = PendingIntent.getBroadcast(getActivity(), 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);



                if (checked) {


                    if (!alarmUp) {
                        Calendar today = Calendar.getInstance();
                        today.setTimeInMillis(System.currentTimeMillis());
                        Calendar moncalendar = Calendar.getInstance();
                        moncalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        moncalendar.set(Calendar.HOUR_OF_DAY, 12);
                        moncalendar.set(Calendar.MINUTE, 0);
                        moncalendar.set(Calendar.SECOND,0);
                        moncalendar.set(Calendar.MILLISECOND,0);

                        Calendar wedcalendar = Calendar.getInstance();
                        wedcalendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                        wedcalendar.set(Calendar.HOUR_OF_DAY, 12);
                        wedcalendar.set(Calendar.MINUTE, 0);
                        wedcalendar.set(Calendar.SECOND,0);
                        wedcalendar.set(Calendar.MILLISECOND,0);

                        Calendar fricalendar = Calendar.getInstance();
                        fricalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                        fricalendar.set(Calendar.HOUR_OF_DAY, 12);
                        fricalendar.set(Calendar.MINUTE, 0);
                        fricalendar.set(Calendar.SECOND,0);
                        fricalendar.set(Calendar.MILLISECOND,0);

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
                        long firstTime = moncalendar.getTimeInMillis();
                        Log.d("", "Monday firstTime: " + firstTime);
                        long firstTimewed = wedcalendar.getTimeInMillis();
                        Log.d("", "Wednesday firstTime: " + firstTimewed);
                        long firstTimefri = fricalendar.getTimeInMillis();
                        Log.d("", "Friday firstTime: " + firstTimefri);
                        alrm.setRepeating(AlarmManager.RTC_WAKEUP, moncalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, operation);
                        alrm1.setRepeating(AlarmManager.RTC_WAKEUP, wedcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, operation1);
                        alrm2.setRepeating(AlarmManager.RTC_WAKEUP, fricalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, operation2);
                        Log.d("alarm status","started");
                    }
                    editor.putBoolean("notifs", true);
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    pm.setComponentEnabledSetting(notifreceiver,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                }else{
                    if (alarmUp){
                        PendingIntent.getBroadcast(getActivity(), 0,
                                new Intent(BROADCAST),PendingIntent.FLAG_NO_CREATE).cancel();
                        PendingIntent.getBroadcast(getActivity(), 1,
                                new Intent(BROADCAST),PendingIntent.FLAG_NO_CREATE).cancel();
                        PendingIntent.getBroadcast(getActivity(), 2,
                                new Intent(BROADCAST),PendingIntent.FLAG_NO_CREATE).cancel();
                        alrm.cancel(operation);
                        alrm1.cancel(operation1);
                        alrm2.cancel(operation2);
                        Log.d("alarm status", "stopped");
                    }
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    pm.setComponentEnabledSetting(notifreceiver,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    editor.putBoolean("notifs", false);

                }
                editor.apply();
                return true;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

}