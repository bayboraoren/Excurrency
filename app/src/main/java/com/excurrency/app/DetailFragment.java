package com.excurrency.app;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.excurrency.app.data.CurrencyContract;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by bora on 20.07.2015.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri detailUri;
    static final String DETAIL_URI = "URI";
    private static final int DETAIL_LOADER = 0;

    private ImageView mFlagIconView;
    private TextView mDay;
    private TextView mMonthAndDate;
    private TextView mCurrencyCountry;
    private TextView mCurrencyName;
    private TextView mCurrencyRate;
    private TextView mCurrencyCode;
    private TextView mCurrencyAsk;
    private TextView mCurrencyBid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            detailUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mFlagIconView = (ImageView) rootView.findViewById(R.id.flag_icon);
        mDay = (TextView) rootView.findViewById(R.id.day);
        mMonthAndDate = (TextView) rootView.findViewById(R.id.month_and_date);
        mCurrencyCountry = (TextView) rootView.findViewById(R.id.currency_country);
        mCurrencyName = (TextView) rootView.findViewById(R.id.currency_name);
        mCurrencyRate = (TextView) rootView.findViewById(R.id.currency_rate);
        mCurrencyCode = (TextView) rootView.findViewById(R.id.currency_code);
        mCurrencyAsk = (TextView) rootView.findViewById(R.id.currency_ask);
        mCurrencyBid = (TextView) rootView.findViewById(R.id.currency_bid);

        return rootView;


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if ( null != detailUri ) {
            return new CursorLoader(
                    getActivity(),
                    detailUri,
                    null,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor.getCount()>0) {
            //TODO repeating, put utils
            cursor.moveToFirst();
            Resources resources = getActivity().getResources();
            int currencyCountryIndex = cursor.getColumnIndex(CurrencyContract.CurrencyPropertyEntry.COLUMN_CURRENCY_COUNTRY);
            final int resourceId = resources.getIdentifier(cursor.getString(currencyCountryIndex), "drawable",
                    getActivity().getPackageName());

            mFlagIconView.setImageResource(resourceId);

            Calendar calendar = Calendar.getInstance();
            String calendarStr = String.format("%1$tA", calendar);

            mDay.setText(calendarStr);
            mMonthAndDate.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + ", " + calendar.get(Calendar.DATE));

            mCurrencyCountry.setText(cursor.getString(9).replaceAll("_", " ").toUpperCase());
            mCurrencyName.setText(cursor.getString(10));

            mCurrencyRate.setText(cursor.getString(3) + " ");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String toCurrencyCode = prefs.getString(getActivity().getString(R.string.pref_currency_convert_to_key), getActivity().getString(R.string.pref_currency_convert_to_default));

            mCurrencyCode.setText(toCurrencyCode);

            mCurrencyAsk.setText(getString(R.string.ask) + " " + cursor.getString(5));
            mCurrencyBid.setText(getString(R.string.bid) + " " + cursor.getString(6));
        }

    }

    void onCurrencyPropertyChanged() {
        if(detailUri!=null) {
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("detailUri",detailUri);
    }



}
