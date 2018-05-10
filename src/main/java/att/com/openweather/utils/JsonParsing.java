package att.com.openweather.utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import att.com.openweather.model.Weather;

/**
 * help parsing json to weather object
 */
public class JsonParsing {

    /**
     *convert json to weather pojo
     * @param json response
     * @return weather object from json
     */
    public static Weather getWeather(String json){
        if (json == null)
            return null ;

        Weather weather = new Weather();
        try {
            JSONObject root= new JSONObject(json);

            JSONObject lonlat = root.getJSONObject("coord");
            double lon = lonlat.getDouble("lon");
            double lat = lonlat.getDouble("lat");
            JSONArray weatherarr = root.getJSONArray("weather");
            JSONObject weatherObj = weatherarr.getJSONObject(0);
            String cityName = root.getString("name");
            int weatherID = weatherObj.getInt("id");
            String weatherState = weatherObj.getString("main");

            JSONObject temp = root.getJSONObject("main");
            float tempMin = (float) temp.getDouble("temp_min");
            float tempMax = (float) temp.getDouble("temp_max");

            //convert to c
            tempMin-=273.15f;
            tempMax-=273.15f;

            weather.setCityName(cityName);
            weather.setWeatherId(weatherID);
            weather.setTempMin(tempMin);
            weather.setTempMax(tempMax);
            weather.setLat(lat);
            weather.setLon(lon);
            weather.setWeatherState(weatherState);

            return  weather;

        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }


    }
}
