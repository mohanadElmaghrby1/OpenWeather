package att.com.openweather.forcast;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import att.com.openweather.ForcastActivity;
import att.com.openweather.R;
import att.com.openweather.database.WeatherDbHelper;
import att.com.openweather.database.WeatherEntry;
import att.com.openweather.utils.WeatherUtils;

/**
 * adapter class for weather RecyclerView
 */
public class WeatherAdapter  extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    Context mContext ;
    Cursor mCursor;



    public WeatherAdapter (Context context) {
        this.mContext = context;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.forecast_list_item, parent, false);
        WeatherViewHolder viewHolder = new WeatherViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( WeatherViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(mCursor.getColumnIndex(WeatherEntry._ID));
        holder.itemView.setTag(id);
        int weatherId = mCursor.getInt(mCursor.getColumnIndex(WeatherEntry.COLUMN_WEATHER_ID));
        int weatherIConId = WeatherUtils.getSmallArtResourceIdForWeatherCondition(weatherId);
        holder.weatherIcon.setImageResource(weatherIConId);

        float min =mCursor.getFloat(mCursor.getColumnIndex(WeatherEntry.COLUMN_MIN_TEMP));
        float max=mCursor.getFloat(mCursor.getColumnIndex(WeatherEntry.COLUMN_MAX_TEMP));

        holder.tempMin.setText(((int)min)+"\u00b0");
        holder.tempMax.setText(((int)max)+"\u00b0");

        String weatherStat =mCursor.getString(mCursor.getColumnIndex(WeatherEntry.COLUMN_WEATHER_STATE));
        holder.weatherState.setText(weatherStat);

        String cityName = mCursor.getString(mCursor.getColumnIndex(WeatherEntry.COLUMN_CITY_NAME));
        holder.cityName.setText(cityName);

        /* to make delete option*/
        holder.menuOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, view);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_options);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId= item.getItemId();
                        if (itemId == R.id.action_delete){

                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            new WeatherDbHelper(mContext).delete(id+"");
                                            if (mContext instanceof ForcastActivity){
                                                ((ForcastActivity)mContext).restartLoader();
                                            }
                                            dialog.dismiss();
                                            Toast.makeText(mContext , "location deleted" ,Toast.LENGTH_SHORT ).show();
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setMessage("Are you sure you want to delete this location ?").setPositiveButton("Delete", dialogClickListener)
                                    .setNegativeButton("Cancel", dialogClickListener).show();

                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mCursor==null)
            return  0;

        return mCursor.getCount();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder{

        ImageView weatherIcon;
        TextView cityName , weatherState ;
        TextView tempMin , tempMax;
         TextView menuOptions;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            weatherIcon = (ImageView)itemView.findViewById(R.id.weather_icon);
            cityName = (TextView)itemView.findViewById(R.id.city_name);
            weatherState = (TextView)itemView.findViewById(R.id.weather_description);
            tempMax = (TextView)itemView.findViewById(R.id.high_temperature);
            menuOptions = (TextView) itemView.findViewById(R.id.btn_menu);
            tempMin =(TextView)itemView.findViewById(R.id.low_temperature);

        }
    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

}
