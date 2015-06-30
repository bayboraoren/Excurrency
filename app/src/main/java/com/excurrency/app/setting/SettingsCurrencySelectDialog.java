package com.excurrency.app.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

import com.excurrency.app.R;

/**
 * Created by bora on 30.06.2015.
 * reference : http://mysoftdevs.blogspot.com.tr/2011/12/android-how-to-create-dialogpreference.html
 */
public class SettingsCurrencySelectDialog extends DialogPreference{

    public SettingsCurrencySelectDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        setDialogLayoutResource(R.layout.settings_currency_select_dialog);
    }


    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setTitle("Test");
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    public void onBindDialogView(View view){
        super.onBindDialogView(view);
    }

}
