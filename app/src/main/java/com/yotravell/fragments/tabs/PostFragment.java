package com.yotravell.fragments.tabs;

/**
 * Created by Developer on 9/12/2017.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.yotravell.R;
import com.yotravell.VolleyService.AppController;
import com.yotravell.adapter.PostAdapter;
import com.yotravell.constant.Constant;
import com.yotravell.constant.WebServiceConstant;
import com.yotravell.interfaces.VolleyCallback;
import com.yotravell.models.Feed;
import com.yotravell.models.ResponseModel;
import com.yotravell.utils.CommonUtils;
import com.yotravell.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostFragment extends Fragment {

    //private String[] Name,Image;
    private static View viewPost;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout profileProgressLayout;

    private ArrayList<Feed> aResponse;

    private int pageNo = 1;
    private EndlessRecyclerViewScrollListener scrollListener;

    public static PostFragment newInstance() {
        return new PostFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewPost = inflater.inflate(R.layout.tab_fragment_post,container, false);

        init();
        return viewPost;
    }

    private void init(){
        mRecyclerView = (RecyclerView) viewPost.findViewById(R.id.recycler_view_profile);
        profileProgressLayout = viewPost.findViewById(R.id.progressBarProfileLayout);

        /*Name = getActivity().getResources().getStringArray(R.array.user_name);
        Image = getActivity().getResources().getStringArray(R.array.user_image);*/

        mRecyclerView.setHasFixedSize(true);
        feedWebService(true);
    }
    private void isShowProgressBar(boolean isShow){
        if(isShow){
            profileProgressLayout.setVisibility(View.VISIBLE);
        } else{
            profileProgressLayout.setVisibility(View.GONE);
        }
    }
    /**
     * this function use for get all member list.
     * @return void;
     */

    private Map<String, String> getParams(){
        Map<String, String> params = new HashMap<>();
        params.put("page", ""+pageNo);
        params.put("user_id", ""+AppController.aSessionUserData.getId());
        return params;
    }
    private void feedWebService(final boolean isShow){
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
                            if(isShow){
                                aResponse = responseData.getActivityFeed();
                                setFeedListAdapter();
                            }else{
                                if(aResponse != null){
                                    aResponse.addAll(responseData.getActivityFeed());
                                    adapter.notifyDataSetChanged();

                                }else{
                                    aResponse = responseData.getActivityFeed();
                                    setFeedListAdapter();
                                }
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

    /**
     * this function use for validate login form.
     */
    private void setFeedListAdapter(){
        /*isShowProgressBar(true);
        adapter = new ProfilePostAdapter(getActivity(),Name,Image,mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setVisibility(View.VISIBLE);
        isShowProgressBar(false);
        */
        adapter = new PostAdapter(getActivity(),aResponse,mRecyclerView, Constant.PERSONAL_FEED);
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