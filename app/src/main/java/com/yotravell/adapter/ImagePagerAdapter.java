package com.yotravell.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.yotravell.R;
import com.yotravell.models.Gallery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Developer on 11/14/2017.
 */

public class ImagePagerAdapter extends PagerAdapter {

    private ArrayList<Gallery> images;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public ImagePagerAdapter(Context mContext, ArrayList<Gallery> images) {
        this.context = mContext;
        this.images = images;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mLayoutInflater.inflate(R.layout.row_single_gallery, container,false);


        // Locate the ImageView in viewpager_item.xml
        ImageView imgSingle = (ImageView) itemView.findViewById(R.id.singleImg);
        // Capture position and set to the ImageView

        Picasso.with(context)
                .load(images.get(position).getValue())
                .placeholder(R.drawable.ic_placeholder)   // optional
                .error(R.drawable.ic_error)      // optional  ic_error
                //.resize(400, 400)                        // optional
                .into(imgSingle);

        // Add viewpager_item.xml to ViewPager

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((View) object);

    }
}
