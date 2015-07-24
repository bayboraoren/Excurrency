package com.excurrency.app.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.excurrency.app.R;
import com.excurrency.app.Utils;
import com.excurrency.app.data.CurrencyContract;

/**
 * Created by bora on 01.07.2015.
 */
public class SettingsToCurrencySelectCursorAdapter extends CursorAdapter implements Filterable{

    public static final String LOG_TAG = SettingsToCurrencySelectCursorAdapter.class.getSimpleName();
    private SettingsToCurrencySelectDialog dialog=null;

    public SettingsToCurrencySelectCursorAdapter(Context context, Cursor c, int flags,SettingsToCurrencySelectDialog dialog) {
        super(context, c, flags);
        this.dialog = dialog;
    }


    public static class ViewHolder {

        public final ImageView currencyFlagImage;
        public final TextView currencyCountryName;
        public final TextView currencySymbolName;
        public final RadioButton currencyEnabled;

        public ViewHolder(View view,final Context context,Cursor cursor) {

            currencyFlagImage = (ImageView) view.findViewById(R.id.currency_flag_image);
            currencyCountryName = (TextView) view.findViewById(R.id.currency_country_name);
            currencySymbolName= (TextView) view.findViewById(R.id.currency_symbol_name);
            currencyEnabled = (RadioButton) view.findViewById(R.id.currency_enabled);

        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {



        View view = LayoutInflater.from(context).inflate(R.layout.settings_to_currency_select_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view,context,cursor);
        view.setTag(viewHolder);


        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        Log.i(LOG_TAG,cursor.getInt(0) + " " + cursor.getString(2));

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(cursor.getString(2), "drawable",
                context.getPackageName());

        final String currencyCode = cursor.getString(CurrencyContract.CurrencyPropertyEntry.INDEX_COLUMN_CURRENCY_PROPERTY_CODE);
        final String currencyCountry = cursor.getString(CurrencyContract.CurrencyPropertyEntry.INDEX_COLUMN_CURRENCY_PROPERTY_COUNTRY);


        //get currency convert to shared preference
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String currencyToValue = prefs.getString(context.getString(R.string.pref_currency_convert_to_key), context.getString(R.string.pref_currency_convert_to_default));

        if (resourceId!=0) {

            //change it to selected
            if (currencyToValue.equals(cursor.getString(CurrencyContract.CurrencyPropertyEntry.INDEX_COLUMN_CURRENCY_PROPERTY_CODE))) {
                viewHolder.currencyEnabled.setChecked(true);
            } else {
                viewHolder.currencyEnabled.setChecked(false);
            }

            viewHolder.currencyFlagImage.setImageDrawable(resources.getDrawable(resourceId));
            viewHolder.currencyCountryName.setText(cursor.getString(2).replaceAll("_", " ").toUpperCase());
            viewHolder.currencySymbolName.setText(cursor.getString(1));



            viewHolder.currencyEnabled.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (((RadioButton) v).isChecked()) {

                        /*ContentValues contentValues = new ContentValues();
                        contentValues.put(CurrencyContract.CurrencyPropertyEntry.COLUMN_CURRENCY_ENABLED, true);

                        String where = CurrencyContract.CurrencyPropertyEntry._ID + " = ?";

                        context.getContentResolver().update(CurrencyContract.CurrencyPropertyEntry.buildCurrencyPropertyUpdateToggleUri(true),
                                contentValues, where, new String[]{id});*/

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(context.getString(R.string.pref_currency_convert_to_key),currencyCode);
                        editor.putString(context.getString(R.string.pref_currency_country_convert_to_key), currencyCountry);
                        editor.commit();

                        dialog.getDialog().dismiss();
                        dialog.setSummary(Utils.getSummaryForCurrencyConvertTo(context));

                    } else {

                        /*ContentValues contentValues = new ContentValues();
                        contentValues.put(CurrencyContract.CurrencyPropertyEntry.COLUMN_CURRENCY_ENABLED, false);

                        String where = CurrencyContract.CurrencyPropertyEntry._ID + " = ?";

                        context.getContentResolver().update(CurrencyContract.CurrencyPropertyEntry.buildCurrencyPropertyUpdateToggleUri(true),
                                contentValues, where, new String[]{id});*/

                        dialog.getDialog().dismiss();

                    }


                }

            });



        }

    }



}
