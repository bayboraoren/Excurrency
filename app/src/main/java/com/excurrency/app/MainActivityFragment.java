package com.excurrency.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.excurrency.app.data.CurrencyContract;
import com.excurrency.app.service.CurrencyService;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    CurrencyAdapter mCurrencyAdapter;
    private static final int CURRENCY_LOADER = 0;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";
    private ListView mListView;

    public MainActivityFragment() {


    }


    public interface Callback {
        void onItemSelected(Uri dateUri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURRENCY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCurrencyAdapter = new CurrencyAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview_currency);
        mListView.setAdapter(mCurrencyAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    /*Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .setData(CurrencyContract.CurrencyDataEntry.buildCurrencyDataById(cursor.getString(0)));
                    startActivity(intent);*/

                    ((Callback) getActivity())
                            .onItemSelected(CurrencyContract.CurrencyDataEntry.buildCurrencyDataById(cursor.getString(0)));

                }


                mPosition = position;
            }
        });


        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }


        return rootView;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateCurrencyList();
        setSchedule(Integer.parseInt(Utils.getSchedule(getActivity())));
    }

    private void setSchedule(int selectedScheduleTime) {

        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), CurrencyService.AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        //20 minustes 1000 * 60 * 20
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000 * 60 * selectedScheduleTime, pi); // Millisec * Second * Minute

    }

    private void updateCurrencyList() {
        Intent intent = new Intent(getActivity(), CurrencyService.class);
        getActivity().startService(intent);
    }


    void onCurrencyPropertyChanged() {
        setSchedule(Integer.parseInt(Utils.getSchedule(getActivity())));
        updateCurrencyList();
        getLoaderManager().restartLoader(CURRENCY_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(),
                CurrencyContract.CurrencyDataEntry.buildCurrencyDataBySelectedCurrencyPropertyUri(),
                null,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCurrencyAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCurrencyAdapter.swapCursor(null);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }
}
