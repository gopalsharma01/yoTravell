package com.yotravell.fragments;

/**
 * Created by Developer on 9/12/2017.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yotravell.R;
import com.yotravell.fragments.tabs.GalleryFragment;
import com.yotravell.fragments.tabs.PostFragment;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_tabs,container, false);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.fixture_tabs);
        tabs.setupWithViewPager(viewPager);


        return view;
    }
    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        AdapterTabs adapter = new AdapterTabs(getChildFragmentManager());
        adapter.addFragment(new PostFragment(), "Post");
        adapter.addFragment(new GalleryFragment(), "Gallery");
        /*adapter.addFragment(new TodaysFixturesFragment(), "Today");
        adapter.addFragment(new WeekFixturesFragment(), "Week");
        adapter.addFragment(new MonthFixturesFragment(), "Month");
        adapter.addFragment(new AllFixturesFragment(), "Month");
        adapter.addFragment(new MyTeamsFixturesFragment(), "My Teams");*/
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