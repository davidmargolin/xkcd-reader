package com.tod.android.xkcdreader;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
        Preference themeswitcheroo=findPreference("themepref");
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



                if (checked) {
                    alarmUp = (PendingIntent.getBroadcast(getActivity(), 0,
                            new Intent(BROADCAST),
                            PendingIntent.FLAG_NO_CREATE) != null);
                    if (!alarmUp) {
                        IntentFilter intentFilter = new IntentFilter(BROADCAST);
                        Receiver myreceiver = new Receiver();
                        getActivity().registerReceiver(myreceiver, intentFilter);
                        Intent intent = new Intent(BROADCAST);
                        PendingIntent operation = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
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
                            Log.d("timemon","added 7 days");
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
                    alarmUp = (PendingIntent.getBroadcast(getActivity(), 0,
                            new Intent(BROADCAST),
                            PendingIntent.FLAG_NO_CREATE) != null);
                    if (alarmUp){
                        PendingIntent.getBroadcast(getActivity(), 0,
                                new Intent(BROADCAST),PendingIntent.FLAG_NO_CREATE).cancel();
                        Log.d("alarm status","stopped");
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