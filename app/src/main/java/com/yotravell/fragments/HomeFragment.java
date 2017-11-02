package com.yotravell.fragments;

/**
 * Created by Developer on 9/12/2017.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.yotravell.R;
import com.yotravell.adapter.PostAdapter;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] Name;
    private String[] Image;
    private ProgressBar mProgressBar;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mProgressBar = view.findViewById(R.id.progressBar);

        Name = getActivity().getResources().getStringArray(R.array.user_name);
        Image = getActivity().getResources().getStringArray(R.array.user_image);


        recyclerView.setHasFixedSize(true);
        adapter=new PostAdapter(getActivity(),Name,Image);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        return view;
    }
}