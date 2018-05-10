package att.com.openweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import att.com.openweather.database.WeatherDbHelper;
import att.com.openweather.database.WeatherEntry;
import att.com.openweather.model.Weather;
import att.com.openweather.networkController.RequestCallBack;
import att.com.openweather.networkController.Service;

/**
 * sync weather data if not updated for more than 4 hours
 */
public class SyncWeatherData implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String MY_PREF="LastUpdateTime";
    private static String UPDATE_KEY="update_key";
    static WeatherDbHelper weatherDbHelper;
    private static final int TASK_LOADER_ID = 0;
    public Context mcContext;



    public  synchronized void syncWeather(Context context , LoaderManager loaderManager){
        mcContext=context;
        SharedPreferences sh = context.getSharedPreferences(MY_PREF , Context.MODE_PRIVATE);
        long lastUpdate =sh.getLong(UPDATE_KEY ,System.currentTimeMillis()-14400000 );

        /*update very 4 hours whe app active
        * check for last update time if updated before 4 hours , update again*/
        if (System.currentTimeMillis()-lastUpdate>=14400000){
            //sync
            weatherDbHelper = new WeatherDbHelper(context);
            loaderManager.initLoader(TASK_LOADER_ID, null, this);
            //update syn time
            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE).edit();
            editor.putLong(UPDATE_KEY,System.currentTimeMillis() );
            editor.commit();

        }


    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(mcContext) {

            // Initialize a Cursor, this will hold all the weather data
            Cursor mWeatherData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mWeatherData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mWeatherData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all weather data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return weatherDbHelper.slectAll();

                } catch (Exception e) {
                    Log.e(this.getClass().getName(), "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mWeatherData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {

        //iterate over saved locations and update their temp
        while (cursor.moveToNext()){
            double lat = cursor.getDouble(cursor.getColumnIndex(WeatherEntry.COLUMN_LAT));
            double lon =cursor.getDouble(cursor.getColumnIndex(WeatherEntry.COLUMN_LON));
            final int id = cursor.getInt(cursor.getColumnIndex(WeatherEntry._ID));

            //update date from api
            Service.getWeatherInfo(mcContext, lon, lat, new RequestCallBack() {
                @Override
                public void success(String response) {
                    Weather weather = JsonParsing.getWeather(response);
                    weatherDbHelper.update(id+"" , weather);
                }

                @Override
                public void error(Exception exc) {

                }
            });

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

