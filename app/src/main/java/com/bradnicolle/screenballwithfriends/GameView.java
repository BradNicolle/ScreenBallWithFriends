package com.bradnicolle.screenballwithfriends;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brad on 02/03/2015.
 */
public class GameView extends View {
    private Paint mPaint;
    private List<Ball> mBalls;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBalls = new ArrayList<Ball>();
        setBackgroundColor(Color.BLACK);
    }

    public void setBalls(List<Ball> balls) {
        mBalls = balls;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Place (0, 0) in centre of screen
        canvas.translate(getWidth()/2, getHeight()/2);

        for (Ball b : mBalls) {
            mPaint.setColor(b.color);
            canvas.drawCircle(b.x, b.y, b.radius, mPaint);
        }
    }

}
