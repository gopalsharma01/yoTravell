package com.yotravell.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by developer on 9/13/2017.
 */
public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ImageLoader imageLoader;
    String[] Name;
    String[] Image;
    Context context;
    static final String TAG = "POST ADAPTER**";

    public PostAdapter(Context context, String[] name, String[] image) {
        this.context = context;
        Image = image;
        Name = name;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view  = null;
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_single_post, parent, false);
        Log.e(TAG, "Constructor Calling" +viewType);

        if(viewType == 0){
            view = layoutInflater.inflate(R.layout.post_feed,  parent, false);
        }else{
            view = layoutInflater.inflate(R.layout.row_single_post,  parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0:
                break;
            case 2:
                break;
        }
    }

    /*@Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "Holder " + position);

        //holder.UserImage.setImageResource(Image[position]);
        *//*holder.name.setText(Name[position]);
        //holder.Distance.setText(Distance[position]);
        //holder.LastSeen.setText(LastSeen[position]);
        Log.e(TAG, "Holder User image :  " + Image[position]);
        //holder.UserImage.setImageUrl(Image[position].toString().trim(), imageLoader);
        Picasso.with(context)
                .load(Image[position].toString().trim())
                .placeholder(R.drawable.ic_placeholder)   // optional
                .error(R.drawable.ic_placeholder)      // optional  ic_error
                .resize(400,400)                        // optional
                .into(holder.UserImage);
        *//*//loadImage(Image[position],holder.UserImage);
    }
*/
    private void loadImage(String url,NetworkImageView UserImage){
        if(!url.equals("")) {
            imageLoader = CustomVolleyImageRequest.getInstance(this.context)
                    .getImageLoader();
            imageLoader.get(url, ImageLoader.getImageListener(UserImage,
                    R.drawable.ic_placeholder, R.drawable.ic_error));
            UserImage.setImageUrl(url, imageLoader);
        }
    }

    @Override
    public int getItemCount() {
        return Name.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Context context;
        //NetworkImageView UserImage;
        ImageView UserImage;

        public ViewHolder(View v) {
            super(v);
            Log.d(TAG, "View Holder"+v.getTag());

            /*name = (TextView) v.findViewById(R.id.txtPostUserName);
            //UserImage = (NetworkImageView) v.findViewById(R.id.thumbnail);
            UserImage = (ImageView) v.findViewById(R.id.imgUserProfile);*/
        }
    }
}