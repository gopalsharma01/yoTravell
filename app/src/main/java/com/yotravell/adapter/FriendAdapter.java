package com.yotravell.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.squareup.picasso.Picasso;
import com.yotravell.R;
import com.yotravell.VolleyService.AppController;
import com.yotravell.constant.WebServiceConstant;
import com.yotravell.interfaces.VolleyCallback;
import com.yotravell.models.Members;
import com.yotravell.utils.CommonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by developer on 9/13/2017.
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    Context context;
    static final String TAG = "FRIEND LIST ADAPTER**";
    private static ArrayList<Members> aMembers;
    private String type;

    public FriendAdapter(Context context, ArrayList<Members> aMembers, String type) {
        this.context = context;
        this.aMembers = aMembers;
        this.type = type;
    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.tab_follower_list,  parent, false);
        //Log.d(TAG, "Constructor Calling");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FriendAdapter.ViewHolder holder, final int position) {
        //Log.d(TAG, "Holder " + position);

        holder.name.setText(this.aMembers.get(position).getName().toUpperCase());
        Picasso.with(context)
                .load(this.aMembers.get(position).getProfileImage().trim())//"http://i.imgur.com/DvpvklR.png")
                .placeholder(R.drawable.ic_placeholder)   // optional
                .error(R.drawable.ic_user)      // optional  ic_error
                .resize(400,400)                        // optional
                .into(holder.UserImage);
    }

    @Override
    public int getItemCount() {
        return this.aMembers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView UserImage;
        private Context context;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();

            name = (TextView) v.findViewById(R.id.txtFriendUserName);
            UserImage = (ImageView) v.findViewById(R.id.friendUserImg);

        }
    }
}