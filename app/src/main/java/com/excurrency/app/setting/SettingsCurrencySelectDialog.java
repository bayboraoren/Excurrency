package com.excurrency.app.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.excurrency.app.R;
import com.excurrency.app.data.CurrencyContract;

/**
 * Created by bora on 30.06.2015.
 * reference : http://mysoftdevs.blogspot.com.tr/2011/12/android-how-to-create-dialogpreference.html
 */
public class SettingsCurrencySelectDialog extends DialogPreference{

    private SettingsCurrencySelectCursorAdapter arrAdapter;

    public SettingsCurrencySelectDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        setDialogLayoutResource(R.layout.settings_currency_select_dialog);
    }



    private void getCurrencies(Cursor cursor,View view){


        ListView list = (ListView) view.findViewById(R.id.settings_currency_select_list);

        arrAdapter = new SettingsCurrencySelectCursorAdapter(getContext(), cursor, 0);

        list.setAdapter(arrAdapter);

        EditText inputSearch = (EditText) view.findViewById(R.id.inputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                arrAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        list.setAdapter(arrAdapter);
    }


    @Override
    protected View onCreateView(ViewGroup parent) {
        return super.onCreateView(parent);

    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setTitle("Test");
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

        getCurrencies(cursor,view);

    }


    /* @Override
    public void onBindDialogView(View view) {

        ListView list = (ListView) view.findViewById(R.id.settings_currency_select_list);
        ArrayList<SettingsCurrencySelectModel> arrList = new ArrayList<SettingsCurrencySelectModel>();

        for (int i = 0; i < 163; i++) {
            SettingsCurrencySelectModel settingsCurrencySelectModel = new SettingsCurrencySelectModel(R.drawable.turkey, "Turkey", "TRY", 23.473F + i);
            arrList.add(settingsCurrencySelectModel);
        }

        arrAdapter = new SettingsCurrencySelectCursorAdapter(getContext(), null, 0);

        EditText inputSearch = (EditText) view.findViewById(R.id.inputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                arrAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        list.setAdapter(arrAdapter);

        super.onBindDialogView(view);
    }*/

}
