package att.com.openweather.map;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import att.com.openweather.R;


/**
 * display map and select locations
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private static String TAG = "Maps Activity";
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    LatLng newLocation = null;
    private String location ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        /*google places auto complete */
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                newLocation = place.getLatLng();
                mMap.clear();
                location=place.getName().toString();
                addMarker(place);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Toast.makeText(getApplicationContext() , "long press for selecting location",Toast.LENGTH_LONG).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        //enable my location
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(this);

    }

    /**
     * add marker on specific location
     * @param place : to mark
     */
    private void addMarker(Place place){
        LatLng marker = place.getLatLng();
        mMap.addMarker(new MarkerOptions().position(marker).title(place.getName()+""));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker,50));


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                addMarker(place);
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));
        newLocation = latLng;
        showInputDialog();

    }

    public void submitLocation(View view) {
        if (newLocation != null){
            Intent intent = new Intent();
            intent.putExtra("newlocation" , newLocation);
            intent.putExtra("locationname",location);
            setResult(Activity.RESULT_OK,intent );
            finish();
        } else{
            Toast.makeText(getApplicationContext() , "Please select location before submitting !", Toast.LENGTH_LONG).show();
        }
    }


    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("name the location please ");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                location = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                mMap.clear();
            }
        });

        builder.show();
    }
}
