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

        public static Uri buildCurrencyListUri(boolean enabled) {
            return CONTENT_URI.buildUpon().appendPath("ENABLED").appendPath("TRUE").build();
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
        public static final String COLUMN_CURRENCY_TIME = "currency_time";
        public static final String COLUMN_CURRENCY_ASK = "currency_ask";
        public static final String COLUMN_CURRENCY_BID = "currency_bid";


    }





}
