package att.com.openweather.networkController;

import android.content.Context;

/**
 * Created by mohanad on 31/07/17.
 */

public class Service {

    /** the Back end  API url*/
    private static String BackEND_API_URL="http://api.openweathermap.org/data/2.5/weather?";

    /** api key*/
    private static String API_KEY="1a2925c2ef4932ef4349fec8d35ca3cd";

    private static String LAT_PARAM="lat";

    private static String LON_PARAM="lon";

    private static String APIKEY_PARAM="appid";





    public static void getWeatherInfo(Context context , double lon , double lat , RequestCallBack callback){
        String ur=BackEND_API_URL+LAT_PARAM+"="+lat+"&"+LON_PARAM+"="+lon+"&"+APIKEY_PARAM+"="+API_KEY;
        BaseRequest.doGet(ur
                ,context   ,callback);

    }



}
