package com.yotravell.fragments;

/**
 * Created by Developer on 9/12/2017.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.yotravell.R;
import com.yotravell.VolleyService.AppController;
import com.yotravell.adapter.PostAdapter;
import com.yotravell.constant.WebServiceConstant;
import com.yotravell.interfaces.VolleyCallback;
import com.yotravell.models.Feed;
import com.yotravell.models.ResponseModel;
import com.yotravell.utils.CommonUtils;
import com.yotravell.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {

    private static View view;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Feed> aResponse;
    //private ProgressBar mProgressBar;
    private LinearLayout progressLayout;

    int pageNo = 1;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

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
        //mProgressBar = view.findViewById(R.id.progressBar);
        progressLayout = view.findViewById(R.id.progressBarLayout);

        /*Name = getActivity().getResources().getStringArray(R.array.user_name);
        Image = getActivity().getResources().getStringArray(R.array.user_image);*/

        mRecyclerView.setHasFixedSize(true);
        feedWebService(true);
    }
    /**
     * this function use for get all member list.
     * @return void;
     */

    private Map<String, String> getParams(){
        Map<String, String> params = new HashMap<>();
        params.put("page", ""+pageNo);
        return params;
    }
    private void feedWebService(boolean isShow){

        isShowProgressBar(isShow);

        AppController.getInstance().callVollayWebService(Request.Method.POST, WebServiceConstant.FEED_ACTIVITY, getParams(), new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                isShowProgressBar(false);
                try {
                    Log.e("response ",response);
                    //converting response to json object
                    if(response != null){
                        //JSONObject obj = new JSONObject(response);
                        Gson gson = new Gson();
                        ResponseModel responseData =  gson.fromJson(response, ResponseModel.class);
                        if(responseData.getStatus().equals("1")){

                            if(aResponse != null){
                                aResponse.addAll(responseData.getActivityFeed());
                                adapter.notifyDataSetChanged();
                            }else{
                                aResponse = responseData.getActivityFeed();
                                setFeedListAdapter();
                            }

                        }else{
                            CommonUtils.showAlertMessage(getActivity(),getString(R.string.error),getString(R.string.error),responseData.getMessage(),getString(R.string.ok));
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

    private void isShowProgressBar(boolean isShow){
        if(isShow){
            progressLayout.setVisibility(View.VISIBLE);
        } else{
            progressLayout.setVisibility(View.GONE);
        }
        /*if(isShow) mProgressBar.setVisibility(View.VISIBLE);
        else mProgressBar.setVisibility(View.GONE);*/
    }

    /**
     * this function use for validate login form.
     */
    private void setFeedListAdapter(){
        adapter = new PostAdapter(getActivity(),aResponse,mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setVisibility(View.VISIBLE);

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //Log.e("page", "page"+page);
                pageNo = page+1;
                feedWebService(false);
            }
        };
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener);

    }


}