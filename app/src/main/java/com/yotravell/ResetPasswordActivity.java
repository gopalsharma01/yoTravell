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
import com.yotravell.VolleyService.AppController;
import com.yotravell.constant.WebServiceConstant;
import com.yotravell.interfaces.VolleyCallback;
import com.yotravell.networkUtils.InternetConnect;
import com.yotravell.utils.CommonUtils;
import com.yotravell.utils.SharedPrefrenceManager;
import com.yotravell.utils.ValidationUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtPassword,edtConfirmPassword;
    private TextView txtReset;
    private String strPassword,strConfirmPassword;
    private Integer intUserId;
    private LinearLayout linearBack;
    private Button btnResetSubmit;
    private Intent intentMsg;
    private ProgressDialog mProgressDialog;
    private ViewGroup resetPassForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        /*intentMsg = getIntent();
        String msg = intentMsg.getStringExtra("message");
        if(!msg.equals("")){
            CommonUtils.ShowToastMessages(ResetPasswordActivity.this,msg.toString());
        }*/
        resetPassForm = (ViewGroup)findViewById(R.id.resetPassForm);
        mProgressDialog = CommonUtils.ProgressBar(this, "");
        linearBack = (LinearLayout)findViewById(R.id.btnResetPassBack);
        txtReset = (TextView) findViewById(R.id.txtResetSubTitle);
        btnResetSubmit = (Button) findViewById(R.id.btnResetSubmit);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/brushScriptMT.ttf");
        txtReset.setTypeface(typeface);

        btnResetSubmit.setOnClickListener(this);
        linearBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==linearBack){
            Intent intent = new Intent(ResetPasswordActivity.this, ForgetPasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if(view==btnResetSubmit){
            CommonUtils.hideKeyboard(ResetPasswordActivity.this);
            resetPasswordAction();
        }
    }
    /**
     * when we click on forget password submit button, this function call on click.
     * @params none;
     * @return void;
     */
    private void resetPasswordAction(){
        edtPassword = (EditText) findViewById(R.id.edtResetPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtResetConfirmPassword);
        strPassword = edtPassword.getText().toString().trim();
        strConfirmPassword = edtConfirmPassword.getText().toString().trim();
        intUserId = SharedPrefrenceManager.getInstance(ResetPasswordActivity.this).getResetUserId();
        if(resetPasswordValidate()){
            CommonUtils.clearErrorFromView(resetPassForm);
            if(InternetConnect.isConnected(ResetPasswordActivity.this)) {
                mProgressDialog.show();
                resetWebService();
            }else{
                CommonUtils.showAlertMessage(ResetPasswordActivity.this,getString(R.string.error),getString(R.string.net_connection_error_title),getString(R.string.net_connection_error_msg),getString(R.string.net_connection_error_btn));
            }
        }
    }
    /**
     * this function use for validate registration form.
     * @params none;
     * @return boolean true/false;
     */
    private boolean resetPasswordValidate(){
        if(strPassword.equals("")){
            CommonUtils.setErrorOnView(edtPassword, getString(R.string.error_field_required));
        }else if(strConfirmPassword.equals("")){
            CommonUtils.setErrorOnView(edtConfirmPassword, getString(R.string.error_field_required));
        }else if(!strConfirmPassword.equals(strPassword)){
            CommonUtils.setErrorOnView(edtConfirmPassword, getString(R.string.error_match_password));
        }else{
            return true;
        }
        return false;
    }

    private Map<String, String> getParams(){
        Map<String, String> params = new HashMap<>();
        params.put("user_id", intUserId.toString());
        params.put("password", strPassword);
        return params;
    }
    private void resetWebService(){
        AppController.getInstance().callVollayWebService(Request.Method.POST, WebServiceConstant.RESETPASS_URL, getParams(), new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                mProgressDialog.dismiss();
                try {
                    //converting response to json object
                    if(response != null){
                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("status").equals("1")){
                            CommonUtils.clearForm(resetPassForm);
                            SharedPrefrenceManager.getInstance(ResetPasswordActivity.this).deleteResetUserId();
                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("message",obj.getString("message").toString());
                            startActivity(intent);
                        }else{
                            CommonUtils.showAlertMessage(ResetPasswordActivity.this,getString(R.string.error),getString(R.string.error),obj.getString("message"),getString(R.string.ok));
                        }
                    }else{
                        CommonUtils.showAlertMessage(ResetPasswordActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertMessage(ResetPasswordActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                }
            }
            @Override
            public void onErrorResponse(String result) {
                mProgressDialog.dismiss();
                CommonUtils.showAlertMessage(ResetPasswordActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
            }
        });
    }
}
