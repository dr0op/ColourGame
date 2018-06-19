package com.dev.droopy.colourmate.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.droopy.colourmate.R;
import com.dev.droopy.colourmate.adapter.CustomFragmentPageAdapter;

/**
 * Created by Admin on 21-06-2017.
 */

public class LibraryFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public LibraryFragment(){
    //default empty constructor.
    }
    @RequiresApi(api= Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        viewPager = (ViewPager)view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new CustomFragmentPageAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }


}
