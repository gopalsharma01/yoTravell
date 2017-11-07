package com.yotravell.fragments;

/**
 * Created by Developer on 9/12/2017.
 */
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.yotravell.R;
import com.yotravell.VolleyService.AppController;
import com.yotravell.adapter.PostAdapter;
import com.yotravell.constant.WebServiceConstant;
import com.yotravell.interfaces.OnLoadMoreListener;
import com.yotravell.interfaces.VolleyCallback;
import com.yotravell.models.Feed;
import com.yotravell.models.ResponseModel;
import com.yotravell.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;


public class HomeFragment extends Fragment {

    private static View view;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Feed> aResponse;
    private ProgressBar mProgressBar;

    boolean userScrolled = false;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        this.init();
        return view;
    }
    // Initialize all variables and views
    private void init() {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mProgressBar = view.findViewById(R.id.progressBar);

        /*Name = getActivity().getResources().getStringArray(R.array.user_name);
        Image = getActivity().getResources().getStringArray(R.array.user_image);*/

        mRecyclerView.setHasFixedSize(true);
        feedWebService();
    }
    /**
     * this function use for get all member list.
     * @return void;
     */

    private Map<String, String> getParams(){
        Map<String, String> params = new HashMap<>();
        return params;
    }
    private void feedWebService(){
        AppController.getInstance().callVollayWebService(Request.Method.POST, WebServiceConstant.FEED_ACTIVITY, getParams(), new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                // mProgressDialog.dismiss();
                try {
                    //Log.e("response ",response);
                    //converting response to json object
                    if(response != null){
                        //JSONObject obj = new JSONObject(response);
                        Gson gson = new Gson();
                        ResponseModel responseData =  gson.fromJson(response, ResponseModel.class);
                        if(responseData.getStatus().equals("1")){//obj.getString("status").equals("1")
                            //Members[] aMemberLst =  gson.fromJson(obj.getString("aUsersList"), Members[].class);
                            //aResponse = new ArrayList<Members>(Arrays.asList(aMemberLst));
                            //aResponse = responseData.getaUsersList();
                            //Log.e("Member name",aResponse.get(0).getEmail());
                            aResponse = responseData.getActivityFeed();
                            setFeedListAdapter();
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
                //mProgressDialog.dismiss();
            }
        });
    }
    /**
     * this function use for validate login form.
     */
    private void setFeedListAdapter(){
        adapter = new PostAdapter(getActivity(),aResponse, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        implementScrollListener();
    }
    // Implement scroll listener
    private void implementScrollListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("HomeFragment",dx+" onScrolled "+dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(recyclerView.SCROLL_AXIS_VERTICAL==newState) {
                    Log.e("HomeFragment", recyclerView.SCROLL_AXIS_VERTICAL + " onScrollStateChanged " + newState + " odel state " + SCROLL_STATE_IDLE);
                }
            }

        });
    }
}