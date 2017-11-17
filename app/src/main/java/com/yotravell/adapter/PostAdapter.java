package com.yotravell.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;
import com.yotravell.R;
import com.yotravell.VolleyService.AppController;
import com.yotravell.constant.Constant;
import com.yotravell.models.Feed;
import com.yotravell.utils.CommonUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by developer on 9/13/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ImageLoader imageLoader;
    Context context;
    private static int screenWidth;
    static final String TAG = "POST ADAPTER**";
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_HEADER = 1;
    private final int VIEW_TYPE_LOADING = 2;
    private static ArrayList<Feed> aResponse = null;
    private String feedType;
    private int addMoreIndex = 1;

    public PostAdapter(Context context, ArrayList<Feed> response,RecyclerView mRecyclerView,String feedType) {
        this.context = context;
        this.aResponse = response;
        this.feedType = feedType;
        this.screenWidth = CommonUtils.getScreenWidth(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = null;// = layoutInflater.inflate(R.layout.row_single_post,  parent, false);
        /*View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_single_post, parent, false);
        View headerRow = LayoutInflater.from(getContext()).inflate(R.layout.row_single_post, null);*/
        //Log.d(TAG, "Constructor Calling "+screenWidth);
        if (viewType == ITEM_TYPE_NORMAL) {
            View view = layoutInflater.inflate(R.layout.post_feed,  parent, false);
            return new PostFeedViewHolder(view);
        } else if (viewType == ITEM_TYPE_HEADER) {
            View view = layoutInflater.inflate(R.layout.row_single_post,  parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = layoutInflater.inflate(R.layout.progress_bar,  parent, false);
            return new LoadingViewHolder(view);
        }
        return null;//new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderView, final int position) {

        if (holderView instanceof ViewHolder) {
            int pos = position;
            if (position != 0 && this.feedType.equals("All")) {
                pos = position - 1;
            }
            final int index = pos;
            Log.e(TAG, "Holder " + index+" size "+this.aResponse.size());
            final ViewHolder holder = (ViewHolder) holderView;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    String name=aResponse.get(index).getUserFullname();
                    if(name != null){
                        name = aResponse.get(index).getNiceName();
                    }
                    holder.name.setText(Html.fromHtml(aResponse.get(index).getUserFullname()));
                    holder.action.setText(aResponse.get(index).getPostAction());
                    holder.content.setText(Html.fromHtml(aResponse.get(index).getContent(), Images, null));
                    Picasso.with(context)
                            .load(aResponse.get(index).getUserProfileImg())
                            .placeholder(R.drawable.ic_placeholder)   // optional
                            .error(R.drawable.ic_placeholder)      // optional  ic_error
                            .resize(400, 400)                        // optional
                            .into(holder.feedImg);

                }
            });
        }else if (holderView instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holderView;
            if((aResponse.size())%Constant.FEED_PER_PAGE==0) {
                loadingViewHolder.progressBar.setIndeterminate(true);
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
            } else{
                loadingViewHolder.progressBar.setVisibility(View.GONE);
            }
        } else if (holderView instanceof PostFeedViewHolder) {
            PostFeedViewHolder postFeedViewHolder = (PostFeedViewHolder) holderView;
            Picasso.with(context)
                    .load(AppController.aSessionUserData.getProfileImage())
                    .placeholder(R.drawable.ic_placeholder)   // optional
                    .error(R.drawable.ic_placeholder)      // optional  ic_error
                    .resize(400, 400)                        // optional
                    .into(postFeedViewHolder.feedImg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //Log.e("gopal sharma chec "," getid "+this.aResponse.size());

        if (position==0 && this.feedType.equals("All")) {
            return ITEM_TYPE_NORMAL;
        } else if((position < (this.aResponse.size() + 1) && this.feedType.equals("All")) || (position < this.aResponse.size())) {
            return ITEM_TYPE_HEADER;
        } else{
            return VIEW_TYPE_LOADING;
        }
    }

    @Override
    public int getItemCount() {
        if(this.feedType.equals("All")){
            this.addMoreIndex = 2;
        }
        return (this.aResponse.size() + this.addMoreIndex);
    }

    // "PostFeed item" ViewHolder
    private static class PostFeedViewHolder extends RecyclerView.ViewHolder {
        private ImageView feedImg;

        public PostFeedViewHolder(View view) {
            super(view);
            feedImg = (ImageView) view.findViewById(R.id.feedUserImg);
        }
    }
    // "Loading item" ViewHolder
    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,action;
        Context context;
        ImageView feedImg;
        TextView content;
        TextView feedAction;


        public ViewHolder(View v) {
            super(v);
            Log.d(TAG, "View Holder");

            name = (TextView) v.findViewById(R.id.txtPostUserName);
            action = (TextView) v.findViewById(R.id.txtPostTime);
            feedImg = (ImageView) v.findViewById(R.id.circel);
            content = (TextView) v.findViewById(R.id.txtPostContent);
            //feedAction = (TextView) v.findViewById(R.id.txtPostTime);
        }
    }


    private Html.ImageGetter Images = new Html.ImageGetter() {

        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            FetchImageUrl fiu = new FetchImageUrl(context,source);
            try {
                fiu.execute().get();
                drawable = fiu.GetImage();
            }
            catch (Exception e) {
                drawable = context.getResources().getDrawable(R.drawable.app_bg);
            }
            // to display image,center of screen
            int imgH = drawable.getIntrinsicHeight();
            int imgW = drawable.getIntrinsicWidth();
            int padding = 0;
            int realWidth = screenWidth - (2*100);
            int realHeight = imgH * realWidth/imgW;
            drawable.setBounds(padding,0,realWidth ,realHeight);
            return drawable;
        }
    };



    public class FetchImageUrl extends AsyncTask<String, String, Boolean> {
        String imageUrl;
        Context context;
        protected Drawable image;
        public FetchImageUrl(Context context, String url) {
            this.imageUrl = url;
            image = null;
            this.context = context;
        }
        public Drawable GetImage()
        {
            return image;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(String... args) {
            try {
                InputStream input_stream = (InputStream) new URL(imageUrl).getContent();
                image = Drawable.createFromStream(input_stream, "src name");
                return true;
            } catch (Exception e){
                image = null;
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {

        }
    }
}