package att.com.openweather;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import att.com.openweather.database.WeatherDbHelper;
import att.com.openweather.forcast.WeatherAdapter;
import att.com.openweather.map.MapsActivity;
import att.com.openweather.model.Weather;
import att.com.openweather.networkController.RequestCallBack;
import att.com.openweather.networkController.Service;
import att.com.openweather.utils.JsonParsing;
import att.com.openweather.utils.SyncWeatherData;

public class ForcastActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TASK_LOADER_ID = 2;
    private static int MAP_REQUEST_CODE=10;
    private ProgressBar mLoadingIndicator;
    private WeatherAdapter adapter;
    private TextView emptyView;
    private RecyclerView mRecyclerView;
    private SyncWeatherData syncWeatherData ;
    private RecyclerView.LayoutManager mLayoutManger;
    private WeatherDbHelper weatherDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forcast);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_forecast);
        emptyView = (TextView)findViewById(R.id.empty_view);
        adapter = new WeatherAdapter(this);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManger=new LinearLayoutManager(this);


        mRecyclerView.setLayoutManager(mLayoutManger);
        weatherDbHelper=new WeatherDbHelper(this);

        /*sync weather data*/
        syncWeatherData=new SyncWeatherData();
        syncWeatherData.syncWeather(this , getSupportLoaderManager());

        this.getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        restartLoader();
    }

    public void restartLoader(){
        this.getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);

    }

    public void addlocation(View view) {
        Intent intent = new Intent(this , MapsActivity.class);
        startActivityForResult(intent ,MAP_REQUEST_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAP_REQUEST_CODE && resultCode==RESULT_OK){
            LatLng newLocation = (LatLng) data.getExtras().get("newlocation");
            String locationName = data.getStringExtra("locationname");
            loadWeatherData(newLocation , locationName);

        }
    }

    /**
     * load and insert weather data for specific location
     * @param latLng
     * @param locationName
     */
    private void loadWeatherData(LatLng latLng , final String locationName){

        showLoading();
        Service.getWeatherInfo(this, latLng.longitude, latLng.latitude, new RequestCallBack() {
            @Override
            public void success(String response) {
                Weather weather = JsonParsing.getWeather(response);
                //insert to data base
                weather.setCityName(locationName);
                weatherDbHelper.insert(weather);
                //hide loading
                restartLoader();
            }

            @Override
            public void error(Exception exc) {
                Toast.makeText(getApplicationContext() , "error loading data" , Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * show data and hide loading
     */
    private void showWeatherDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * show loading and hide RecyclerView
     */
    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        showLoading();
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mWeatherData = null;
            @Override
            protected void onStartLoading() {
                if (mWeatherData != null) {
                    deliverResult(mWeatherData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return weatherDbHelper.slectAll();

                } catch (Exception e) {
                    Log.e(this.getClass().getName(), "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }
            public void deliverResult(Cursor data) {
                mWeatherData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        adapter.swapCursor(cursor);
        if (cursor==null || cursor.getCount()==0 ){
            mRecyclerView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        }else{
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
        }
        showWeatherDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
