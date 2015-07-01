package com.excurrency.app.setting;

import android.graphics.drawable.Drawable;

/**
 * Created by bora on 01.07.2015.
 */
public class SettingsCurrencySelectModel {


    public int currencyFlagImage;
    public String currencyCountryName;
    public String currencySymbolName;
    public Float currencyRate;

    public SettingsCurrencySelectModel(int currencyFlagImage,
                                       String currencyCountryName,
                                       String currencySymbolName,
                                       Float currencyRate) {

        this.currencyCountryName = currencyCountryName;
        this.currencyFlagImage = currencyFlagImage;
        this.currencySymbolName = currencySymbolName;
        this.currencyRate = currencyRate;
    }

}
