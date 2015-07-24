package com.excurrency.app.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.excurrency.app.DateUtil;
import com.excurrency.app.R;
import com.excurrency.app.data.CurrencyContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by bora on 24.06.2015.
 */
public class CurrencyService extends IntentService {

    public static final String LOG_TAG = CurrencyService.class.getSimpleName();
    public String toCurrencyCode = "TRY";

    public CurrencyService() {
        super("Excurrency");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Cursor retCurrencyPropertyCursor = this.getContentResolver().query(
                CurrencyContract.CurrencyPropertyEntry.buildCurrencyPropertyListEnabledUri(),
                null,
                null,
                null,
                null
        );

        if(retCurrencyPropertyCursor.getCount()>0) {

            String where = "";

            while (retCurrencyPropertyCursor.moveToNext()) {

                String currencyCode = retCurrencyPropertyCursor.getString(CurrencyContract.CurrencyPropertyEntry.INDEX_COLUMN_CURRENCY_PROPERTY_CODE);


                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                toCurrencyCode = prefs.getString(getApplicationContext().getString(R.string.pref_currency_convert_to_key), getApplicationContext().getString(R.string.pref_currency_convert_to_default));



                //"%20%22USDTRY%22","%20%22USDTRY%22"
                where = where + "\"" + currencyCode + toCurrencyCode + "\"" + ",";

            }

            where = where.substring(0, where.lastIndexOf(',', where.lastIndexOf(',')));


            String CURRENCY_BASE_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(" + where + ")&lang=en&env=store://datatables.org/alltableswithkeys&format=json";

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            Uri builtUri = Uri.parse(CURRENCY_BASE_URL).buildUpon()
                    .build();

            try {
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    return;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }

                if (builder.length() == 0) {
                    return;
                }

                getDataFromCurrency(builder.toString());

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

        }

    }


    private void getDataFromCurrency(String currencyJsonStr) {

/*

{
    query: {
        count: 3,
        created: "2015-06-29T11:34:09Z",
        lang: "en-US",
        results: {
            rate: [
                {
                    id: "EURTRY",
                    Name: "EUR/TRY",
                    Rate: "2.9816",
                    Date: "6/29/2015",
                    Time: "12:34pm",
                    Ask: "2.9827",
                    Bid: "2.9806"
                },
                {
                    id: "USDTRY",
                    Name: "USD/TRY",
                    Rate: "2.6825",
                    Date: "6/29/2015",
                    Time: "12:34pm",
                    Ask: "2.6830",
                    Bid: "2.6825"
                },.....

 */

        Cursor retCurrencyPropertyCursor = this.getContentResolver().query(
                CurrencyContract.CurrencyPropertyEntry.buildCurrencyPropertyListEnabledUri(),
                null,
                null,
                null,
                null
        );



        //currency resources information
        final String CURRENCY_QUERY = "query";
        final String CURRENCY_RESULTS = "results";
        final String CURRENCY_RATE_ARRAY = "rate";

        //currency information
        final String CURRENCY_ID = "id";
        final String CURRENCY_NAME = "Name";
        final String CURRENCY_RATE = "Rate";
        final String CURRENCY_DATE = "Date";
        final String CURRENCY_TIME = "Time";
        final String CURRENCY_ASK = "Ask";
        final String CURRENCY_BID = "Bid";

        String id;
        String name;
        String rate;
        long date;
        String ask;
        String bid;


        try {


            JSONObject currencyJson = new JSONObject(currencyJsonStr);
            JSONObject query = currencyJson.getJSONObject(CURRENCY_QUERY);
            JSONObject results = query.getJSONObject(CURRENCY_RESULTS);
            JSONArray rateArray = results.getJSONArray(CURRENCY_RATE_ARRAY);

            Vector<ContentValues> currencyVector = new Vector<>(rateArray.length());


            for (int counter = 0; counter < rateArray.length(); counter++) {

                ContentValues currencyValues = new ContentValues();

                JSONObject rateObject = rateArray.getJSONObject(counter);
                id = rateObject.getString(CURRENCY_ID);
                name = rateObject.getString(CURRENCY_NAME);
                rate = rateObject.getString(CURRENCY_RATE);
                date = DateUtil.convert(rateObject.getString(CURRENCY_DATE),rateObject.getString(CURRENCY_TIME));
                ask = rateObject.getString(CURRENCY_ASK);
                bid = rateObject.getString(CURRENCY_BID);


                long currencyPropertyId;

                retCurrencyPropertyCursor.moveToFirst();
                do{

                    String currencyCode = retCurrencyPropertyCursor.getString(CurrencyContract.CurrencyPropertyEntry.INDEX_COLUMN_CURRENCY_PROPERTY_CODE);
                    String cursorCurrencyId = currencyCode + toCurrencyCode;

                    if(id.equals(cursorCurrencyId)){

                        int currencyPropertyIdIndex = retCurrencyPropertyCursor.getColumnIndex(CurrencyContract.CurrencyPropertyEntry._ID);
                        currencyPropertyId = retCurrencyPropertyCursor.getLong(currencyPropertyIdIndex);


                        currencyValues.put(CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_PROPERTY_KEY, currencyPropertyId);
                        currencyValues.put(CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_NAME, name);
                        currencyValues.put(CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_RATE, rate);
                        currencyValues.put(CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_DATE, date);
                        currencyValues.put(CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_ASK, ask);
                        currencyValues.put(CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_BID, bid);

                        currencyVector.add(currencyValues);

                        break;
                    }

                }while(retCurrencyPropertyCursor.moveToNext());





            }


            if ( currencyVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[currencyVector.size()];
                currencyVector.toArray(cvArray);
                this.getContentResolver().bulkInsert(CurrencyContract.CurrencyDataEntry.CONTENT_URI, cvArray);
            }

            retCurrencyPropertyCursor.close();
            Log.i(LOG_TAG, "Tamamlandi...");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        } finally {
            retCurrencyPropertyCursor.close();
        }


    }


    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent = new Intent(context, CurrencyService.class);
            context.startService(sendIntent);
        }
    }

}
