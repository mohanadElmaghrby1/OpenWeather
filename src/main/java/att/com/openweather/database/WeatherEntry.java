package att.com.openweather.database;

import android.provider.BaseColumns;

/**
 * contract class for weather data base schema 
 */
public class WeatherEntry implements BaseColumns {

    /*table name*/
    public static final String TABLE_NAME = "weather";


    /* Weather ID as returned by API, used to identify the icon to be used */
    public static final String COLUMN_WEATHER_ID = "weather_id";

    /* Min and max temperatures in °C for the day */
    public static final String COLUMN_MIN_TEMP = "min";
    public static final String COLUMN_MAX_TEMP = "max";

    /* Min and max temperatures in °C for the day */
    public static final String COLUMN_LON = "lon";
    public static final String COLUMN_LAT = "lat";


    /* weather state   */
    public static final String COLUMN_WEATHER_STATE = "weather_state";

    /* city name  */
    public static final String COLUMN_CITY_NAME = "city_name";


}
