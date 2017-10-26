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
import com.yotravell.models.Members;

import java.util.ArrayList;

/**
 * Created by developer on 9/13/2017.
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    ImageLoader imageLoader;
    Context context;
    static final String TAG = "FRIEND LIST ADAPTER**";
    private ArrayList<Members> aMembers;

    public MemberAdapter(Context context, ArrayList<Members> aMembers) {
        this.context = context;
        this.aMembers = aMembers;
    }

    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row_member_list,  parent, false);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_single_post, parent, false);
        Log.d(TAG, "Constructor Calling");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemberAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "Holder " + position);

        //holder.UserImage.setImageResource(Image[position]);
        holder.name.setText(this.aMembers.get(position).getName());
        holder.lastActivity.setText(this.aMembers.get(position).getLastActivity());
        //holder.Distance.setText(Distance[position]);
        //holder.LastSeen.setText(LastSeen[position]);
        Log.e(TAG, "Holder User image :  " + this.aMembers.get(position).getProfileImage().toString().trim());
        //holder.UserImage.setImageUrl(Image[position].toString().trim(), imageLoader);
        Picasso.with(context)
                .load(this.aMembers.get(position).getProfileImage().toString().trim())//"http://i.imgur.com/DvpvklR.png")
                .placeholder(R.drawable.ic_placeholder)   // optional
                .error(R.drawable.ic_user)      // optional  ic_error
                .resize(400,400)                        // optional
                .into(holder.UserImage);
        //loadImage(Image[position],holder.UserImage);
    }

    @Override
    public int getItemCount() {
        return this.aMembers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView lastActivity;
        private Context context;
        ImageView UserImage;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.txtUserName);
            UserImage = (ImageView) v.findViewById(R.id.userImg);
            lastActivity = (TextView) v.findViewById(R.id.txtLastLogin);
        }
    }
}