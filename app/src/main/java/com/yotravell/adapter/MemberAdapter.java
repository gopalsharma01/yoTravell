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
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    Context context;
    static final String TAG = "FRIEND LIST ADAPTER**";
    private static ArrayList<Members> aMembers;
    private String type;
    private static ProgressDialog progressDialog;

    public MemberAdapter(Context context, ArrayList<Members> aMembers,String type) {
        this.context = context;
        this.aMembers = aMembers;
        this.type = type;
        progressDialog = CommonUtils.ProgressBar(context, "");;
    }

    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row_member_list,  parent, false);
        //Log.d(TAG, "Constructor Calling");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemberAdapter.ViewHolder holder, final int position) {
        //Log.d(TAG, "Holder " + position);

        holder.name.setText(this.aMembers.get(position).getName());
        holder.lastActivity.setText(this.aMembers.get(position).getLastActivity());
        //Log.e(TAG, "Holder User image :  " + this.aMembers.get(position).getProfileImage().toString().trim());
        Picasso.with(context)
                .load(this.aMembers.get(position).getProfileImage().trim())//"http://i.imgur.com/DvpvklR.png")
                .placeholder(R.drawable.ic_placeholder)   // optional
                .error(R.drawable.ic_user)      // optional  ic_error
                .resize(400,400)                        // optional
                .into(holder.UserImage);
        //Log.d(TAG, "User Id " + this.aMembers.get(position).getId()+" current user id "+AppController.aSessionUserData.getId());
        if(this.aMembers.get(position).getFriendRequest().equals("")){
            if(!this.aMembers.get(position).getId().toString().equals(AppController.aSessionUserData.getId().toString())){
                //Log.e(TAG, " gopal sharma");
                holder.addFriend.setVisibility(View.VISIBLE);
            }
        }else if(this.aMembers.get(position).getFriendRequest().equals("0")){
            if(this.aMembers.get(position).getRequestSender().toString().equals("1")){
                holder.cancelRequest.setVisibility(View.VISIBLE);
            }else{
                holder.requestAction.setVisibility(View.VISIBLE);
            }
        }else if(this.aMembers.get(position).getFriendRequest().equals("1")){
            holder.cancelFriendShip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.aMembers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView lastActivity;
        private ImageView UserImage;
        private LinearLayout requestAction;
        private Button cancelRequest,cancelFriendShip,addFriend,requestAccept,requestReject;
        private Context context;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();

            name = (TextView) v.findViewById(R.id.txtUserName);
            UserImage = (ImageView) v.findViewById(R.id.imgMemberFragment);
            lastActivity = (TextView) v.findViewById(R.id.txtLastLogin);

            requestAction = (LinearLayout) v.findViewById(R.id.requestAction);

            cancelRequest = (Button) v.findViewById(R.id.btnCancelRequest);
            cancelFriendShip = (Button) v.findViewById(R.id.btnCancelFriendship);
            addFriend = (Button) v.findViewById(R.id.btnSendRequest);

            requestAccept = (Button) v.findViewById(R.id.btnAcceptRequest);
            requestReject = (Button) v.findViewById(R.id.btnRejectRequest);

            cancelRequest.setOnClickListener(this);
            cancelFriendShip.setOnClickListener(this);
            addFriend.setOnClickListener(this);
            requestAccept.setOnClickListener(this);
            requestReject.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Map<String, String> params = new HashMap<>();
            String url = null;
            params.put("sender_id", String.valueOf(AppController.aSessionUserData.getId()));
            params.put("receiver_id", String.valueOf(aMembers.get(getAdapterPosition()).getId()));
            if (v.getId() == cancelRequest.getId() || v.getId() == requestReject.getId() || v.getId() == cancelFriendShip.getId()){
                //Log.e("reponse",String.valueOf(getAdapterPosition())+" "+aMembers.get(getAdapterPosition()).getName());
                //Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                params.put("request_id", String.valueOf(aMembers.get(getAdapterPosition()).getRequestId()));
                url = WebServiceConstant.CANCEL_FRIEND_REQUEST;
            } else if (v.getId() == addFriend.getId()){
                url = WebServiceConstant.SEND_FRIEND_REQUEST;
            } else if (v.getId() == requestAccept.getId()){
                params.put("request_id", String.valueOf(aMembers.get(getAdapterPosition()).getRequestId()));
                url = WebServiceConstant.ACCEPT_FRIEND_REQUEST;
            }
            if(url!=null){
                progressDialog.show();
                callWebService(v.getContext(),url,params,getAdapterPosition(),v);
                Log.e("Warning ",v.getResources().getString(R.string.warning)+" war "+v.getResources().getString(R.string.success)+" suc "+v.getResources().getString(R.string.info));
            }
        }

        private void callWebService(Context con, String url, final Map<String, String> params, final int position, final View iView){
            AppController.getInstance().callVollayWebService(Request.Method.POST, url, params, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String response) {
                    try {
                        progressDialog.dismiss();
                        if(response != null){

                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("1")){
                                if(obj.has("requestId")){
                                    aMembers.get(position).setRequestId(obj.getInt("requestId"));
                                }
                                if (iView.getId() == cancelRequest.getId() || iView.getId() == requestReject.getId() || iView.getId() == cancelFriendShip.getId()){
                                    if(iView.getId() == requestReject.getId()){
                                        requestAction.setVisibility(View.GONE);
                                    }else{
                                        iView.setVisibility(View.GONE);
                                    }
                                    addFriend.setVisibility(View.VISIBLE);
                                } else if (iView.getId() == addFriend.getId()){
                                    iView.setVisibility(View.GONE);
                                    cancelRequest.setVisibility(View.VISIBLE);
                                } else if (iView.getId() == requestAccept.getId()){
                                    requestAction.setVisibility(View.GONE);
                                    cancelFriendShip.setVisibility(View.VISIBLE);
                                }
                            }else{
                                if(obj.getString("status").equals("2")){
                                    if(obj.has("actionType")){
                                        if(obj.has("requestId")){
                                            aMembers.get(position).setRequestId(obj.getInt("requestId"));
                                        }else{
                                            aMembers.get(position).setRequestId(null);
                                        }
                                        switch (obj.getString("actionType")){
                                            case "addFriend":
                                                if (iView.getId() == requestAccept.getId() || iView.getId() == requestReject.getId()){
                                                    requestAction.setVisibility(View.GONE);
                                                }else{
                                                    iView.setVisibility(View.GONE);
                                                }
                                                addFriend.setVisibility(View.VISIBLE);
                                                break;
                                            case "CancelFriendShip":
                                                iView.setVisibility(View.GONE);
                                                cancelFriendShip.setVisibility(View.VISIBLE);
                                                break;
                                            case "CancelRequest":
                                                iView.setVisibility(View.GONE);
                                                cancelRequest.setVisibility(View.VISIBLE);
                                                break;
                                            case "requestAction":
                                                iView.setVisibility(View.GONE);
                                                requestAction.setVisibility(View.VISIBLE);
                                                break;
                                        }
                                    }

                                    CommonUtils.showAlertMessage(context,context.getResources().getString(R.string.warning),context.getResources().getString(R.string.warning),obj.getString("message"),context.getResources().getString(R.string.ok));
                                    //CommonUtils.showAlertMessage(context,"Warning","Warning",obj.getString("message"),"OK");
                                }else{
                                    CommonUtils.showAlertMessage(context,context.getResources().getString(R.string.error),context.getResources().getString(R.string.error),obj.getString("message"),context.getResources().getString(R.string.ok));
                                }
                            }
                        }else{
                            CommonUtils.showAlertMessage(context,context.getResources().getString(R.string.error),context.getResources().getString(R.string.error),context.getResources().getString(R.string.error_message),context.getResources().getString(R.string.ok));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onErrorResponse(String result) {
                    progressDialog.dismiss();
                    CommonUtils.showAlertMessage(context,context.getResources().getString(R.string.error),context.getResources().getString(R.string.error),result,context.getResources().getString(R.string.ok));
                }
            });
        }
    }
}