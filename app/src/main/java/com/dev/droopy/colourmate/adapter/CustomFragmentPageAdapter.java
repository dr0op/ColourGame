package com.dev.droopy.colourmate.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dev.droopy.colourmate.fragment.*;
import com.dev.droopy.colourmate.fragment.ClassicFragment;
import com.dev.droopy.colourmate.fragment.ShareFragment;

/**
 * Created by shubham on 21-06-2017.
 */

public class CustomFragmentPageAdapter extends FragmentPagerAdapter {
    private static final int FRAGMENT_COUNT = 3;
    public CustomFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ClassicFragment();
            case 1:
                return new ShareFragment();
            case 2:
                return new DeveloperFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }
    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
                return "Instruction";
            case 1:
                return "Share/feedback";
            case 2:
                return "Developer";
        }

        return null;
    }
}
