package com.excurrency.app.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by bora on 23.06.2015.
 */
public class CurrencyPropertyProvider extends ContentProvider{

    private CurrencyDBHelper mCurrencyDbHelper;


    @Override
    public boolean onCreate() {
        mCurrencyDbHelper = new CurrencyDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor  retCursor = mCurrencyDbHelper.getReadableDatabase().query(
                CurrencyContract.CurrencyPropertyEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

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
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        return 0;
    }
}
