package com.dev.droopy.colourmate.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dev.droopy.colourmate.R;

/**
 * Created by Admin on 08-08-2017.
 */

public class DeveloperFragment  extends Fragment implements View.OnClickListener{
    private Button b1;
    public DeveloperFragment(){
        //empty constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_developer, container, false);
        getActivity().setTitle("Instructions");
        b1=(Button)view.findViewById(R.id.button3);
        b1.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v==b1){
            final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    }
}
