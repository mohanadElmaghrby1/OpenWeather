package att.com.openweather.model;

/**
 * model class for returned weather data
 */
public class Weather {

    private double lon;
    private double lat;
    private String weatherState;
    private int weatherId;
    private float tempMin;
    private float tempMax;
    private String CityName;

    public Weather(double lon, double lat, String weatherState,int weatherId,
                   float tempMin, float tempMax, String cityName) {
        this.lon = lon;
        this.lat = lat;
        this.weatherState = weatherState;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.weatherId=weatherId;
        CityName = cityName;
    }

    public Weather() {

    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getWeatherState() {
        return weatherState;
    }

    public void setWeatherState(String weatherState) {
        this.weatherState = weatherState;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public int getWeatherId() {
        return weatherId;
    }
}
