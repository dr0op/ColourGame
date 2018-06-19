package com.dev.droopy.colourmate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends BaseGameActivity implements View.OnClickListener  {
    private ImageButton playGameButton,playClassicButton;
    private View signInButton, signOutButton;
    private ImageView logoView;
    final int REQUEST_LEADERBOARD = 7000;
    private TextView taglineTextView1, taglineTextView2, taglineTextView3;
    private ImageButton music,leader,instruction;
    int musicPlayed;
    int x;
    MediaPlayer mp1;
    private static final String TAG = "MainActivity";
    InterstitialAd mInterstitialAd;

    private AdView mAdView;
    SharedPreferences sharedPreferences,musicSharedPreference,firstPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instruction=(ImageButton)findViewById(R.id.button_instruction);
        instruction.setOnClickListener(this);
        leader=(ImageButton)findViewById(R.id.leaderboard);
        music=(ImageButton)findViewById(R.id.music);
        leader.setOnClickListener(this);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("76981f10715040aa9d7d312993b2d0e5").build();
        mAdView.loadAd(adRequest);
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();
        Log.i("device id=",deviceId);
        musicSharedPreference=this.getSharedPreferences("musicStatus",Context.MODE_PRIVATE);
       // SharedPreferences.Editor editor = musicSharedPreference.edit();
      //  editor.putInt("music",1);//code is yet to be completed
       // editor.apply();
        mp1 = MediaPlayer.create(this, R.raw.gamesound);
        musicPlayed=musicSharedPreference.getInt("music",1);
        if(musicPlayed==1){
            mp1.start();
            mp1.setVolume(2,50);
            mp1.setLooping(true);
        }
        else{
            music.setImageResource(R.drawable.musicoff);
        }
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp1.isPlaying()){
                    mp1.pause();
                    music.setImageResource(R.drawable.musicoff);
                    SharedPreferences.Editor editor = musicSharedPreference.edit();
                      editor.putInt("music",0);
                     editor.apply();
                }
                else{
                    mp1.start();
                    mp1.setLooping(true);
                    music.setImageResource(R.drawable.music);
                    SharedPreferences.Editor editor = musicSharedPreference.edit();
                    editor.putInt("music",1);
                    editor.apply();
                }
            }
        });
        signInButton = findViewById(R.id.sign_in_button);
       // signOutButton = findViewById(R.id.sign_out_button);
        signInButton.setOnClickListener(this);
        //signOutButton.setOnClickListener(this);
        playGameButton=(ImageButton)findViewById(R.id.play_button);
        playGameButton.setOnClickListener(this);
       playClassicButton=(ImageButton)findViewById(R.id.button_classic);
        playClassicButton.setOnClickListener(this);
        sharedPreferences = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int shouldSignIn = sharedPreferences.getInt("SIGNIN", 1);
        getGameHelper().setMaxAutoSignInAttempts(shouldSignIn);
        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest1 = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest1);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }


    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


    @Override
    public void onSignInFailed() {
        signInButton.setVisibility(View.VISIBLE);
        //signOutButton.setVisibility(View.GONE);
        Log.e("SIGN IN", "ERROR Signin in home screen");
    }

    @Override
    public void onSignInSucceeded() {
        signInButton.setVisibility(View.GONE);
        //signOutButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button) {
            beginUserInitiatedSignIn();
        }
        /*else if (view.getId() == R.id.sign_out_button) {
            signOut();

            // save user's preference for sign-in
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("SIGNIN", 0);
            editor.apply();

            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        }*/
        if(view == playGameButton){
            firstPreference=getSharedPreferences("classic",Context.MODE_PRIVATE);
            x=firstPreference.getInt("first",1);
            if(x==1){


                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Classic Instruction");

                // Setting Dialog Message
                alertDialog.setMessage("Select light color among two colors in 20 seconds and score as fast as u can");

                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.tick);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        Intent i=new Intent(MainActivity.this,GameActivity.class);
                        startActivity(i);
                    }
                });
                SharedPreferences.Editor editor = firstPreference.edit();
                editor.putInt("first",2);
                editor.apply();

                // Showing Alert Message
                alertDialog.show();
            }
            else{
                Intent i=new Intent(MainActivity.this,GameActivity.class);
                startActivity(i);
            }

        }
        if(view==playClassicButton){
            firstPreference=getSharedPreferences("survival",Context.MODE_PRIVATE);
            x=firstPreference.getInt("first",1);
            if(x==1){
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Classic Instruction");

            // Setting Dialog Message
            alertDialog.setMessage("Select light color among two colors in 12 seconds and score as fast as u can");

            // Setting Icon to Dialog
            //alertDialog.setIcon(R.drawable.tick);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    Intent i=new Intent(MainActivity.this,gameClassicActivity.class);
                    startActivity(i);
                }
            });
                SharedPreferences.Editor editor = firstPreference.edit();
                editor.putInt("first",2);
                editor.apply();

            // Showing Alert Message
            alertDialog.show();
            }
            else{
                Intent i=new Intent(MainActivity.this,gameClassicActivity.class);
                startActivity(i);
            }

        }
        if(view==instruction){
            Intent i=new Intent(this,instructionGame.class);
            startActivity(i);
        }
        if(view==leader){
            final String appPackageName = this.getPackageName();
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "ColorMate");
                String sAux = "Color Clash is the game can help you train agility, reflexes extremely high.\n" +
                        "Can you differentiate colors very fast? Try this one!\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=" + appPackageName;
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        }

    }
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
