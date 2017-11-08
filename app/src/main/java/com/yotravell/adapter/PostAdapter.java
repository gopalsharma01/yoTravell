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
    private static int screenWidth = CommonUtils.getScreenWidth();
    static final String TAG = "POST ADAPTER**";
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_HEADER = 1;
    private final int VIEW_TYPE_LOADING = 2;
    private static ArrayList<Feed> aResponse = null;

    public PostAdapter(Context context, ArrayList<Feed> response,RecyclerView mRecyclerView) {
        this.context = context;
        this.aResponse = response;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = null;// = layoutInflater.inflate(R.layout.row_single_post,  parent, false);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_single_post, parent, false);
        Log.d(TAG, "Constructor Calling "+screenWidth);

        //return new ViewHolder(view);
        if (viewType == ITEM_TYPE_NORMAL) {
            View view = layoutInflater.inflate(R.layout.post_feed,  parent, false);
            return new ViewHolder(view);
            //View normalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_feed, null);

        } else if (viewType == ITEM_TYPE_HEADER) {
            View view = layoutInflater.inflate(R.layout.row_single_post,  parent, false);
            return new ViewHolder(view);
            //View headerRow = LayoutInflater.from(getContext()).inflate(R.layout.row_single_post, null);

        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = layoutInflater.inflate(R.layout.progress_bar,  parent, false);
            Log.d(TAG, "loading Calling "+screenWidth);
            return new LoadingViewHolder(view);
            //View headerRow = LayoutInflater.from(getContext()).inflate(R.layout.row_single_post, null);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderView, final int position) {

        if (holderView instanceof ViewHolder) {
            if (position != 0) {
                Log.e(TAG, "Holder " + position);
               final ViewHolder holder = (ViewHolder) holderView;
            /*if(this.aResponse.get(position-1).getUserFullname().equals("")){
                holder.name.setText(this.aResponse.get(position-1).getNiceName());
            }else{
                holder.name.setText(this.aResponse.get(position-1).getUserFullname());
            }*/

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        holder.name.setText(Html.fromHtml(aResponse.get(position - 1).getPostAction(), Images, null));
                        holder.content.setText(Html.fromHtml(aResponse.get(position - 1).getContent(), Images, null));

                    }
                });

                Picasso.with(context)
                        .load(this.aResponse.get(position - 1).getUserProfileImg())
                        .placeholder(R.drawable.ic_placeholder)   // optional
                        .error(R.drawable.ic_placeholder)      // optional  ic_error
                        .resize(400, 400)                        // optional
                        .into(holder.feedImg);
                //holder.UserImage.setImageUrl(Image[position].toString().trim(), imageLoader);
            /*Picasso.with(context)
                    .load(Image[position-1].toString().trim())
                    .placeholder(R.drawable.ic_placeholder)   // optional
                    .error(R.drawable.ic_placeholder)      // optional  ic_error
                    .resize(400, 400)                        // optional
                    .into(holder.UserImage);*/
                //loadImage(Image[position],holder.UserImage);

            }
        }else if (holderView instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holderView;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0) {
            return ITEM_TYPE_NORMAL;
        } else if(position <= this.aResponse.size()) {
            return ITEM_TYPE_HEADER;
        } else{
            Log.e("gopal sharma chec "," getid "+this.aResponse.size());
            return VIEW_TYPE_LOADING;
        }
    }

    @Override
    public int getItemCount() {
        return (this.aResponse.size()+2);
    }

    // "Loading item" ViewHolder
    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        private LinearLayout progressLayout;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Context context;
        ImageView feedImg;
        TextView content;
        TextView feedAction;

        public ViewHolder(View v) {
            super(v);
            Log.d(TAG, "View Holder");

            name = (TextView) v.findViewById(R.id.txtPostUserName);
            feedImg = (ImageView) v.findViewById(R.id.feedImg);
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
            int realWidth = screenWidth - (2*200);
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

            }
            catch (Exception e)
            {
                image = null;
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean result) {

        }
    }
}