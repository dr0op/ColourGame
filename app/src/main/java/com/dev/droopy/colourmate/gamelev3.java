package com.dev.droopy.colourmate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class gamelev3 extends Activity implements View.OnClickListener {
    private ArrayList<Button> buttonList;
    private TextView pointsTextView, levelTextView,timerProgress;
    private int level,points=0;
    private String TAG="SHUBHAM123";
    private boolean gameStart=false;
    private int timer=200;
    private Runnable runnable1;
    private static final int START_TIMER=200;
    private static final int FPS = 100;
    private Handler handler1;
    private Button button_1,button_2,button_3,button_4,button_5,button_6;
    MediaPlayer gamesound,gameover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamelev3);
        gamesound = MediaPlayer.create(this, R.raw.points);
        gameover = MediaPlayer.create(this, R.raw.gameover);
        handler1=new Handler();
        setupProgressView();
        button_1 = (Button) findViewById(R.id.button_1);
        button_2 = (Button) findViewById(R.id.button_2);
        button_3 = (Button) findViewById(R.id.button_3);
        button_4 = (Button) findViewById(R.id.button_4);
        button_5 = (Button) findViewById(R.id.button_5);
        button_6 =(Button)findViewById(R.id.button_6);


        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);
        button_3.setOnClickListener(this);
        button_4.setOnClickListener(this);
        button_5.setOnClickListener(this);
        button_6.setOnClickListener(this);

        buttonList = new ArrayList<Button>();
        buttonList.add(button_1);
        buttonList.add(button_2);
        buttonList.add(button_3);
        buttonList.add(button_4);
        buttonList.add(button_5);
        buttonList.add(button_6);

        // bootstrap game
        resetGame();
        setupGameLoop();
        startGame();
    }
    protected void setupProgressView() {
        timerProgress=(TextView)findViewById(R.id.time_text);
        pointsTextView=(TextView)findViewById(R.id.points_value);
        levelTextView=(TextView)findViewById(R.id.level_value);
        timerProgress.setText(Integer.toString(timer));
        pointsTextView.setText(Integer.toString(points));
        levelTextView.setText(Integer.toString(level));

    }
    protected void setColorsOnButtons() {
        int color  = Color.parseColor(chooseRandomColor.getColor());
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int[] alphas = shuffledColors();

        for (int i = 0; i < alphas.length; i++) {
            Button button = buttonList.get(i);
            button.setBackgroundColor(Color.argb(alphas[i], red, green, blue));
        }
    }

    protected void calculatePoints(View clickedView) {
        ColorDrawable clickedColor = (ColorDrawable) clickedView.getBackground();
        int clickedAlpha = Color.alpha(clickedColor.getColor());

        int lightestColor = clickedAlpha;
        for (Button button : buttonList) {
            ColorDrawable color = (ColorDrawable) button.getBackground();
            int alpha = Color.alpha(color.getColor());
            if (alpha < lightestColor) {
                lightestColor = alpha;
            }
        }

        // correct guess
        if (lightestColor == clickedAlpha) {
            updatePoints();
            gamesound.start();
        } else {
            // wrong guess
            gameover.start();
            endGame();
        }
    }

    @Override
    public void onClick(View view) {
        if (!gameStart) return;
        calculatePoints(view);
        setColorsOnButtons();
    }


    // Fisher Yates shuffling algorithm
    private int[] shuffledColors() {
        Random random = new Random();
        int[] arr = {255, 185, 155, 225,200,170};
        for (int i = arr.length - 1; i >= 1; i--) {
            int j = random.nextInt(i);
            // swap i and j
            int tmp;
            tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        return arr;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        endGame();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameStart = false;
    }

    protected void setupGameLoop() {
        runnable1 = new Runnable() {
            @Override
            public void run() {
                while (timer > 0 && gameStart) {
                    synchronized (this) {
                        try {
                            wait(FPS);
                        } catch (InterruptedException e) {
                            Log.i("THREAD ERROR", e.getMessage());
                        }
                        timer = timer -1;
                    }
                    handler1.post(new Runnable() {
                        @Override
                        public void run() {
                            timerProgress.setText(Integer.toString(timer));
                        }
                    });
                }
                if (gameStart) {
                    handler1.post(new Runnable() {
                        @Override
                        public void run() {
                            endGame();
                        }
                    });
                }
            }
        };

    }

    protected void resetGame() {
        gameStart = false;
        //set initials for the level 3
        level =3;
        points =1000;
        timer=200;

        // update view
        pointsTextView.setText(Integer.toString(points));
        levelTextView.setText(Integer.toString(level));
        timerProgress.setText(Integer.toString(timer));
    }

    protected void startGame() {
        gameStart = true;

        Toast.makeText(this, R.string.game_help, Toast.LENGTH_SHORT).show();
        setColorsOnButtons();

        // start timer
        timer = START_TIMER;
        Thread thread = new Thread(runnable1);
        thread.start();
    }

    protected void endGame() {
        gameStart = false;
        int highScore = saveAndGetHighScore();
        finish();
        launchGameOver(highScore);
        finish();
    }

    private int saveAndGetHighScore() {
        SharedPreferences preferences = this.getSharedPreferences(
                "com.droopy.colormate.classic", Context.MODE_PRIVATE);

        int highScore = preferences.getInt("HIGHSCORE", 0);

        if (points > highScore) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("HIGHSCORE", points);
            editor.apply();
            highScore = points;
        }
        return highScore;
    }

    private void launchGameOver(int highScore) {
        // Send data to another activity
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra("points", points);
        intent.putExtra("level", level);
        intent.putExtra("best", highScore);
        intent.putExtra("newScore", highScore == points);
        finish();
        startActivity(intent);

    }

    // called on correct guess
    public void updatePoints() {
        points = points + 40;
        pointsTextView.setText(Integer.toString(points));
        timer+=2;
        if (points>1500) {
            Intent i=new Intent(this,gamelev4.class);
            finish();
            startActivity(i);
            finish();
        }
    }


}
