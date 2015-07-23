package com.excurrency.app;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by bora on 23.06.2015.
 */
public class CurrencyViewHolder {

    ImageView flagImage; //country flag image
    TextView currencyCode; //currency code ex:USD, EUR etc.
    TextView currencyCountryName; //currency code description ex:United States Dollar
    TextView currencyPrice; // currency value in now
    ImageView currencyUpOrDownImage; //increase or decrease currency
    TextView currencyConvertTo;


    public CurrencyViewHolder(View view) {
        flagImage = (ImageView)view.findViewById(R.id.flag_image);
        currencyCode = (TextView)view.findViewById(R.id.currency_code);
        currencyPrice = (TextView)view.findViewById(R.id.currency_price);
        currencyCountryName = (TextView)view.findViewById(R.id.currency_country_name);
        currencyUpOrDownImage = (ImageView)view.findViewById(R.id.currency_upOrDown_image);
        currencyConvertTo = (TextView)view.findViewById(R.id.currency_convert_to);
    }


}
