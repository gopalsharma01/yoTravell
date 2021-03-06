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
import com.google.gson.Gson;
import com.yotravell.VolleyService.AppController;
import com.yotravell.constant.WebServiceConstant;
import com.yotravell.interfaces.VolleyCallback;
import com.yotravell.models.ResponseModel;
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
    private Map<String, String> getParams(){
        Map<String, String> params = new HashMap<>();
        params.put("email", strEmail);
        return params;
    }
    private void forgetWebService(){
        AppController.getInstance().callVollayWebService(Request.Method.POST, WebServiceConstant.FORGOTPASS_URL, getParams(), new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                mProgressDialog.dismiss();
                try {
                    Log.e("Response in try  : ",response);
                    //converting response to json object
                    if(response != null){
                        Gson gson = new Gson();
                        ResponseModel responseData =  gson.fromJson(response, ResponseModel.class);
                        //JSONObject obj = new JSONObject(response);
                        if(responseData.getStatus().toString().equals("1")){//obj.getString("status").equals("1")
                            CommonUtils.clearForm(forgotForm);
                            SharedPrefrenceManager.getInstance(ForgetPasswordActivity.this).setForgotEmail(strEmail);
                            //finish();
                            Intent intent = new Intent(ForgetPasswordActivity.this, ForgetOtpValidateActivity.class);
                            intent.putExtra("message",responseData.getMessage());
                            startActivity(intent);
                        }else{
                            if(!responseData.getEmail().equals("")) {//obj.has("email")
                                CommonUtils.setErrorOnView(edtEmail, responseData.getMessage());
                            }
                            CommonUtils.showAlertMessage(ForgetPasswordActivity.this,getString(R.string.error),getString(R.string.error),responseData.getMessage(),getString(R.string.ok));
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
            @Override
            public void onErrorResponse(String result) {
                mProgressDialog.dismiss();
                Log.e("Response in try  : ","rerror in response");

                CommonUtils.showAlertMessage(ForgetPasswordActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
            }
        });
    }
}
