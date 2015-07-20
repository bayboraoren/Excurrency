package com.excurrency.app.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by bora on 23.06.2015.
 */
public class CurrencyContract {

    public static final String CONTENT_AUTHORITY = "com.excurrency.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CURRENCY_DATA = "currency_data";
    public static final String PATH_CURRENCY_PROPERTY = "currency_property";


    //CURRENCY PROPERTY ENTRY
    public static final class CurrencyPropertyEntry implements  BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CURRENCY_PROPERTY).build();

        public static final String TABLE_NAME = "currency_property";

        public static final String COLUMN_CURRENCY_CODE = "currency_code";
        public static final String COLUMN_CURRENCY_NAME = "currency_name";
        public static final String COLUMN_CURRENCY_COUNTRY = "currency_country";
        public static final String COLUMN_CURRENCY_ENABLED = "currency_enabled";


        //TABLE INDEX
        public static final int INDEX_COLUMN_CURRENCY_PROPERTY_ID = 0;
        public static final int INDEX_COLUMN_CURRENCY_PROPERTY_CODE = 1;
        public static final int INDEX_COLUMN_CURRENCY_PROPERTY_COUNTRY = 2;
        public static final int INDEX_COLUMN_CURRENCY_PROPERTY_NAME = 3;
        public static final int INDEX_COLUMN_CURRENCY_PROPERTY_ENABLED = 4;



        public static Uri buildCurrencyPropertyListUri(boolean enabled) {
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildCurrencyPropertyListEnabledUri(boolean enabled) {
            return CONTENT_URI.buildUpon().appendPath("ENABLED").build();
        }


        public static Uri buildCurrencyPropertyUpdateToggleUri(boolean enabled) {
            return CONTENT_URI.buildUpon().build();
        }


    }

    //CURRENCY DATA ENTRY
    public static final class CurrencyDataEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CURRENCY_DATA).build();


        public static final String TABLE_NAME = "currency_data";

        public static final String COLUMN_CURRENCY_PROPERTY_KEY = "currency_property_id";

        //Yahoo finance currency api json
        public static final String COLUMN_CURRENCY_NAME = "currency_name";
        public static final String COLUMN_CURRENCY_RATE = "currency_rate";
        public static final String COLUMN_CURRENCY_DATE = "currency_date";
        public static final String COLUMN_CURRENCY_ASK = "currency_ask";
        public static final String COLUMN_CURRENCY_BID = "currency_bid";


        //TABLE INDEX
        public static final int INDEX_COLUMN_CURRENCY_DATA_ID = 0;
        public static final int INDEX_COLUMN_CURRENCY_PROPERTY_KEY = 1;
        public static final int INDEX_COLUMN_CURRENCY_DATA_NAME = 2;
        public static final int INDEX_COLUMN_CURRENCY_DATA_RATE = 3;
        public static final int INDEX_COLUMN_CURRENCY_DATA_DATE = 4;
        public static final int INDEX_COLUMN_CURRENCY_DATA_ASK = 5;
        public static final int INDEX_COLUMN_CURRENCY_DATA_BID = 6;

        public static Uri buildCurrencyDataBySelectedCurrencyPropertyUri(boolean enabled) {
            return CONTENT_URI.buildUpon().appendPath("ENABLED").build();
        }

        public static Uri buildCurrencyDataById(String id) {
            return ContentUris.withAppendedId(CurrencyContract.CurrencyDataEntry.CONTENT_URI,Integer.parseInt(id));
        }


    }





}
