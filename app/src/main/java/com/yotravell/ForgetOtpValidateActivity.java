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

public class ForgetOtpValidateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtForgetOtp;
    private TextView txtForgetOtp;
    private String strOTP,strEmail;
    private LinearLayout linearBack;
    private Button btnForgetOtpSubmit;
    private Intent intentMsg;
    private ProgressDialog mProgressDialog;
    private ViewGroup forgotOTPForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_otp_validate);

        intentMsg = getIntent();
        String msg = intentMsg.getStringExtra("message");
        if(!msg.equals("")){
            CommonUtils.showAlertMessage(ForgetOtpValidateActivity.this,getString(R.string.error),getString(R.string.otp_title),msg.toString(),getString(R.string.ok));
            //CommonUtils.ShowToastMessages(ForgetOtpValidateActivity.this,msg.toString());
        }

        forgotOTPForm = (ViewGroup)findViewById(R.id.forgotPassOTPForm);
        mProgressDialog = CommonUtils.ProgressBar(this, "");
        linearBack = (LinearLayout)findViewById(R.id.btnForgetOtpBack);
        txtForgetOtp = (TextView) findViewById(R.id.txtForgetOtpSubTitle);
        btnForgetOtpSubmit = (Button) findViewById(R.id.btnForgetOtpSubmit);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/brushScriptMT.ttf");
        txtForgetOtp.setTypeface(typeface);

        btnForgetOtpSubmit.setOnClickListener(this);
        linearBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==linearBack){
            finish();
        }else if(view==btnForgetOtpSubmit){
            CommonUtils.hideKeyboard(ForgetOtpValidateActivity.this);
            forgetPasswordOTPAction();
        }
    }
    /**
     * when we click on forget password submit button, this function call on click.
     * @params none;
     * @return void;
     */
    private void forgetPasswordOTPAction(){
        edtForgetOtp = (EditText) findViewById(R.id.edtForgetOtp);
        strOTP = edtForgetOtp.getText().toString().trim();
        strEmail = SharedPrefrenceManager.getInstance(ForgetOtpValidateActivity.this).getForgotEmail();

        if(forgetPasswordOTPValidate()){
            CommonUtils.clearErrorFromView(forgotOTPForm);
            if(InternetConnect.isConnected(getApplicationContext())) {
                mProgressDialog.show();
                forgetOTPWebService();
            }else{
                CommonUtils.showAlertMessage(ForgetOtpValidateActivity.this,getString(R.string.error),getString(R.string.net_connection_error_title),getString(R.string.net_connection_error_msg),getString(R.string.net_connection_error_btn));
            }
        }
    }
    /**
     * this function use for validate registration form.
     * @params none;
     * @return boolean true/false;
     */
    private boolean forgetPasswordOTPValidate(){
        if(strOTP.equals("")){
            CommonUtils.setErrorOnView(edtForgetOtp, getString(R.string.error_field_required));
        }else{
            return true;
        }
        return false;
    }
    private Map<String, String> getParams(){
        Map<String, String> params = new HashMap<>();
        params.put("otp", strOTP);
        params.put("email", strEmail);
        return params;
    }
    private void forgetOTPWebService(){
        AppController.getInstance().callVollayWebService(Request.Method.POST, WebServiceConstant.FORGOTOTP_URL, getParams(), new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                mProgressDialog.dismiss();
                try {
                    //converting response to json object
                    if(response != null){
                        //JSONObject obj = new JSONObject(response);
                        Gson gson = new Gson();
                        ResponseModel responseData =  gson.fromJson(response, ResponseModel.class);
                        if(responseData.getStatus().toString().equals("1")){
                            CommonUtils.clearForm(forgotOTPForm);
                            SharedPrefrenceManager.getInstance(ForgetOtpValidateActivity.this).deleteForgotEmail();
                            SharedPrefrenceManager.getInstance(ForgetOtpValidateActivity.this).setResetUserId(responseData.getUserId());
                            Intent intent = new Intent(ForgetOtpValidateActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("message",responseData.getMessage().toString());
                            startActivity(intent);
                        }else{
                            CommonUtils.setErrorOnView(edtForgetOtp, responseData.getMessage().toString());
                            CommonUtils.showAlertMessage(ForgetOtpValidateActivity.this,getString(R.string.error),getString(R.string.error),responseData.getMessage(),getString(R.string.ok));
                        }
                    }else{
                        CommonUtils.showAlertMessage(ForgetOtpValidateActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertMessage(ForgetOtpValidateActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                }
            }
            @Override
            public void onErrorResponse(String result) {
                mProgressDialog.dismiss();
                CommonUtils.showAlertMessage(ForgetOtpValidateActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
            }
        });
    }
}
