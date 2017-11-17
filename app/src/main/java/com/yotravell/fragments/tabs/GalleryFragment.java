package com.yotravell.fragments.tabs;

/**
 * Created by Developer on 9/12/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.yotravell.ImageViewPagerActivity;
import com.yotravell.R;
import com.yotravell.VolleyService.AppController;
import com.yotravell.adapter.GridViewAdapter;
import com.yotravell.constant.Constant;
import com.yotravell.constant.WebServiceConstant;
import com.yotravell.interfaces.VolleyCallback;
import com.yotravell.models.Gallery;
import com.yotravell.models.ResponseModel;
import com.yotravell.utils.CommonUtils;
import com.yotravell.utils.EndlessRecyclerViewScrollListener;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private Boolean scrollView = false;
    private static View viewGallery;

    private String[] Image,Name;

    private LinearLayout progressBarLayourProfileGallery,pBarLayourProfGalleryLoadMore;
    private GridViewAdapter mGridAdapter;
    private ArrayList<Gallery> mGridData;
    private GridView mGridView;
    private int pageNo = 1;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGallery = inflater.inflate(R.layout.tab_fragment_gallery,container, false);
        init();
        return viewGallery;
    }

    private void init(){
        progressBarLayourProfileGallery = viewGallery.findViewById(R.id.progressBarLayourProfileGallery);
        pBarLayourProfGalleryLoadMore = viewGallery.findViewById(R.id.progressBarLayourProfileGalleryLoadMore);

        /*Name = getActivity().getResources().getStringArray(R.array.user_name);
        Image = getActivity().getResources().getStringArray(R.array.user_image);
        mGridData = new ArrayList<>();
        for(int i=0;i<Image.length;i++){
            Gallery gdata = new Gallery();
            gdata.setName(Name[i]);
            gdata.setValue(Image[i]);
            mGridData.add(gdata);
        }*/

        mGridView = (GridView) viewGallery.findViewById(R.id.gridViewUserImages);

        galleryWebService(true);
    }

    private void isShowProgressBar(boolean isShow){
        if(isShow){
            progressBarLayourProfileGallery.setVisibility(View.VISIBLE);
        } else{
            progressBarLayourProfileGallery.setVisibility(View.GONE);
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
    private void galleryWebService(final boolean isShow){
        if(isShow){
            pageNo = 1;
        }
        isShowProgressBar(isShow);
        AppController.getInstance().callVollayWebService(Request.Method.POST, WebServiceConstant.MY_GALLERY, getParams(), new VolleyCallback() {
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
                            if(isShow){
                                mGridData = responseData.getaUploadedPhotos();
                                setGalleryGridAdapter();
                            }else{
                                if(mGridData != null){
                                    mGridData.addAll(responseData.getaUploadedPhotos());
                                    pBarLayourProfGalleryLoadMore.setVisibility(View.GONE);
                                    mGridAdapter.notifyDataSetChanged();
                                }else{
                                    mGridData = responseData.getaUploadedPhotos();
                                    setGalleryGridAdapter();
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
                isShowProgressBar(false);
                //mProgressDialog.dismiss();
            }
        });
    }

    private void setGalleryGridAdapter(){
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.row_grid_gallery, mGridData);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setVisibility(View.VISIBLE);
        // Listening to GridView item click
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // Launch ImageViewPager.java on selecting GridView Item
                Intent i = new Intent(getContext(), ImageViewPagerActivity.class);

                // Show a simple toast message for the item position
                //Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();

                // Send the click position to ImageViewPager.java using intent

                Bundle bundle = new Bundle();
                bundle.putSerializable("aGallery", mGridData);
                bundle.putSerializable("id", position);
                i.putExtras(bundle);

                /*Bundle bundle = new Bundle();
                bundle.putSerializable("aGallery", mGridData);
                Log.e("Bundle",bundle.toString());*/

                // Start ImageViewPager
                startActivity(i);
            }
        });
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener(){

            @Override
            public void onScroll(AbsListView view,int firstVisibleItem, int visibleItemCount,int totalItemCount) {
                //Algorithm to check if the last item is visible or not
                final int lastItem = firstVisibleItem + visibleItemCount;
                //Log.e("Scroll",lastItem+" last item in grid view "+totalItemCount+" asdjh");
                if(lastItem == totalItemCount){
                   // pBarLayourProfGalleryLoadMore.setVisibility(View.VISIBLE);
                    // here you have reached end of list, load more data
                    //galleryWebService(false);
                }
            }
            @Override
            public void onScrollStateChanged(AbsListView view,int scrollState) {
                //blank, not required in your case
                int threshold = 1;
                int count = mGridView.getCount();
                int page = (count/Constant.MY_GALLERY_PER_PAGE);
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (mGridView.getLastVisiblePosition() >= count - threshold) {
                        if(page == pageNo) {
                            pageNo = page+1;
                            //Log.i("loading more data", mGridView.getLastVisiblePosition() + " sdfs " + count + " sdf"+pageNo+" pageno page "+page);
                            // Execute LoadMoreDataTask AsyncTask
                            pBarLayourProfGalleryLoadMore.setVisibility(View.VISIBLE);
                            galleryWebService(false);
                        }
                    }
                }
            }
        });
    }
}