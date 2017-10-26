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
import com.yotravell.models.Members;
import com.yotravell.utils.CommonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FriendsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Members> aResponse;
    private ProgressDialog mProgressDialog;

    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);

        recyclerView= (RecyclerView) view.findViewById(R.id.member_recycler_view);

        mProgressDialog = CommonUtils.ProgressBar(getActivity(), "");
        mProgressDialog.show();

        recyclerView.setHasFixedSize(true);

        //Call WebService to get All Friends List
        friendsWebService();
        return view;
    }
    /**
     * this function use for get all member list.
     * @params none;
     * @return void;
     */
    public void friendsWebService(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServiceConstant.FRIEND_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        try {
                            //Log.e("response ",response);
                            //converting response to json object
                            if(response != null){
                                JSONObject obj = new JSONObject(response);
                                Gson gson = new Gson();
                                if(obj.getString("status").equals("1")){
                                    //ResponseModel aResponse =  gson.fromJson(response, ResponseModel.class);
                                    Members[] aMemberLst =  gson.fromJson(obj.getString("aUsersList"), Members[].class);
                                    //aResponse = Arrays.asList(aMemberLst);
                                    aResponse = new ArrayList<Members>(Arrays.asList(aMemberLst));
                                    //Log.e("Member name",aResponse.get(0).getEmail());
                                    setFriendsListAdapter();
                                }else{
                                    CommonUtils.showAlertMessage(getActivity(),getString(R.string.error),getString(R.string.error),obj.getString("message"),getString(R.string.ok));
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.dismiss();
                        //CommonUtils.ShowToastMessages(LoginActivity.this,error.getMessage()+" service error  ");
                        //CommonUtils.showAlertMessage(LoginActivity.this,getString(R.string.error),getString(R.string.error),getString(R.string.error_message),getString(R.string.ok));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(AppController.aSessionUserData.getId()));
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    /**
     * this function use for validate login form.
     * @params none;
     * @return boolean true/false;
     */
    public void setFriendsListAdapter(){
        adapter=new MemberAdapter(getActivity(),aResponse);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }
}