package com.yotravell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.yotravell.adapter.ImagePagerAdapter;
import com.yotravell.models.Gallery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Developer on 11/14/2017.
 */

public class ImageViewPagerActivity extends Activity {
    // Declare Variable
    private int position;
    private String[] Image,Name;
    private ImagePagerAdapter pageradapter;
    private ViewPager viewpager;
    private LinearLayout btnPagerBack;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set title for the ViewPager
        setTitle("ViewPager");
        // Get the view from activity_image_pager.xml_pager.xml
        setContentView(R.layout.activity_image_pager);

        // Retrieve data from MainActivity on item click event
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        ArrayList<Gallery> aGallery = (ArrayList<Gallery>)bundle.getSerializable("aGallery");

        position = (Integer) bundle.getSerializable("id");

        //Bundle bundle = (Bundle) p.getExtras().get("aGalleryBundle");
        //ArrayList<Gallery> aGallery = (ArrayList<Gallery>)p.getExtras().getSerializable("aGallery");

        Name = getResources().getStringArray(R.array.user_name);
        Image = getResources().getStringArray(R.array.user_image);


        btnPagerBack = findViewById(R.id.btnPagerBack);
        // Set the images into ViewPager
        pageradapter = new ImagePagerAdapter(this,aGallery);
        viewpager = findViewById(R.id.pager);
        viewpager.setAdapter(pageradapter);
        viewpager.setPageTransformer(true, new RotateDownTransformer());//RotateUpTransformer
        // Show images following the position
        viewpager.setCurrentItem(position);

        btnPagerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
