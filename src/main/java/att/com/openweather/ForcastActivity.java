package att.com.openweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import att.com.openweather.map.MapsActivity;

public class ForcastActivity extends AppCompatActivity {

    private static int MAP_REQUEST_CODE=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forcast);
    }

    public void addlocation(View view) {
        Intent intent = new Intent(this , MapsActivity.class);
        startActivityForResult(intent ,MAP_REQUEST_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAP_REQUEST_CODE && requestCode==RESULT_OK){

        }
    }
}
