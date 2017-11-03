package com.yotravell.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yotravell.R;

/**
 * Created by Developer on 9/12/2017.
 */

public class CommonUtils {

    public static void showAlertMessage(final Context mCtx,String strType,String strTitle,String strMsg,String strBtn){
        AlertDialog alertDialog = new AlertDialog.Builder(mCtx).create();

        // Setting Dialog Title
        //alertDialog.setTitle(strTitle);
        TextView title =  new TextView(mCtx);
        title.setText(strTitle);
        //title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setPadding(20,20,0,20);
        //title.setBackgroundColor(Color.GRAY);
        title.setTextColor(Color.BLACK);
        alertDialog.setCustomTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(strMsg);
        // Setting Icon to Dialog
        if(strType.trim().toLowerCase().equals("error")){
            //alertDialog.setIcon(R.drawable.ic_cross);
        }else{
            alertDialog.setIcon(R.drawable.ic_tick);
        }
        // Setting OK Button
        alertDialog.setButton(strBtn.toString().trim().toUpperCase(), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int which)
            {
                // Write your code here to execute after dialog closed
                //Toast.makeText(mCtx,"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    /**
     * Show input error message at that point
     * @param mEdt : input field
     * @param MsgStr : message string which we want to show
     */
    public static void setErrorOnView(EditText mEdt, String MsgStr){
        mEdt.setError(MsgStr);
        mEdt.requestFocus();
    }

    public static void removeErrorOnView(EditText mEdt){
        mEdt.setError(null);
        mEdt.clearFocus();
    }
    public static void clearErrorFromView(ViewGroup group){
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setError(null);
                ((EditText)view).clearFocus();
            }
            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0)) {
                clearForm((ViewGroup) view);
            }
        }
    }

    /**
     * progress dialog box
     * @param mCtx
     * @param MsgStr
     */
    public static ProgressDialog ProgressBar(Context mCtx, String MsgStr){
        ProgressDialog progressBar = new ProgressDialog(mCtx);
        progressBar.setMessage("Loading....");
        progressBar.setCancelable(false);
        return progressBar;
    }
    /**
     * Show all type message which comes after execution
     * @param mCtx
     * @param MsgStr
     */
    public static void ShowToastMessages(Context mCtx, String MsgStr){
        Toast.makeText(mCtx, MsgStr, Toast.LENGTH_SHORT).show();
    }
    /**
     * Get device token
     * @param mCtx
     * @return String
     */
    public static String getDeviceToken(Context mCtx){
        return Settings.Secure.getString(mCtx.getContentResolver(),Settings.Secure.ANDROID_ID);
    }
    /**
     * Get DeviceName
     * @return string
     */
    public static String getDeviceName() {

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        String DeviceName = android.os.Build.DEVICE;
        return DeviceName+" "+manufacturer+" "+model+" "+product;
        /*
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }*/
    }
    /**
     * Hide open keybord
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /**
     * Clear form data after use
     * @param group
     */
    public static void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).getText().clear();
            }
            if (view instanceof Spinner) {
                ((Spinner) view).setSelection(0);
            }
            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0)) {
                clearForm((ViewGroup) view);
            }
        }
    }
    /**
     * Get Device window height and width
     */
    public static int getScreenWidth()
    {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public static int getScreenHeight()
    {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
