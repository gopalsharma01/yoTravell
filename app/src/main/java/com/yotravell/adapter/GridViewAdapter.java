package com.yotravell.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yotravell.R;
import com.yotravell.models.Gallery;

import java.util.ArrayList;

/**
 * Created by Developer on 11/13/2017.
 */

public class GridViewAdapter extends ArrayAdapter<Gallery> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Gallery> mGridData = new ArrayList<Gallery>();

    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<Gallery> mGridData) {
            super(mContext, layoutResourceId, mGridData);
            this.layoutResourceId = layoutResourceId;
            this.mContext = mContext;
            this.mGridData = mGridData;
    }

    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */

    public void setGridData(ArrayList<Gallery> mGridData) {
            this.mGridData.addAll(mGridData);
            notifyDataSetChanged();
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;
            if (row == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new ViewHolder();
                holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
                holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }
            Gallery item = mGridData.get(position);
            holder.titleTextView.setText(item.getName());

            Picasso.with(mContext)
                .load(item.getValue())
                .placeholder(R.drawable.ic_placeholder)   // optional
                .error(R.drawable.ic_error)      // optional  ic_error
                .resize(400, 400)                        // optional
                .into(holder.imageView);

            return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}
