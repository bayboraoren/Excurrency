package com.excurrency.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.excurrency.app.data.CurrencyContract;
import com.excurrency.app.service.CurrencyService;

import java.util.Currency;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    CurrencyAdapter mCurrencyAdapter;
    private static final int CURRENCY_LOADER = 0;

    public MainActivityFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURRENCY_LOADER , null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCurrencyAdapter = new CurrencyAdapter(getActivity(),null,0);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_currency);
        listView.setAdapter(mCurrencyAdapter);

        return rootView;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateCurrencyList();
        setSchedule();
    }

    private void setSchedule(){

        AlarmManager am=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(),CurrencyService.AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, i, 0);
        //20 minustes 1000 * 60 * 20
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000 * 60 * 5, pi); // Millisec * Second * Minute
    }

    private void updateCurrencyList() {
        Intent intent = new Intent(getActivity(), CurrencyService.class);
        getActivity().startService(intent);
    }


    void onCurrencyPropertyChanged( ) {
        updateCurrencyList();
        getLoaderManager().restartLoader(CURRENCY_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(),
                CurrencyContract.CurrencyDataEntry.buildCurrencyDataBySelectedCurrencyPropertyUri(true),
                null,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCurrencyAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCurrencyAdapter.swapCursor(null);
    }




}
