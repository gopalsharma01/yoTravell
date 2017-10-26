package com.yotravell.VolleyService;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.yotravell.models.User;
import com.yotravell.utils.SharedPrefrenceManager;

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
}
