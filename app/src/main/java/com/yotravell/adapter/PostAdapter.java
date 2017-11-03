package com.yotravell.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
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
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    ImageLoader imageLoader;
    Context context;
    private static int screenWidth = CommonUtils.getScreenWidth();
    static final String TAG = "POST ADAPTER**";
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_HEADER = 1;
    private static ArrayList<Feed> aResponse;

    public PostAdapter(Context context, ArrayList<Feed> response) {
        this.context = context;
        this.aResponse = response;
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;// = layoutInflater.inflate(R.layout.row_single_post,  parent, false);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_single_post, parent, false);
        Log.d(TAG, "Constructor Calling "+screenWidth);

        //return new ViewHolder(view);
        if (viewType == ITEM_TYPE_NORMAL) {
            view = layoutInflater.inflate(R.layout.post_feed,  parent, false);
            //View normalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_feed, null);

        } else if (viewType == ITEM_TYPE_HEADER) {
            view = layoutInflater.inflate(R.layout.row_single_post,  parent, false);
            //View headerRow = LayoutInflater.from(getContext()).inflate(R.layout.row_single_post, null);

        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "Holder " + position);
        if (position != 0) {
            /*if(this.aResponse.get(position-1).getUserFullname().equals("")){
                holder.name.setText(this.aResponse.get(position-1).getNiceName());
            }else{
                holder.name.setText(this.aResponse.get(position-1).getUserFullname());
            }*/

            holder.name.setText(Html.fromHtml(this.aResponse.get(position-1).getPostAction(), Images, null));
            holder.content.setText(Html.fromHtml(this.aResponse.get(position-1).getContent(), Images, null));

            Picasso.with(context)
                    .load(this.aResponse.get(position-1).getUserProfileImg())
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
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0) {
            return ITEM_TYPE_NORMAL;
        } else {
            return ITEM_TYPE_HEADER;
        }
    }

    @Override
    public int getItemCount() {
        return this.aResponse.size();
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

        public FetchImageUrl(Context context, String url)
        {
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

        }}
}