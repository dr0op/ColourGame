package com.dev.droopy.colourmate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.droopy.colourmate.R;


public class ClassicFragment extends Fragment {
    public ClassicFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classic, container, false);

        getActivity().setTitle("CampusRope");
        return view;
    }
}
