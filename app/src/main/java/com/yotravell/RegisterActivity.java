package com.yotravell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.yotravell.models.Country;
import com.yotravell.models.CountryList;
import com.yotravell.models.ResponseModel;
import com.yotravell.networkUtils.InternetConnect;
import com.yotravell.utils.CommonUtils;
import com.yotravell.utils.ValidationUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtName,edtUserName,edtEmail,edtPassword,edtCountry,edtCity,edtWeather;

    private String strName,strUserName,strEmail,strPassword,strCountry,strCity,strWeather;
    private TextView txtCountry;
    private LinearLayout linearBack;
    private Button btnRegSubmit;
    private Spinner spnrCountry;
    private ProgressDialog mProgressDialog;
    private Intent intent;
    private ViewGroup signUp;
    Country country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        linearBack = (LinearLayout)findViewById(R.id.btnRegBack);
        btnRegSubmit = (Button)findViewById(R.id.btnRegSubmit);
        spnrCountry = (Spinner)findViewById(R.id.spinnerCountry);
        signUp = (ViewGroup) findViewById(R.id.signUp);


        btnRegSubmit.setOnClickListener(this);
        linearBack.setOnClickListener(this);

        //Add Font Family
        TextView txtRegSubTitle = (TextView) findViewById(R.id.txtRegSubTitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/brushScriptMT.ttf");
        txtRegSubTitle.setTypeface(typeface);

        mProgressDialog =  CommonUtils.ProgressBar(RegisterActivity.this, "");

        if(!InternetConnect.isConnected(getApplicationContext())) {
            CommonUtils.showAlertMessage(RegisterActivity.this,getString(R.string.error),getString(R.string.net_connection_error_title),getString(R.string.net_connection_error_msg),getString(R.string.net_connection_error_btn));
        }else {
            mProgressDialog.show();
            regCountryWebService();
        }
    }

    @Override
    public void onClick(View view) {
        if(view==linearBack){
            finish();
        }else if(view==btnRegSubmit){
            CommonUtils.hideKeyboard(RegisterActivity.this);
            regAction();
        }
    }
    /**
     * when we click on register submit button, this function call on click.
     * @params none;
     * @return void;
     */
    private void regAction(){
        edtName = (EditText) findViewById(R.id.edtRegName);
        edtUserName = (EditText) findViewById(R.id.edtRegUsername);
        edtEmail = (EditText) findViewById(R.id.edtRegEmail);
        edtPassword = (EditText) findViewById(R.id.edtRegPassword);
        txtCountry = (TextView)spnrCountry.getSelectedView();

        edtCity = (EditText) findViewById(R.id.edtRegCity);
        edtWeather = (EditText) findViewById(R.id.edtRegWeather);

        strName = edtName.getText().toString().trim();
        strUserName = edtUserName.getText().toString().trim();
        strEmail = edtEmail.getText().toString().trim();
        strPassword = edtPassword.getText().toString().trim();

        strCountry = "";
        if(spnrCountry.getSelectedItemPosition()!=0)
            strCountry = country.getaCountryList().get(spnrCountry.getSelectedItemPosition()-1).getCode();
        //spnrCountry.getItemAtPosition(spnrCountry.getSelectedItemPosition()).toString().trim();
        strCity = edtCity.getText().toString().trim();
        strWeather = edtWeather.getText().toString().trim();
        if(regValidate()){
            CommonUtils.clearErrorFromView(signUp);
            if(InternetConnect.isConnected(getApplicationContext())) {
                mProgressDialog.show();
                regWebService();
            }else{
                CommonUtils.showAlertMessage(RegisterActivity.this,getString(R.string.error),getString(R.string.net_connection_error_title),getString(R.string.net_connection_error_msg),getString(R.string.net_connection_error_btn));
                //CommonUtils.ShowToastMessages(getApplicationContext()," check network connection country "+strCountry);
            }
        }
    }
    /**
     * this function use for validate registration form.
     * @params none;
     * @return boolean true/false;
     */
    private boolean regValidate(){
        if(strName.equals("")){
            CommonUtils.setErrorOnView(edtName, getString(R.string.error_field_required));
        }else if(strUserName.equals("")){
            CommonUtils.setErrorOnView(edtUserName, getString(R.string.error_field_required));
        }else if(!ValidationUtils.isUsernameValid(strUserName)){
            CommonUtils.setErrorOnView(edtUserName,getString(R.string.error_invalid_username));
        }else if(strEmail.equals("")){
            CommonUtils.setErrorOnView(edtEmail, getString(R.string.error_field_required));
        }else if(!ValidationUtils.isEmailValid(strEmail)){
            CommonUtils.setErrorOnView(edtEmail, getString(R.string.error_invalid_email));
        }else if(strPassword.equals("")){
            CommonUtils.setErrorOnView(edtPassword, getString(R.string.error_field_required));
        }else if(strCountry.equals("")){
            txtCountry.setError(getString(R.string.error_field_required));
            txtCountry.setTextColor(Color.RED);
            //CommonUtils.setErrorOnView(txtCountry, getString(R.string.error_field_required));
        }else if(strCity.equals("")){
            CommonUtils.setErrorOnView(edtCity, getString(R.string.error_field_required));
        }else if(strWeather.equals("")){
            CommonUtils.setErrorOnView(edtWeather, getString(R.string.error_field_required));
        }else{
            return true;
        }
        return false;
    }
    private Map<String, String> getParams(){
        Map<String, String> params = new HashMap<>();
        return params;
    }
    private void regCountryWebService(){
        AppController.getInstance().callVollayWebService(Request.Method.POST, WebServiceConstant.COUNTRY_URL, getParams(), new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                mProgressDialog.dismiss();
                //Log.e("country list", response);

                try {
                    //converting response to json object
                    if(response != null){
                        Gson gson = new Gson();
                        country =  gson.fromJson(response, Country.class);
                        List<String> countries = new ArrayList<String>();
                        countries.add("Select Country");
                        if(country != null && country.getaCountryList() != null ){
                            for(int i =0; i <country.getaCountryList().size(); i++){
                                countries.add(country.getaCountryList().get(i).getName());
                            }
                        }
                                /*JSONObject obj = new JSONObject(response);
                                if(obj.getJSONObject("status").equals('1')) {
                                    List<String> countries = new ArrayList<String>();
                                    ArrayList<CountryList> aCountryList = obj.getJSONArray("aCountryList");
                                    if(countryArry != null && countryArry.length() != null ){
                                        for(int i =0; i <country.getaCountryList().size(); i++){
                                            countries.add(country.getaCountryList().get(i).getName());
                                        }
                                    }
                                }*/
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegisterActivity.this, R.layout.country_spinner_layout, countries);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // attaching data adapter to spinner
                        spnrCountry.setAdapter(dataAdapter);

                                /*ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_dropdown_item, countries);
                                MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner) findViewById(R.id.countryList);
                               // materialDesignSpinner.setBackgroundResource(R.drawable.background);
                                materialDesignSpinner.setAdapter(countryAdapter);*/

                    }
                    //Log.e("Response in try  : ",response);
                } catch (Exception e) {
                    CommonUtils.showAlertMessage(RegisterActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                    e.printStackTrace();
                }
            }
            @Override
            public void onErrorResponse(String result) {
                mProgressDialog.dismiss();
                CommonUtils.showAlertMessage(RegisterActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                //CommonUtils.ShowToastMessages(getApplicationContext(),error.getMessage()+" service error  ");
            }
        });
    }
    private Map<String, String> getRegParams(){
        Map<String, String> params = new HashMap<>();
        params.put("name", strName);
        params.put("username", strUserName);
        params.put("email", strEmail);
        params.put("password", strPassword);
        params.put("country", strCountry);
        params.put("city", strCity);
        params.put("weather", strWeather);
        return params;
    }
    private void regWebService(){
        AppController.getInstance().callVollayWebService(Request.Method.POST, WebServiceConstant.REG_URL, getRegParams(), new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                mProgressDialog.dismiss();
                try {
                    Log.e("response",response);
                    //converting response to json object
                    if(response != null){
                        //JSONObject obj = new JSONObject(response);
                        Gson gson = new Gson();
                        ResponseModel responseData =  gson.fromJson(response, ResponseModel.class);
                        if(responseData.getStatus().toString().equals("1")){
                            CommonUtils.clearForm(signUp);
                            CommonUtils.showAlertMessage(RegisterActivity.this,getString(R.string.success),getString(R.string.register_success_title),responseData.getMessage(),getString(R.string.ok));
                            //finish();
                            //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }else{
                            if(!responseData.getUsername().equals("")){//obj.has("username")
                                CommonUtils.setErrorOnView(edtUserName,responseData.getMessage());//obj.getString("message")
                            }else if(!responseData.getEmail().toString().equals("")){//obj.has("email")
                                CommonUtils.setErrorOnView(edtEmail,responseData.getMessage());//obj.getString("message")
                            }

                            CommonUtils.showAlertMessage(RegisterActivity.this,getString(R.string.error),getString(R.string.error),responseData.getMessage(),getString(R.string.ok));
                            //CommonUtils.ShowToastMessages(RegisterActivity.this,obj.getJSONObject("message").toString());
                        }
                    }else{
                        CommonUtils.showAlertMessage(RegisterActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                        //CommonUtils.ShowToastMessages(RegisterActivity.this,getString(R.string.error_message));
                    }
                    //Log.e("Response in try  : ",response);
                } catch (Exception e) {
                    e.printStackTrace();
                    //Log.e("Response in try  : ","incatch");
                    CommonUtils.showAlertMessage(RegisterActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                }
            }
            @Override
            public void onErrorResponse(String result) {
                mProgressDialog.dismiss();
                CommonUtils.showAlertMessage(RegisterActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
            }
        });
    }
}