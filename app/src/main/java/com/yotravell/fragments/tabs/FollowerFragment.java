package com.yotravell.fragments.tabs;

/**
 * Created by Developer on 9/12/2017.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.yotravell.R;
import com.yotravell.VolleyService.AppController;
import com.yotravell.adapter.FriendAdapter;
import com.yotravell.adapter.MemberAdapter;
import com.yotravell.constant.WebServiceConstant;
import com.yotravell.interfaces.VolleyCallback;
import com.yotravell.models.Members;
import com.yotravell.models.ResponseModel;
import com.yotravell.utils.CommonUtils;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FollowerFragment extends Fragment {

    private String[] aName,aImage;
    private LinearLayout progressBarLayout;
    private View followerTabView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Members> aResponse;

    private int pageNo = 1;

    public static FollowerFragment newInstance() {
        return new FollowerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        followerTabView = inflater.inflate(R.layout.tab_fragment_follower,container, false);
        init();
        return followerTabView;
    }

    private void init(){
        aName = getActivity().getResources().getStringArray(R.array.user_name);
        aImage = getActivity().getResources().getStringArray(R.array.user_image);

        progressBarLayout = followerTabView.findViewById(R.id.progressBarFollowerTabLayout);
        mRecyclerView = followerTabView.findViewById(R.id.followerTabRecyclerView);

        mRecyclerView.setHasFixedSize(true);
        followerWebService(true);

    }
    private void isShowProgressBar(boolean isShow){
        if(isShow){
            progressBarLayout.setVisibility(View.VISIBLE);
        } else{
            progressBarLayout.setVisibility(View.GONE);
        }
    }
    /**
     * this function use for get all member list.
     * @return void;
     */

    private Map<String, String> getParams(){
        Map<String, String> params = new HashMap<>();
        params.put("page", ""+pageNo);
        params.put("user_id", ""+ AppController.aSessionUserData.getId());
        return params;
    }
    private void followerWebService(final boolean isShow){
        isShowProgressBar(isShow);
        AppController.getInstance().callVollayWebService(Request.Method.POST, WebServiceConstant.FRIEND_LIST_URL, getParams(), new VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                isShowProgressBar(false);
                try {
                    //Log.e("response ",response);
                    //converting response to json object
                    if(response != null){
                        //JSONObject obj = new JSONObject(response);
                        Gson gson = new Gson();
                        ResponseModel responseData =  gson.fromJson(response, ResponseModel.class);
                        if(responseData.getStatus().equals("1")){
                            aResponse = responseData.getaUsersList();
                            setFollowerListAdapter();
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
    private void setFollowerListAdapter(){
        adapter=new FriendAdapter(getActivity(),aResponse,"TabFriend");
        mRecyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setVisibility(View.VISIBLE);
        progressBarLayout.setVisibility(View.GONE);
        /*adapter = new PostAdapter(getActivity(),aResponse,mRecyclerView, Constant.PERSONAL_FEED);
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
        mRecyclerView.addOnScrollListener(scrollListener);*/
    }
}