package com.excurrency.app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by bora on 23.06.2015.
 */
public class CurrencyPropertyProvider extends ContentProvider {

    private CurrencyDBHelper mCurrencyDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int CURRENCY_DATA = 100;
    static final int CURRENCY_PROPERTY = 101;
    static final int CURRENCY_DATA_BY_SELECTED_CURRENCY_PROPERTY = 102;
    static final int CURRENCY_PROPERTY_ENABLED = 103;
    static final int CURRENCY_DATA_BY_ID = 104;

    private static final SQLiteQueryBuilder sCurrencyDataByCurrencyPropertyBuilder;

    static{
        sCurrencyDataByCurrencyPropertyBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sCurrencyDataByCurrencyPropertyBuilder.setTables(
                CurrencyContract.CurrencyDataEntry.TABLE_NAME + " INNER JOIN " +
                        CurrencyContract.CurrencyPropertyEntry.TABLE_NAME +
                        " ON " + CurrencyContract.CurrencyDataEntry.TABLE_NAME +
                        "." + CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_PROPERTY_KEY+
                        " = " + CurrencyContract.CurrencyPropertyEntry.TABLE_NAME +
                        "." + CurrencyContract.CurrencyPropertyEntry._ID);
    }

    @Override
    public boolean onCreate() {
        mCurrencyDbHelper = new CurrencyDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor=null;

        switch (sUriMatcher.match(uri)) {

            case CURRENCY_DATA: {
                break;
            }

            case CURRENCY_PROPERTY: {
                retCursor = getCurrencyProperty(sortOrder);
                break;
            }

            case CURRENCY_DATA_BY_SELECTED_CURRENCY_PROPERTY: {
                retCursor = getCurrencyDataBySelectedCurrencyProperty(null);
                break;
            }

            case CURRENCY_DATA_BY_ID:{
                retCursor = getCurrencyDataById(uri);
                break;
            }

            case CURRENCY_PROPERTY_ENABLED: {
                retCursor = getCurrencyPropertyEnabled();
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mCurrencyDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CURRENCY_DATA:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CurrencyContract.CurrencyDataEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int count;

        switch (sUriMatcher.match(uri)) {

            case CURRENCY_PROPERTY: {

                count = mCurrencyDbHelper.getWritableDatabase().update(
                        CurrencyContract.CurrencyPropertyEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        getContext().getContentResolver().notifyChange(uri, null);
        return count;


    }


    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CurrencyContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, CurrencyContract.PATH_CURRENCY_DATA, CURRENCY_DATA);
        matcher.addURI(authority, CurrencyContract.PATH_CURRENCY_DATA + "/ENABLED", CURRENCY_DATA_BY_SELECTED_CURRENCY_PROPERTY);
        matcher.addURI(authority, CurrencyContract.PATH_CURRENCY_PROPERTY, CURRENCY_PROPERTY);
        matcher.addURI(authority, CurrencyContract.PATH_CURRENCY_PROPERTY + "/ENABLED", CURRENCY_PROPERTY_ENABLED);
        matcher.addURI(authority, CurrencyContract.PATH_CURRENCY_DATA + "/#", CURRENCY_DATA_BY_ID);



        return matcher;

    }


    private Cursor getCurrencyProperty(String sortOrder){

        return mCurrencyDbHelper.getReadableDatabase().query(
                CurrencyContract.CurrencyPropertyEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder);
    }


    private Cursor getCurrencyDataBySelectedCurrencyProperty(String sortOrder){

        return sCurrencyDataByCurrencyPropertyBuilder.query(mCurrencyDbHelper.getReadableDatabase(),
                null,
                sCurrencyPropertySelectedSelection,
                new String[]{"1"},
                null,
                null,
                sortOrder
        );

    }


    private Cursor getCurrencyPropertyEnabled(){

        return mCurrencyDbHelper.getReadableDatabase().query(
                CurrencyContract.CurrencyPropertyEntry.TABLE_NAME,
                null,
                sCurrencyPropertySelectedSelection,
                new String[]{"1"},
                null,
                null,
                null);
    }


    private Cursor getCurrencyDataById(Uri uri){

        String id = Long.toString(ContentUris.parseId(uri));


        return sCurrencyDataByCurrencyPropertyBuilder.query(mCurrencyDbHelper.getReadableDatabase(),
                null,
                sCurrencyDataId,
                new String[]{id},
                null,
                null,
                null
        );

    }


    private static final String sCurrencyPropertySelectedSelection =
            CurrencyContract.CurrencyPropertyEntry.TABLE_NAME+
                    "." + CurrencyContract.CurrencyPropertyEntry.COLUMN_CURRENCY_ENABLED+ " = ? ";

    private static final String sCurrencyDataId =
            CurrencyContract.CurrencyDataEntry.TABLE_NAME+
                    "." + CurrencyContract.CurrencyPropertyEntry._ID+ " = ? ";



}
