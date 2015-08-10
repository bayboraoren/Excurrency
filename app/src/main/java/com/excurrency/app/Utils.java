package com.excurrency.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.excurrency.app.data.CurrencyContract;

/**
 * Created by bora on 20.07.2015.
 */
public class Utils {

    public static String getSchedule(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_schedule_time_key),
                context.getString(R.string.pref_schedule_time_five));
    }


    public static String getSummaryForCurrencyConvertTo(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String currencyToValue = prefs.getString(context.getString(R.string.pref_currency_convert_to_key), context.getString(R.string.pref_currency_convert_to_default));
        currencyToValue = currencyToValue.toUpperCase();
        String currencyCountryToValue = prefs.getString(context.getString(R.string.pref_currency_country_convert_to_key), context.getString(R.string.pref_currency_country_convert_to_default));
        currencyCountryToValue = currencyCountryToValue.replaceAll("_"," ").toUpperCase();

        return currencyCountryToValue + " (" + currencyToValue + ")";

    }


    public static String getSummaryForSelectCurrencies(Context context){
        String str = "";

        Cursor cursor = context.getContentResolver().query(CurrencyContract.CurrencyDataEntry.buildCurrencyDataBySelectedCurrencyPropertyUri(),
                null,
                null,
                null,
                null);

        if(cursor.getCount()>0) {


            cursor.moveToFirst();

            do {
                //TODO currency country
                str = str + cursor.getString(9) + ", ";

            } while (cursor.moveToNext());

            cursor.close();

            str = str.replaceAll("_", " ").toUpperCase();

        }

        return str;
    }

}
