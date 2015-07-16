package com.excurrency.app;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.excurrency.app.data.CurrencyContract;

/**
 * Created by bora on 22.06.2015.
 */
public class CurrencyAdapter extends CursorAdapter {

    public CurrencyAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_currency,parent,false);
        CurrencyViewHolder currencyViewHolder = new CurrencyViewHolder(view);
        view.setTag(currencyViewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        CurrencyViewHolder viewHolder = (CurrencyViewHolder ) view.getTag();


        /*select * from currency_data inner join currency_property on
        currency_data.currency_property_id = currency_property._id;*/

        int currencyCountryIndex = cursor.getColumnIndex(CurrencyContract.CurrencyPropertyEntry.COLUMN_CURRENCY_COUNTRY);

        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(cursor.getString(currencyCountryIndex), "drawable",
                context.getPackageName());

        if(resourceId!=0) {

            viewHolder.flagImage.setImageDrawable(resources.getDrawable(resourceId));


            int currencyCountryNameIndex = cursor.getColumnIndex(CurrencyContract.CurrencyPropertyEntry.COLUMN_CURRENCY_COUNTRY);
            viewHolder.currencyCountryName.setText(cursor.getString(1) + " " + cursor.getString(currencyCountryNameIndex));

            //int currencyCodeIndex = cursor.getColumnIndex(CurrencyContract.CurrencyPropertyEntry.COLUMN_CURRENCY_CODE);
            int currencyNameIndex = cursor.getColumnIndex(CurrencyContract.CurrencyPropertyEntry.COLUMN_CURRENCY_NAME);
            viewHolder.currencyCode.setText(cursor.getString(currencyNameIndex));

            int currencyRateIndex = cursor.getColumnIndex(CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_RATE);
            viewHolder.currencyPrice.setText(cursor.getString(currencyRateIndex));



        }
    }
}
