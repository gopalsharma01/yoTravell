package com.yotravell.VolleyService;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yotravell.interfaces.VolleyCallback;
import com.yotravell.models.User;
import com.yotravell.utils.SharedPrefrenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Developer on 9/21/2017.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    public static User aSessionUserData;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    @Override
    public void onLowMemory() {
        Runtime.getRuntime().gc();
        super.onLowMemory();
    }

    /**
     * Get Session User Details
     * @param mCtx
     * @return User model object
     */
    public static void getSessionData(Context mCtx){
        aSessionUserData = SharedPrefrenceManager.getInstance(mCtx).getUserDetails();
    }
    /**
     * Function use to manage all parameters
     * @param aParamList ()
     * @return none
     */
    public static Map<String, String> getParams(Map<String, String> aParamList){
        Map<String, String> params = new HashMap<>();
        //params.put("user_id", String.valueOf(AppController.aSessionUserData.getId()));
        return params;
    }
    /**
     * Function use to call webService through Volley API
     * @param method (request method)
     * @param url (service url)
     * @param paramsData (service Parameters)
     * @param callback (volleyCallback object for response and error)
     * @return none
     */
    public void callVollayWebService(int method, String url, final Map<String, String> paramsData, final VolleyCallback callback){
        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccessResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onErrorResponse(error.getMessage());
                        /**
                         * handle Volley Error
                         */
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            /*showSnackBar(parentLayout, getString(R.string.internet_not_found), getString(R.string.retry), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    //handle retry button

                                }
                            });*/

                        } else if (error instanceof AuthFailureError) {
                        } else if (error instanceof ServerError) {
                        } else if (error instanceof NetworkError) {
                        } else if (error instanceof ParseError) {
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paramsData;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
