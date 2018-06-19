package com.dev.droopy.colourmate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements View.OnClickListener {
    private Button topBtn, bottomBtn;
    private TextView pointsTextView, levelTextView,timerProgress;
    private int level,points=0;
    private String TAG="SHUBHAM";
    private boolean gameStart=false;
    private int timer=200;
    private Runnable runnable;
    private static final int START_TIMER=200;
    private static final int FPS = 100;
    private Handler handler;
    MediaPlayer gamesound,gameover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setupProgressView();

        gamesound = MediaPlayer.create(this, R.raw.points);
        gameover = MediaPlayer.create(this, R.raw.gameover);
        topBtn=(Button)findViewById(R.id.top_button);
        bottomBtn=(Button)findViewById(R.id.bottom_button);
        Toast.makeText(this,"Tap on the lighter Colour Fast!!",Toast.LENGTH_SHORT).show();
        resetGame();
        handler=new Handler();
        setupGameLoop();
        startGame();
        topBtn.setOnClickListener(this);
        bottomBtn.setOnClickListener(this);

    }
    protected  void resetGame(){
        Log.d(TAG,"reset game");
        gameStart=false;
        level=1;
        points=0;
        pointsTextView.setText(Integer.toString(points));
        levelTextView.setText(Integer.toString(level));
        timerProgress.setText(Integer.toString(timer));
    }
    protected void startGame(){
        Log.d(TAG,"Start Game");
        gameStart=true;

        setColorsOnButtons();
        timer=START_TIMER;
        Thread thread=new Thread(runnable);
        thread.start();
    }
    protected void setupGameLoop(){
        Log.i(TAG,"SET UP GAME LOOP");
        runnable=new Runnable() {
            @Override
            public void run() {
                while(timer>0 && gameStart){
                    synchronized (this){
                        try{
                            wait(FPS);
                        }catch (InterruptedException e){
                            Log.i("Tread ERROR",e.getMessage());
                        }
                        timer=timer-1;
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            timerProgress.setText(Integer.toString(timer));
                        }
                    });
                }
                if(gameStart){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            endGame();
                        }
                    });
                }

            }
        };
    }

    protected void setupProgressView(){
        Log.d(TAG,"Set Up Progress VIEW");
        timerProgress=(TextView)findViewById(R.id.time_text);
        pointsTextView=(TextView)findViewById(R.id.points_value);
        levelTextView=(TextView)findViewById(R.id.level_value);
        timerProgress.setText(Integer.toString(timer));
        pointsTextView.setText(Integer.toString(points));
        levelTextView.setText(Integer.toString(level));
        // setting up fonts
        Typeface avenir_black = Typeface.createFromAsset(getAssets(), "fonts/avenir_black.ttf");
        //Typeface avenir_book = Typeface.createFromAsset(getAssets(), "fonts/avenir_book.ttf");
        pointsTextView.setTypeface(avenir_black);
        levelTextView.setTypeface(avenir_black);
        timerProgress.setTypeface(avenir_black);
       // pointsLabel.setTypeface(avenir_book);
       // levelsLabel.setTypeface(avenir_book);

        // setting up animations
     // AnimatorSet pointAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.points_animations);
       // pointAnim.setTarget(pointsTextView);
        //AnimatorSet levelAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.level_animations);
        //levelAnim.setTarget(levelTextView);

    }
    @Override
    public void onClick(View v) {
        Log.d(TAG,"ON click");
        if(!gameStart)return;
        calculatePoints(v);
        setColorsOnButtons();

    }
    @Override
    protected void onRestart(){
        super.onRestart();
        endGame();
    }
    @Override
    protected void onStop(){
        super.onStop();
        gameStart=false;
    }
    protected void setColorsOnButtons() {
        Log.d(TAG,"Set color on button");
        int color= Color.parseColor(chooseRandomColor.getColor());
        int red=Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        int alpha1, alpha2;
        if (Math.random() > 0.5) {
            alpha1 = 255;
            alpha2 = 185;
        } else {
            alpha1 = 185;
            alpha2 = 255;
        }
        topBtn.setBackgroundColor(Color.argb(alpha1, red, green, blue));
        bottomBtn.setBackgroundColor(Color.argb(alpha2, red, green, blue));
    }

    protected void calculatePoints(View clickedView) {
        Log.d(TAG,"Calculate points");
        View unclickedView = (clickedView == topBtn )? bottomBtn : topBtn;
        ColorDrawable clickedColor = (ColorDrawable) clickedView.getBackground();
        ColorDrawable unClickedColor = (ColorDrawable) unclickedView.getBackground();

        int alpha1 = Color.alpha(clickedColor.getColor());
        int alpha2 = Color.alpha(unClickedColor.getColor());

        // correct guess
        if (alpha1 < alpha2) {
            updatePoints();
            gamesound.start();
        } else { // incorrect guess
            gameover.start();
            endGame();

        }
    }


    private void launchGameOver(int highScore){
        Intent i=new Intent(this,GameOverActivity.class);
        i.putExtra("points",points);
        i.putExtra("level",level);
        i.putExtra("best",highScore);
        i.putExtra("newScore",highScore==points);
        startActivity(i);
    }


    protected void endGame(){
        if(points<500){
            gameStart=false;
            int highScore=SaveAndGetHighScore();
            finish();
            launchGameOver(highScore);
            finish();
        }
        else{
            Intent i=new Intent(this,gamelev2.class);
            startActivity(i);
        }

    }
    private int SaveAndGetHighScore(){
        SharedPreferences preferences=this.getSharedPreferences("com.droopy.colormate.classic", Context.MODE_PRIVATE );
        int highScore=preferences.getInt("HIGHSCORE",0);
        if(points>highScore){
            SharedPreferences.Editor editor=preferences.edit();
            editor.putInt("HIGHSCORE",points);
            editor.apply();
            highScore=points;
        }
        return highScore;
    }
    public void updatePoints(){
        Log.d(TAG,"Update pts");
        points=points+20;
         timer+=1;
        pointsTextView.setText(Integer.toString(points));
        levelTextView.setText(Integer.toString(level));
        timerProgress.setText(Integer.toString(timer));
        //level change
        if(points>=500){
            Intent i=new Intent(this,gamelev2.class);
            finish();
            startActivity(i);
            finish();
        }
    }
}
