package com.excurrency.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.excurrency.app.data.CurrencyContract.*;

/**
 * Created by bora on 23.06.2015.
 */
public class CurrencyDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "currency.db";


    public CurrencyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_CURRENCY_PROPERTY_TABLE = "CREATE TABLE " + CurrencyPropertyEntry.TABLE_NAME + " (" +
                CurrencyPropertyEntry._ID + " INTEGER PRIMARY KEY," +
                CurrencyPropertyEntry.COLUMN_CURRENCY_CODE + " TEXT UNIQUE NOT NULL, " +
                CurrencyPropertyEntry.COLUMN_CURRENCY_COUNTRY + " TEXT NOT NULL, " +
                CurrencyPropertyEntry.COLUMN_CURRENCY_NAME + " TEXT NOT NULL, " +
                CurrencyPropertyEntry.COLUMN_CURRENCY_ENABLED + " flag INTEGER DEFAULT 0 " +
                " );";


        final String SQL_CREATE_CURRENCY_DATA_TABLE = "CREATE TABLE " + CurrencyDataEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                CurrencyDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                CurrencyDataEntry.COLUMN_CURRENCY_PROPERTY_KEY + " INTEGER NOT NULL, " +
                CurrencyDataEntry.COLUMN_CURRENCY_NAME + " TEXT NOT NULL, " +
                CurrencyDataEntry.COLUMN_CURRENCY_RATE+ " REAL NOT NULL, " +
                CurrencyDataEntry.COLUMN_CURRENCY_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +

                CurrencyDataEntry.COLUMN_CURRENCY_ASK + " REAL NOT NULL, " +
                CurrencyDataEntry.COLUMN_CURRENCY_BID + " REAL NOT NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + CurrencyDataEntry.COLUMN_CURRENCY_PROPERTY_KEY + ") REFERENCES " +
                CurrencyPropertyEntry.TABLE_NAME + " (" + CurrencyPropertyEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + CurrencyDataEntry.COLUMN_CURRENCY_DATE + ", " +
                CurrencyDataEntry.COLUMN_CURRENCY_PROPERTY_KEY + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_CURRENCY_PROPERTY_TABLE);
        db.execSQL(SQL_CREATE_CURRENCY_DATA_TABLE);
        addCurrencyProperties(db);

    }


    private void addCurrencyProperties(SQLiteDatabase db){
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CurrencyPropertyEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CurrencyDataEntry.TABLE_NAME);
        onCreate(db);
    }
}
