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
import com.yotravell.models.User;
import com.yotravell.networkUtils.InternetConnect;
import com.yotravell.utils.CommonUtils;
import com.yotravell.utils.SharedPrefrenceManager;
import com.yotravell.utils.ValidationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{

    private EditText edtLogUsername,edtLogPass;
    private TextView txtForgot,txtReg;
    private Button btnLoginSubmit;
    private Intent intentActivity,intentMsg;
    private ViewGroup loginForm;
    private ProgressDialog mProgressDialog;

    private String strLogUsername,strLogPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*intentMsg = getIntent();
        String msg = intentMsg.getStringExtra("message");
        if(!msg.equals("")){
            CommonUtils.ShowToastMessages(LoginActivity.this,msg.toString());
        }*/

        //Link
        txtForgot = (TextView) findViewById(R.id.txtLoginForgot);
        txtReg = (TextView)findViewById(R.id.txtLoginReg);
        //Sing In Button
        btnLoginSubmit = (Button)findViewById(R.id.btnLoginSubmit);

        txtReg.setOnClickListener(this);
        txtForgot.setOnClickListener(this);
        btnLoginSubmit.setOnClickListener(this);

        mProgressDialog = CommonUtils.ProgressBar(this, "");

        loginForm = (ViewGroup) findViewById(R.id.loginForm);

        TextView txtLoginSubTitle = (TextView) findViewById(R.id.txtLoginSubTitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/brushScriptMT.ttf");
        txtLoginSubTitle.setTypeface(typeface);
    }

    @Override
    public void onClick(View view) {
        if(view==txtForgot){
            intentActivity = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
            startActivity(intentActivity);
        }else if(view==txtReg){
            intentActivity = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intentActivity);
        }else if(view == btnLoginSubmit) {
            //finish();
            //startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            CommonUtils.hideKeyboard(LoginActivity.this);
            loginAction();
        }
    }
    /**
     * when we click on login submit button, this function call on click.
     * @params none;
     * @return void;
     */
    private void loginAction(){
        edtLogUsername = (EditText)findViewById(R.id.editLoginUsername);
        edtLogPass = (EditText)findViewById(R.id.editLoginPassword);

        strLogUsername = edtLogUsername.getText().toString().trim();
        strLogPass = edtLogPass.getText().toString().trim();
        //CommonUtils.ShowToastMessages(LoginActivity.this," device token "+CommonUtils.getDeviceToken(getApplicationContext()));
        if(loginValidate()){
            CommonUtils.clearErrorFromView(loginForm);
            if(InternetConnect.isConnected(LoginActivity.this)) {
                mProgressDialog.show();
                loginWebService();
                /*finish();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));*/
            }else{
                CommonUtils.showAlertMessage(LoginActivity.this,getString(R.string.error),getString(R.string.net_connection_error_title),getString(R.string.net_connection_error_msg),getString(R.string.net_connection_error_btn));
            }
        }
    }
    /**
     * this function use for validate login form.
     * @params none;
     * @return boolean true/false;
     */
    private boolean loginValidate(){
        if(strLogUsername.equals("")){
            CommonUtils.setErrorOnView(edtLogUsername, getString(R.string.error_field_required));
        }else if(!ValidationUtils.isUsernameValid(strLogUsername)){
            CommonUtils.setErrorOnView(edtLogUsername, getString(R.string.error_invalid_username));
        }else if(strLogPass.equals("")){
            CommonUtils.setErrorOnView(edtLogPass, getString(R.string.error_field_required));
        }else{
            return true;
        }
        return false;
    }
    private Map<String, String> getParams(){
        Map<String, String> params = new HashMap<>();
        params.put("username", strLogUsername);
        params.put("password", strLogPass);
        params.put("devicetoken", CommonUtils.getDeviceToken(LoginActivity.this));
        params.put("devicetype", "Android");
        return params;
    }
    private void loginWebService(){
        AppController.getInstance().callVollayWebService(Request.Method.POST, WebServiceConstant.LOGIN_URL, getParams(), new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                mProgressDialog.dismiss();
                try {
                    //Log.e("response ",response);
                    //converting response to json object
                    if(response != null){
                        //JSONObject obj = new JSONObject(response);
                        Gson gson = new Gson();
                        ResponseModel responseData =  gson.fromJson(response, ResponseModel.class);
                        //Log.e("response ",obj.toString());

                        if(responseData.getStatus().toString().equals("1")){
                        //if(obj.getString("status").equals("1")){
                            //User userDetail =  gson.fromJson(obj.getJSONObject("UserData").toString(), User.class);
                            //creating a new user object
                            /*User user = new User(
                            userJson.getInt("id"),
                                    userJson.getString("username"),
                                    userJson.getString("email"),
                                    userJson.getString("gender")
                                    );*/
                            //storing the user in shared preferences
                            SharedPrefrenceManager.getInstance(LoginActivity.this).setUserDetails(responseData.getUserData());
                            AppController.getSessionData(getApplicationContext());
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }else{
                            CommonUtils.showAlertMessage(LoginActivity.this,getString(R.string.error),getString(R.string.error),responseData.getMessage(),getString(R.string.ok));
                            //CommonUtils.ShowToastMessages(LoginActivity.this,"User name password is invalid, Please try again.");
                        }
                    }else{
                        CommonUtils.showAlertMessage(LoginActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertMessage(LoginActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                }
            }
            @Override
            public void onErrorResponse(String result) {
                mProgressDialog.dismiss();
                //CommonUtils.ShowToastMessages(LoginActivity.this,error.getMessage()+" service error  ");
                CommonUtils.showAlertMessage(LoginActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
            }
        });
    }

}
