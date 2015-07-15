package com.excurrency.app;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Currency;

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


        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(cursor.getString(2), "drawable",
                context.getPackageName());

        if(resourceId!=0) {
            viewHolder.flagImage.setImageDrawable(resources.getDrawable(resourceId));
            viewHolder.currencyName.setText(cursor.getString(2));
            viewHolder.currencyCode.setText(cursor.getString(1));
        }
    }
}
