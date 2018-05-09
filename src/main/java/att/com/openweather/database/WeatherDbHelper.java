package att.com.openweather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import att.com.openweather.model.Weather;

/**
 * control the data bases transactions insert , delete , select , update
 */
public class WeatherDbHelper extends SQLiteOpenHelper {

    /*data base name*/
    public static final String DATABASE_NAME = "weather.db";

    /*data base version*/
    private static final int DATABASE_VERSION = 1;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +
                        WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL," +
                        WeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_LON + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_LAT + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_WEATHER_STATE + " TEXT NOT NULL ," +
                        WeatherEntry.COLUMN_CITY_NAME + " TEXT NOT NULL " + " ) ; ";
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public long insert(Weather weather) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WeatherEntry.COLUMN_WEATHER_ID, weather.getWeatherId());
        contentValues.put(WeatherEntry.COLUMN_WEATHER_STATE, weather.getWeatherState());
        contentValues.put(WeatherEntry.COLUMN_CITY_NAME, weather.getCityName());


        contentValues.put(WeatherEntry.COLUMN_LON, weather.getLon());
        contentValues.put(WeatherEntry.COLUMN_LAT, weather.getLat());
        contentValues.put(WeatherEntry.COLUMN_MIN_TEMP, weather.getTempMin());
        contentValues.put(WeatherEntry.COLUMN_MAX_TEMP, weather.getTempMax());

        return db.insert(WeatherEntry.TABLE_NAME, null, contentValues);
    }


    public Cursor query() {
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = this.getReadableDatabase();

        Cursor retCursor;

        retCursor = db.query(WeatherEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        return retCursor;
    }

    public int delete(String id) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = this.getWritableDatabase();
        Log.d("delete" , id);
        return db.delete(WeatherEntry.TABLE_NAME, "_id=?", new String[]{id});
    }

    public int update(String id , Weather weather) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WeatherEntry.COLUMN_MIN_TEMP, weather.getTempMin());
        contentValues.put(WeatherEntry.COLUMN_MAX_TEMP, weather.getTempMax());
        contentValues.put(WeatherEntry.COLUMN_WEATHER_ID, weather.getWeatherId());
        contentValues.put(WeatherEntry.COLUMN_WEATHER_STATE, weather.getWeatherState());

        int result =db.update(WeatherEntry.TABLE_NAME ,
                contentValues ,
                "_id=?", new String[]{id});

        return result;
    }

}