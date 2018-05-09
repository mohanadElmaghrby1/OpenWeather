package att.com.openweather.forcast;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import att.com.openweather.R;
import att.com.openweather.model.Weather;
import att.com.openweather.utils.WeatherUtils;

public class WeatherAdapter  extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    Context mContext ;
    public ArrayList<Weather> locationsWeather ;



    public WeatherAdapter (Context context, ArrayList<Weather> locationsWeather) {
        this.mContext = context;
        this.locationsWeather=locationsWeather;

    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.forecast_list_item, parent, false);
        WeatherViewHolder viewHolder = new WeatherViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( WeatherViewHolder holder, int position) {

        Weather weather = locationsWeather.get(position);

        int weatherId= weather.getWeatherId();
        int weatherIConId = WeatherUtils.getSmallArtResourceIdForWeatherCondition(weatherId);
        holder.weatherIcon.setImageResource(weatherIConId);

        float min =weather.getTempMin();
        float max=weather.getTempMin();

        holder.tempMin.setText(((int)min)+"\u00b0");
        holder.tempMax.setText(((int)max)+"\u00b0");

        holder.weatherState.setText(weather.getWeatherState());
        holder.cityName.setText(weather.getCityName());

    }

    @Override
    public int getItemCount() {
        if (locationsWeather==null)
            return  0;

        return locationsWeather.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder{

        ImageView weatherIcon;
        TextView cityName , weatherState ;
        TextView tempMin , tempMax;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            weatherIcon = (ImageView)itemView.findViewById(R.id.weather_icon);
            cityName = (TextView)itemView.findViewById(R.id.city_name);
            weatherState = (TextView)itemView.findViewById(R.id.weather_description);
            tempMax = (TextView)itemView.findViewById(R.id.high_temperature);
            tempMin =(TextView)itemView.findViewById(R.id.low_temperature);

        }
    }



    public void setDataChange(Weather weather){
        if (locationsWeather==null){
            locationsWeather = new ArrayList<>();
            locationsWeather.add(weather);
        }else {
            locationsWeather.add(weather);
        }
        notifyDataSetChanged();
    }


}
