package att.com.openweather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import att.com.openweather.forcast.WeatherAdapter;
import att.com.openweather.map.MapsActivity;
import att.com.openweather.model.Weather;
import att.com.openweather.networkController.RequestCallBack;
import att.com.openweather.networkController.Service;
import att.com.openweather.utils.JsonParsing;

public class ForcastActivity extends AppCompatActivity {

    private static int MAP_REQUEST_CODE=10;
    private ProgressBar mLoadingIndicator;
    WeatherAdapter adapter;
    RecyclerView mRecyclerView;
    private Parcelable mListState;

    RecyclerView.LayoutManager mLayoutManger;
    private String LIST_STATE_KEY = "listkey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forcast);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_forecast);
        adapter = new WeatherAdapter(this, null);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManger=new LinearLayoutManager(this);

        if (savedInstanceState!=null){
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            mLayoutManger.onRestoreInstanceState(mListState);
        }
        mRecyclerView.setLayoutManager(mLayoutManger);
    }


    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        // Save list state
        mListState = mLayoutManger.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // Retrieve list state and list/item positions
        if(state != null) {
            mListState = state.getParcelable(LIST_STATE_KEY);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListState != null) {
            mLayoutManger.onRestoreInstanceState(mListState);
        }

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

    private void loadWeatherData(LatLng latLng , final String locationName){

        //show loading
        showLoading();

        Service.getWeatherInfo(this, latLng.longitude, latLng.latitude, new RequestCallBack() {
            @Override
            public void success(String response) {
                Weather weather = JsonParsing.getWeather(response);
                weather.setCityName(locationName);
                adapter.setDataChange(weather);
                //hide loading
                showWeatherDataView();
            }

            @Override
            public void error(Exception exc) {
                Toast.makeText(getApplicationContext() , "error loadin data" , Toast.LENGTH_SHORT).show();
                //hide loading
                showWeatherDataView();
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
}
