package com.yotravell.fragments;

/**
 * Created by Developer on 9/12/2017.
 */
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yotravell.R;
import com.yotravell.VolleyService.AppController;
import com.yotravell.fragments.tabs.FollowerFragment;
import com.yotravell.fragments.tabs.GalleryFragment;
import com.yotravell.fragments.tabs.PostFragment;
import com.yotravell.fragments.tabs.VideoFragment;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private TabLayout tabs;
    private LinearLayout progressBar,profileTabs;

    private ImageView profileImg;
    String[] tabItem;
    private View view;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_tabs,container, false);

        tabItem = getResources().getStringArray(R.array.my_profile_tab_name);

        init();
        return view;
    }
    private void init(){
        profileImg = (ImageView) view.findViewById(R.id.imgProfilePic);

        //progressBar = view.findViewById(R.id.progressBarLayout);
        //profileTabs = view.findViewById(R.id.profileTabs);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabs = (TabLayout) view.findViewById(R.id.fixture_tabs);
        tabs.setupWithViewPager(viewPager);
        setupTabIcons();
        //progressBar.setVisibility(View.GONE);
        //profileTabs.setVisibility(View.VISIBLE);

        profileImg = view.findViewById(R.id.imgProfilePic);
        Log.e("Profile Fragment post tab",""+AppController.aSessionUserData.getFullProfileImage());
        Picasso.with(getActivity())
                .load(AppController.aSessionUserData.getFullProfileImage())
                .placeholder(R.drawable.ic_placeholder)   // optional
                .error(R.drawable.ic_error)      // optional  ic_error
                .resize(400, 400)                        // optional
                .into(profileImg);
    }
    private void setupTabIcons() {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_design, null);
        TextView tabName = (TextView) view.findViewById(R.id.tab);
        //TextView tabIcon = (TextView) view.findViewById(R.id.tabIcon);
        //tabIcon.setText("10");
        tabName.setText(tabItem[0]);
        //tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_favourite, 0, 0);
        tabs.getTabAt(0).setCustomView(view);

        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_design, null);
        TextView tabName1 = (TextView) view1.findViewById(R.id.tab);
        //TextView tabIcon1 = (TextView) view1.findViewById(R.id.tabIcon);
        //tabIcon1.setText("100");
        tabName1.setText(tabItem[1]);
        tabs.getTabAt(1).setCustomView(view1);

        View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_design, null);
        TextView tabName2 = (TextView) view2.findViewById(R.id.tab);
        //TextView tabIcon2 = (TextView) view2.findViewById(R.id.tabIcon);
        //tabIcon2.setText("50");
        tabName2.setText(tabItem[2]);
        tabs.getTabAt(2).setCustomView(view2);

        View view3 = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_design, null);
        TextView tabName3 = (TextView) view3.findViewById(R.id.tab);
        //TextView tabIcon3 = (TextView) view3.findViewById(R.id.tabIcon);
        //tabIcon3.setText("25");
        tabName3.setText(tabItem[3]);
        tabs.getTabAt(3).setCustomView(view3);

        /*
        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("THREE");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_contacts, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);*/
    }
    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        AdapterTabs adapter = new AdapterTabs(getChildFragmentManager());

        adapter.addFragment(new PostFragment(), "Post");
        adapter.addFragment(new GalleryFragment(), "Gallery");
        adapter.addFragment(new VideoFragment(), "Videos");
        adapter.addFragment(new FollowerFragment(), "Followers");

        viewPager.setAdapter(adapter);

    }
    static class AdapterTabs extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public AdapterTabs(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}