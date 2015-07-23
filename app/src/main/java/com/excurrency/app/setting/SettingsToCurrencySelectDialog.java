package com.excurrency.app.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.preference.DialogPreference;
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

        arrAdapter = new SettingsToCurrencySelectCursorAdapter(getContext(), cursor, 0);

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

        String str = "";

        Cursor cursor = getContext().getContentResolver().query(CurrencyContract.CurrencyDataEntry.buildCurrencyDataBySelectedCurrencyPropertyUri(true),
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
