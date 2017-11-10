package com.yotravell.fragments;

/**
 * Created by Developer on 9/12/2017.
 */
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.yotravell.R;
import com.yotravell.VolleyService.AppController;
import com.yotravell.adapter.MemberAdapter;
import com.yotravell.constant.WebServiceConstant;
import com.yotravell.interfaces.VolleyCallback;
import com.yotravell.models.Members;
import com.yotravell.models.ResponseModel;
import com.yotravell.networkUtils.InternetConnect;
import com.yotravell.utils.CommonUtils;
import com.yotravell.utils.ValidationUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangePasswordFragment extends Fragment implements OnClickListener {

    private View view;
    private ViewGroup changePassForm;

    private EditText edtOldPass,edtNewPass,edtConPass;
    private String strOldPass,strNewPass,strConPass;
    private Button btnSubmit;

    private ProgressDialog mProgressDialog;

    /*public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        init();
        return view;
    }

    private void init() {
        edtOldPass = (EditText) view.findViewById(R.id.editChangePassOldPass);
        edtNewPass = (EditText) view.findViewById(R.id.editChangePassNewPass);
        edtConPass = (EditText) view.findViewById(R.id.editChangePassConfirmPass);

        btnSubmit = (Button) view.findViewById(R.id.btnChangePasswordSubmit);

        changePassForm = (ViewGroup) view.findViewById(R.id.changePassForm);

        mProgressDialog = CommonUtils.ProgressBar(getActivity(), "");

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==btnSubmit){
            CommonUtils.hideKeyboard(getActivity());
            changePassAction();
        }
    }

    private void changePassAction() {
        strOldPass = edtOldPass.getText().toString().trim();
        strNewPass = edtNewPass.getText().toString().trim();
        strConPass = edtConPass.getText().toString().trim();
        if(changePassValidation()){
            if(InternetConnect.isConnected(getActivity())) {
                CommonUtils.clearErrorFromView(changePassForm);
                mProgressDialog.show();
                changePasswordWebService();
            }else{
                CommonUtils.showAlertMessage(getActivity(),getString(R.string.error),getString(R.string.net_connection_error_title),getString(R.string.net_connection_error_msg),getString(R.string.net_connection_error_btn));
            }
        }
    }
    /**
     * when we click on change password submit button, this function call on click.
     * @params none;
     * @return booleam;
     */
    private boolean changePassValidation() {
        if(strOldPass.equals("")){
            CommonUtils.setErrorOnView(edtOldPass, getString(R.string.error_field_required));
        } else if(strNewPass.equals("")){
            CommonUtils.setErrorOnView(edtNewPass, getString(R.string.error_field_required));
        } else if(strConPass.equals("")){
            CommonUtils.setErrorOnView(edtConPass, getString(R.string.error_field_required));
        } else if(!strConPass.equals(strNewPass)){
            CommonUtils.setErrorOnView(edtConPass, getString(R.string.error_match_password));
        } else{
            return true;
        }
        return false;
    }

    /**
     * this function use for get all member list.
     * @return void;
     */

    private Map<String, String> getParams(){
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(AppController.aSessionUserData.getId()));
        params.put("old_password", strOldPass);
        params.put("password", strNewPass);
        return params;
    }
    public void changePasswordWebService(){
        AppController.getInstance().callVollayWebService(Request.Method.POST, WebServiceConstant.CHANGE_PASSWORD, getParams(), new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                mProgressDialog.dismiss();
                try {
                    //converting response to json object
                    if(response != null){
                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("status").equals("1")){
                            CommonUtils.clearForm(changePassForm);
                            CommonUtils.showAlertMessage(getActivity(),getString(R.string.success),getString(R.string.success),obj.getString("message"),getString(R.string.ok));
                        }else{
                            CommonUtils.showAlertMessage(getActivity(),getString(R.string.error),getString(R.string.error),obj.getString("message"),getString(R.string.ok));
                        }
                    }else{
                        CommonUtils.showAlertMessage(getActivity(),getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onErrorResponse(String result) {
                mProgressDialog.dismiss();
            }
        });
    }


}