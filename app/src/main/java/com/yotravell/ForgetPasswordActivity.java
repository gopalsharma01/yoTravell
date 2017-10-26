package com.yotravell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yotravell.VolleyService.AppController;
import com.yotravell.constant.WebServiceConstant;
import com.yotravell.networkUtils.InternetConnect;
import com.yotravell.utils.CommonUtils;
import com.yotravell.utils.SharedPrefrenceManager;
import com.yotravell.utils.ValidationUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail;
    private TextView txtForget;
    private String strEmail;
    private LinearLayout linearBack;
    private Button btnForgetSubmit;
    private Intent intent;
    private ProgressDialog mProgressDialog;
    private ViewGroup forgotForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mProgressDialog = CommonUtils.ProgressBar(this, "");

        forgotForm = (ViewGroup)findViewById(R.id.forgotPassForm);
        linearBack = (LinearLayout)findViewById(R.id.btnBack);
        txtForget = (TextView) findViewById(R.id.txtForgetSubTitle);
        btnForgetSubmit = (Button) findViewById(R.id.btnForgetSubmit);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/brushScriptMT.ttf");
        txtForget.setTypeface(typeface);

        btnForgetSubmit.setOnClickListener(this);
        linearBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==linearBack){
            finish();
        }else if(view==btnForgetSubmit){
            CommonUtils.hideKeyboard(ForgetPasswordActivity.this);
            forgetPasswordAction();
        }
    }
    /**
     * when we click on forget password submit button, this function call on click.
     * @params none;
     * @return void;
     */
    private void forgetPasswordAction(){
        edtEmail = (EditText) findViewById(R.id.edtForgetEmail);
        strEmail = edtEmail.getText().toString().trim();
        if(forgetPasswordValidate()){
            CommonUtils.clearErrorFromView(forgotForm);
            if(InternetConnect.isConnected(getApplicationContext())) {
                mProgressDialog.show();
                forgetWebService();
            }else{
                CommonUtils.showAlertMessage(ForgetPasswordActivity.this,getString(R.string.error),getString(R.string.net_connection_error_title),getString(R.string.net_connection_error_msg),getString(R.string.net_connection_error_btn));
            }
        }
    }
    /**
     * this function use for validate registration form.
     * @params none;
     * @return boolean true/false;
     */
    private boolean forgetPasswordValidate(){
        if(strEmail.equals("")){
            CommonUtils.setErrorOnView(edtEmail, getString(R.string.error_field_required));
        }else if(!ValidationUtils.isEmailValid(strEmail)){
            CommonUtils.setErrorOnView(edtEmail, getString(R.string.error_invalid_email));
        }else{
            return true;
        }
        return false;
    }

    private void forgetWebService(){
        Log.e("Response in try  : ","call webservice");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServiceConstant.FORGOTPASS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        try {
                            Log.e("Response in try  : ",response);
                            //converting response to json object
                            if(response != null){
                                JSONObject obj = new JSONObject(response);
                                if(obj.getString("status").equals("1")){
                                    CommonUtils.clearForm(forgotForm);
                                    SharedPrefrenceManager.getInstance(ForgetPasswordActivity.this).setForgotEmail(strEmail);
                                    //finish();
                                    Intent intent = new Intent(ForgetPasswordActivity.this, ForgetOtpValidateActivity.class);
                                    intent.putExtra("message",obj.getString("message"));
                                    startActivity(intent);
                                }else{
                                    if(obj.has("email")) {
                                        CommonUtils.setErrorOnView(edtEmail, obj.getString("message"));
                                    }
                                    CommonUtils.showAlertMessage(ForgetPasswordActivity.this,getString(R.string.error),getString(R.string.error),obj.getString("message"),getString(R.string.ok));
                                    //CommonUtils.ShowToastMessages(ForgetPasswordActivity.this,obj.getString("message"));
                                }
                            }else{
                                //CommonUtils.ShowToastMessages(ForgetPasswordActivity.this,getString(R.string.error_message));
                                CommonUtils.showAlertMessage(ForgetPasswordActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                            }
                            //Log.e("Response in try  : ",response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            //Log.e("tag","error");
                            CommonUtils.showAlertMessage(ForgetPasswordActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.dismiss();
                        Log.e("Response in try  : ","rerror in response");

                        CommonUtils.showAlertMessage(ForgetPasswordActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                        //CommonUtils.ShowToastMessages(ForgetPasswordActivity.this,error.getMessage()+" service error  ");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", strEmail);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
