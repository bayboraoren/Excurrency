package com.excurrency.app.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.excurrency.app.DateUtil;
import com.excurrency.app.data.CurrencyContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * Created by bora on 24.06.2015.
 */
public class CurrencyService extends IntentService {

    public static final String LOG_TAG  = CurrencyService.class.getSimpleName();


    public CurrencyService() {
        super("Excurrency");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String CURRENCY_BASE_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22EURTRY%22,%20%22USDTRY%22,%20%22TRYEUR%22)&lang=fr-CA&env=store://datatables.org/alltableswithkeys&format=json";

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
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return;
            }

            getDataFromCurrency(buffer.toString());

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


    private void getDataFromCurrency(String currencyJsonStr){

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


        //currency resources information
        final String CURRENCY_QUERY="query";
        final String CURRENCY_RESULTS="results";
        final String CURRENCY_RATE_ARRAY="rate";

        //currency information
        final String CURRENCY_NAME="Name";
        final String CURRENCY_RATE="Rate";
        final String CURRENCY_DATE="Date";
        final String CURRENCY_TIME="Time";
        final String CURRENCY_ASK="Ask";
        final String CURRENCY_BID="Bid";

        String name;
        String rate;
        long date;
        String time;
        String ask;
        String bid;




        try{

            JSONObject currencyJson = new JSONObject(currencyJsonStr);
            JSONObject query = currencyJson.getJSONObject(CURRENCY_QUERY);
            JSONObject results = query.getJSONObject(CURRENCY_RESULTS);
            JSONArray rateArray = results.getJSONArray(CURRENCY_RATE_ARRAY);

            Vector<ContentValues> currencyVector = new Vector<ContentValues>(rateArray.length());



            for(int counter=0;counter<rateArray.length();counter++){

                ContentValues currencyValues = new ContentValues();

                JSONObject rateObject = rateArray.getJSONObject(counter);
                name = rateObject.getString(CURRENCY_NAME);
                rate = rateObject.getString(CURRENCY_RATE);
                date = DateUtil.convert(rateObject.getString(CURRENCY_DATE));
                time = rateObject.getString(CURRENCY_TIME);
                ask = rateObject.getString(CURRENCY_ASK);
                bid = rateObject.getString(CURRENCY_BID);

                currencyValues.put(CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_NAME,name);
                currencyValues.put(CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_RATE,rate);
                currencyValues.put(CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_DATE,date);
                currencyValues.put(CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_TIME,time);
                currencyValues.put(CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_ASK,ask);
                currencyValues.put(CurrencyContract.CurrencyDataEntry.COLUMN_CURRENCY_BID,bid);

                currencyVector.add(currencyValues);

            }



        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }


    }
}
