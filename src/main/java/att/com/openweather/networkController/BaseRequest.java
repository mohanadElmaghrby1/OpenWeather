package att.com.openweather.networkController;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * base request methods
 */

public class BaseRequest {

    private static final String LOG_TAG = "BaseRequest";

    /**
     * request tha api with GET method
     *
     * @param url      : API URL
     * @param context  :  application context
     * @param callBack : the interface callback to notify
     */
    static void doGet(String url, Context context, final RequestCallBack callBack) {


        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //notify callback for success
                        callBack.success(response);
                        Log.v(LOG_TAG,response+"jjj");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v(LOG_TAG,error.toString()+"jjj");

                    }
                }
        )
        ;
        // Add the request to the queue
        Volley.newRequestQueue(context).add(stringRequest);
    }

}
