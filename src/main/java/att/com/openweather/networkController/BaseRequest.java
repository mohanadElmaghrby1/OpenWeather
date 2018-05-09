package att.com.openweather.networkController;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohanad on 31/07/17.
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

    /**
     * request tha api with Post method
     *
     * @param url      : API URL
     * @param context  :  application context
     * @param paramas  : the POST method paramas
     * @param callBack : the interface callback to notify
     */
    static void doPost(String url, Context context, final HashMap<String, String> paramas, final RequestCallBack callBack) {

        //show progress dialog
        //progess dialgo
        final ProgressDialog pDialog;
        // Showing progress dialog before sending http request
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading..");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();


        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        callBack.success(response);
                        Log.i("Response", response);
                        //dismiss the progress dialog
//                        pDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        callBack.error(error);
                        Log.e("Error.Response", error + "");
                        //dismiss the progress dialog
//                        pDialog.dismiss();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                return paramas;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Add the request to the queue
        Volley.newRequestQueue(context).add(stringRequest);
    }

    /**
     * request tha api with Post method
     *
     * @param url      : API URL
     * @param context  :  application context
     * @param callBack : the interface callback to notify
     */
    static void doPost(String url, Context context, JSONObject jsonObject, final RequestCallBack callBack) {

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        callBack.success(jsonObject.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callBack.error(volleyError);
                    }
                });

        queue.add(jobReq);
    }


}
