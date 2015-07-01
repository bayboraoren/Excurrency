package com.excurrency.app.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.excurrency.app.R;

import java.util.ArrayList;

/**
 * Created by bora on 01.07.2015.
 */
public class SettingsCurrencySelectAdapter extends ArrayAdapter<SettingsCurrencySelectModel> implements Filterable{

    // declaring our ArrayList of items
    public ArrayList<SettingsCurrencySelectModel> settingsCurrencySelectModelArrayList;



    public SettingsCurrencySelectAdapter(Context context, ArrayList<SettingsCurrencySelectModel> settingsCurrencySelectModels) {
        super(context, 0, settingsCurrencySelectModels);
        this.settingsCurrencySelectModelArrayList = settingsCurrencySelectModels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SettingsCurrencySelectModel settingsCurrencySelectModel =  getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.settings_currency_select_list_item, parent, false);
        }

        ImageView currencyFlagImage =  (ImageView)convertView.findViewById(R.id.currency_flag_image);
        currencyFlagImage.setImageResource(settingsCurrencySelectModel.currencyFlagImage);

        TextView currencyCountryName = (TextView)convertView.findViewById(R.id.currency_country_name);
        currencyCountryName.setText(settingsCurrencySelectModel.currencyCountryName);

        TextView currencySymbolName = (TextView)convertView.findViewById(R.id.currency_symbol_name);
        currencySymbolName.setText(settingsCurrencySelectModel.currencySymbolName);



        return convertView;
    }


    @Override
    public Filter getFilter() {

        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<SettingsCurrencySelectModel> tempList=new ArrayList<SettingsCurrencySelectModel>();
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
