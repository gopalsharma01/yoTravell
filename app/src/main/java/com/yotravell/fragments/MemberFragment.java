package com.yotravell.fragments;

/**
 * Created by Developer on 9/12/2017.
 */
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.yotravell.R;
import com.yotravell.VolleyService.AppController;
import com.yotravell.adapter.MemberAdapter;
import com.yotravell.constant.WebServiceConstant;
import com.yotravell.interfaces.VolleyCallback;
import com.yotravell.models.Members;
import com.yotravell.models.ResponseModel;
import com.yotravell.utils.CommonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Members> aResponse;
    private ProgressDialog mProgressDialog;

    public static MemberFragment newInstance() {
        return new MemberFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);

        recyclerView= (RecyclerView) view.findViewById(R.id.member_recycler_view);

        mProgressDialog = CommonUtils.ProgressBar(getActivity(), "");
        mProgressDialog.show();

        recyclerView.setHasFixedSize(true);

        //Call WebService to get All member List
        memberWebService();
        return view;
    }
    /**
     * this function use for get all member list.
     * @params none;
     * @return void;
     */

    private Map<String, String> getParams(){
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(AppController.aSessionUserData.getId()));
        return params;
    }
    public void memberWebService(){
        AppController.getInstance().callVollayWebService(Request.Method.POST, WebServiceConstant.MEMBER_LIST_URL, getParams(), new VolleyCallback() {
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
                        if(responseData.getStatus().toString().equals("1")){//obj.getString("status").equals("1")
                            //Members[] aMemberLst =  gson.fromJson(obj.getString("aUsersList"), Members[].class);
                            //aResponse = new ArrayList<Members>(Arrays.asList(aMemberLst));
                            aResponse = responseData.getaUsersList();
                            //Log.e("Member name",aResponse.get(0).getEmail());
                            setMemberListAdapter();
                        }else{
                            CommonUtils.showAlertMessage(getActivity(),getString(R.string.error),getString(R.string.error),responseData.getMessage(),getString(R.string.ok));
                            //CommonUtils.ShowToastMessages(LoginActivity.this,"User name password is invalid, Please try again.");
                        }
                    }else{
                        CommonUtils.showAlertMessage(getActivity(),getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //CommonUtils.showAlertMessage(LoginActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                }
            }
            @Override
            public void onErrorResponse(String result) {
                mProgressDialog.dismiss();
            }
        });
    }
    /**
     * this function use for validate login form.
     * @params none;
     * @return boolean true/false;
     */
    public void setMemberListAdapter(){
        adapter=new MemberAdapter(getActivity(),aResponse,"Members");
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }
}