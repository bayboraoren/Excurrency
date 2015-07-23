package com.excurrency.app.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.excurrency.app.R;
import com.excurrency.app.data.CurrencyContract;

/**
 * Created by bora on 30.06.2015.
 * reference : http://mysoftdevs.blogspot.com.tr/2011/12/android-how-to-create-dialogpreference.html
 */
public class SettingsToCurrencySelectDialog extends DialogPreference{

    private SettingsToCurrencySelectCursorAdapter arrAdapter;

    public SettingsToCurrencySelectDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        setDialogLayoutResource(R.layout.settings_to_currency_select_dialog);
    }

    private void getCurrencies(Cursor cursor,View view){

        ListView list = (ListView) view.findViewById(R.id.settings_to_currency_select_list);

        arrAdapter = new SettingsToCurrencySelectCursorAdapter(getContext(), cursor, 0,this);

        list.setAdapter(arrAdapter);
    }


    @Override
    protected View onCreateView(ViewGroup parent) {
        return super.onCreateView(parent);

    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setTitle(R.string.convert_to_currencies);
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected void onBindDialogView(final View view) {
        super.onBindDialogView(view);

        String sortOrder = CurrencyContract.CurrencyPropertyEntry.COLUMN_CURRENCY_COUNTRY + " ASC";
        Uri currencyListUri = CurrencyContract.CurrencyPropertyEntry.buildCurrencyPropertyListUri(true);
        Cursor cursor = getContext().getContentResolver().query(currencyListUri,null,null,null,sortOrder);

        getCurrencies(cursor, view);

    }

    @Override
    public CharSequence getSummary() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String currencyToValue = prefs.getString(getContext().getString(R.string.pref_currency_convert_to_key), getContext().getString(R.string.pref_currency_convert_to_default));
        currencyToValue = currencyToValue.toUpperCase();
        String currencyCountryToValue = prefs.getString(getContext().getString(R.string.pref_currency_country_convert_to_key), getContext().getString(R.string.pref_currency_country_convert_to_default));
        currencyCountryToValue = currencyCountryToValue.replaceAll("_"," ").toUpperCase();

        return currencyCountryToValue + " (" + currencyToValue + ")";
    }

}
