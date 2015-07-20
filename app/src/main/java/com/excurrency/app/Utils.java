package com.excurrency.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by bora on 20.07.2015.
 */
public class Utils {

    public static String getSchedule(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_schedule_time_key),
                context.getString(R.string.pref_schedule_time_five));
    }


}
