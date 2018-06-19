package com.dev.droopy.colourmate.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dev.droopy.colourmate.R;
import com.google.android.gms.games.Games;


/**
 * Created by Admin on 21-06-2017.
 */

public class ClassicFragment extends Fragment implements View.OnClickListener {
    private TextView t1,t2,t3,t4,time;
    private ImageButton l1,l2;
    public ClassicFragment(){
    //default empty constructor.
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classic, container, false);

        getActivity().setTitle("Instruction");
        t1=(TextView)view.findViewById(R.id.level1);
        t2=(TextView)view.findViewById(R.id.level2);
        t3=(TextView)view.findViewById(R.id.level3);
        t4=(TextView)view.findViewById(R.id.level4);
     //   l1=(ImageButton)view.findViewById(R.id.leaderboard1);
      //  l2=(ImageButton)view.findViewById(R.id.leaderboard2);
        time=(TextView)view.findViewById(R.id.timeSurvival);
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        time.setOnClickListener(this);
     //   l1.setOnClickListener(this);
       // l2.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        if(v==t1){
           // Toast.makeText(getContext(),"level1",Toast.LENGTH_SHORT).show();
            AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Level 1");
            String s="Time : 20 sec "+"\n"+"next Level : 500points "+"\n"+"correct : point+20 time +1 "+"\n"+"wrong : Game Over";
            alertDialog.setMessage(s);
            alertDialog.show();
        }
        if(v==t2){
            AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Level 1");
            String s="Time : 20 sec "+"\n"+"next Level : 1000points "+"\n"+"correct : point+30 time +2 "+"\n"+"wrong : Game Over";
            alertDialog.setMessage(s);
            alertDialog.show();
        }
        if(v==t3){
            AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Level 1");
            String s="Time : 20 sec "+"\n"+"next Level :1500points "+"\n"+"correct : point+40 time +2 "+"\n"+"wrong : Game Over";
            alertDialog.setMessage(s);
            alertDialog.show();
        }
       if(v==t4){
            AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Level 1");
            String s="Time : 20 sec "+"\n"+"next Level :  "+"\n"+"correct : point+50 time +2 "+"\n"+"wrong : Game Over";
            alertDialog.setMessage(s);
            alertDialog.show();
        }
        if(v==time){
            AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("SURVIVAL MODE");
            String s="Time : 12 sec "+"\n"+"Level bonous : 2sec "+"\n"+"correct : point+20 time +2 "+"\n"+"Wrong : -50 time +0";
            alertDialog.setMessage(s);
            alertDialog.show();
        }
     /*   if(v==l1){
            if (isSignedIn()) {
                showAlert(getString(R.string.signin_help_title), getString(R.string.signin_help));
            } else {
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(),
                        getString(R.string.leaderboard_leaderboard)), REQUEST_LEADERBOARD);
            }
        }
        if(v==l2){
            if (isSignedIn()) {
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(),
                        getString(R.string.leaderboard_leaderboard)), REQUEST_LEADERBOARD);
            } else {
                showAlert(getString(R.string.signin_help_title), getString(R.string.signin_help));
            }
        }*/
    }

}
