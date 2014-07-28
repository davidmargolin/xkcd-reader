package com.tod.android.xkcdreader;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
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
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Margolin on 7/9/2014.
 */
public class SettingsFragment extends PreferenceFragment {
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
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
                Intent intent = new Intent(getActivity(), Receiver.class);
                Intent intent1 = new Intent(getActivity(), Receiver.class);
                Intent intent2 = new Intent(getActivity(), Receiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getActivity(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getActivity(), 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alrm = (AlarmManager)getActivity().getSystemService(getActivity().ALARM_SERVICE);
                AlarmManager alrm1 = (AlarmManager)getActivity().getSystemService(getActivity().ALARM_SERVICE);
                AlarmManager alrm2 = (AlarmManager)getActivity().getSystemService(getActivity().ALARM_SERVICE);

                if (checked) {
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    pm.setComponentEnabledSetting(notifreceiver,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
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
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("notifs", true);
                    editor.commit();
                }else{
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    pm.setComponentEnabledSetting(notifreceiver,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    alrm1.cancel(pendingIntent);
                    alrm.cancel(pendingIntent);
                    pendingIntent.cancel();
                    pendingIntent1.cancel();
                    pendingIntent2.cancel();
                    alrm2.cancel(pendingIntent);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("notifs", false);
                    editor.commit();
                }
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