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

public class ProfilePostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    static final String TAG = "POST ADAPTER**";
    private String[] Name,Image;

    public ProfilePostAdapter(Context context, String[] Name,String[] Image, RecyclerView mRecyclerView) {
        this.context = context;
        this.Name = Name;
        this.Image = Image;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row_single_post,  parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderView, final int position) {

        final ViewHolder holder = (ViewHolder) holderView;
        Log.e("image url",""+this.Image[position].trim());
        holder.name.setText(this.Name[position]);
        Picasso.with(this.context)
                .load(this.Image[position].trim().toString())
                .placeholder(R.drawable.ic_placeholder)   // optional
                .error(R.drawable.ic_error)      // optional  ic_error
                .resize(400, 400)                        // optional
                .into(holder.feedImg);
    }

    @Override
    public int getItemCount() {
        return this.Name.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Context context;
        ImageView feedImg;


        public ViewHolder(View v) {
            super(v);

            name = (TextView) v.findViewById(R.id.txtPostUserName);
            feedImg = (ImageView) v.findViewById(R.id.circel);
        }
    }
}