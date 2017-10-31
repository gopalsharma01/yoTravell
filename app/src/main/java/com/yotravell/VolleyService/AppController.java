package com.yotravell.VolleyService;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yotravell.interfaces.VolleyCallback;
import com.yotravell.models.User;
import com.yotravell.utils.SharedPrefrenceManager;

import java.util.HashMap;
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
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params = paramsData;
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
