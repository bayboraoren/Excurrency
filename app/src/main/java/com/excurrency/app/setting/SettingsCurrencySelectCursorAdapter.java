package com.excurrency.app.setting;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.excurrency.app.R;
import com.excurrency.app.data.CurrencyContract;

import java.util.ArrayList;

/**
 * Created by bora on 01.07.2015.
 */
public class SettingsCurrencySelectCursorAdapter extends CursorAdapter implements Filterable{

    public static final String LOG_TAG = SettingsCurrencySelectCursorAdapter.class.getSimpleName();


    // declaring our ArrayList of items
    public ArrayList<SettingsCurrencySelectModel> settingsCurrencySelectModelArrayList;


    public SettingsCurrencySelectCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    public static class ViewHolder {

        public final ImageView currencyFlagImage;
        public final TextView currencyCountryName;
        public final TextView currencySymbolName;
        public final ToggleButton currencyEnabled;

        public ViewHolder(View view,final Context context,Cursor cursor) {

            currencyFlagImage = (ImageView) view.findViewById(R.id.currency_flag_image);
            currencyCountryName = (TextView) view.findViewById(R.id.currency_country_name);
            currencySymbolName= (TextView) view.findViewById(R.id.currency_symbol_name);
            currencyEnabled = (ToggleButton) view.findViewById(R.id.currency_enabled);

        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {



        View view = LayoutInflater.from(context).inflate(R.layout.settings_currency_select_list_item, parent, false);

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

        final String id = cursor.getString(0);

        if(resourceId!=0) {

            viewHolder.currencyFlagImage.setImageDrawable(resources.getDrawable(resourceId));
            viewHolder.currencyCountryName.setText(cursor.getInt(0) + " " + cursor.getString(2).replaceAll("_", " "));
            viewHolder.currencySymbolName.setText(cursor.getString(1));
            boolean b = (cursor.getInt(4) != 0);
            viewHolder.currencyEnabled.setChecked(b);

            viewHolder.currencyEnabled.setActivated(false);
            viewHolder.currencyEnabled.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (((ToggleButton) v).isChecked()) {

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(CurrencyContract.CurrencyPropertyEntry.COLUMN_CURRENCY_ENABLED, true);

                        String where = CurrencyContract.CurrencyPropertyEntry._ID + " = ?";

                        context.getContentResolver().update(CurrencyContract.CurrencyPropertyEntry.buildCurrencyPropertyUpdateToggleUri(true),
                                contentValues, where, new String[]{id});


                    } else {

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(CurrencyContract.CurrencyPropertyEntry.COLUMN_CURRENCY_ENABLED, false);

                        String where = CurrencyContract.CurrencyPropertyEntry._ID + " = ?";

                        context.getContentResolver().update(CurrencyContract.CurrencyPropertyEntry.buildCurrencyPropertyUpdateToggleUri(true),
                                contentValues, where, new String[]{id});

                    }
                }

            });



        }

    }


    @Override
    public Filter getFilter() {

        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<SettingsCurrencySelectModel> tempList=new ArrayList<>();
                //constraint is the result from text you want to filter against.
                //objects is your data set you will filter from
                if(constraint != null && settingsCurrencySelectModelArrayList !=null) {
                    int length= settingsCurrencySelectModelArrayList.size();
                    int i=0;
                    while(i<length){
                        SettingsCurrencySelectModel item= settingsCurrencySelectModelArrayList.get(i);
                        //do whatever you wanna do here
                        //adding result set output array

                        tempList.add(item);

                        i++;
                    }
                    //following two lines is very important
                    //as publish result can only take FilterResults objects
                    filterResults.values = tempList;
                    filterResults.count = tempList.size();
                }
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                settingsCurrencySelectModelArrayList = (ArrayList<SettingsCurrencySelectModel>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return myFilter;
    }
}
