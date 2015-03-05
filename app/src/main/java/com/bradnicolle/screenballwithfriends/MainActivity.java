package com.bradnicolle.screenballwithfriends;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends Activity implements SensorEventListener {
    private static final String TAG = "ScreenBall";

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private Handler mHandler;
    private Runnable mTickRunnable = new Runnable() {
        @Override
        public void run() {
            if (mGameActive) {
                mScore++;
                mScoreView.setText("SCORE: " + mScore);
                mGameActive = !(mBall.x < -mGameView.getWidth()/2 || mBall.x > mGameView.getWidth()/2
                        || mBall.y < -mGameView.getHeight()/2 || mBall.y > mGameView.getHeight()/2);
                mBall.applyImpulse((mRandom.nextFloat() - 0.5f) * mScore * 0.1f, (mRandom.nextFloat() - 0.5f) * mScore * 0.1f, 0.05f);
                mGameView.invalidate();
                mHandler.postDelayed(this, 100);
            }
            else {
                mRestartView.setVisibility(View.VISIBLE);
                mScoreView.setText("GAME OVER!\nSCORE: " + mScore);
            }
        }
    };

    private GameView mGameView;
    private TextView mScoreView;
    private TextView mRestartView;

    private List<Ball> mBalls = new ArrayList<Ball>();
    private Ball mBall;

    private Random mRandom = new Random();

    private int mScore = 0;
    private long mPrevTime = 0;
    private boolean mGameActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGameView = (GameView)findViewById(R.id.game_view);
        mScoreView = (TextView)findViewById(R.id.score_view);
        mRestartView = (TextView)findViewById(R.id.restart_text);
        mRestartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        restartGame();
    }

    private void restartGame() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mTickRunnable);
        }

        mRestartView.setVisibility(View.GONE);

        mBalls.clear();
        mBall = new Ball("Brad", Color.WHITE, 0, 0);
        mBalls.add(mBall);
        mGameView.setBalls(mBalls);

        mScore = 0;
        mGameActive = true;

        mHandler = new Handler();
        mHandler.postDelayed(mTickRunnable, 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mGameActive) {
            mBall.applyImpulse(-event.values[0], event.values[1], 0.05f);
            mPrevTime = event.timestamp;
            mGameView.invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    /*
     * Set flags to enable full-screen 'immersion' with sliding status bar and nav bar.
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
