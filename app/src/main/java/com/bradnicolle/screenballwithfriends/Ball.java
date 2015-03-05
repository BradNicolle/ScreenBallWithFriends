package com.bradnicolle.screenballwithfriends;

import android.graphics.Color;

/**
 * Created by Brad on 02/03/2015.
 */
public class Ball {
    public String owner = "";
    public int color = Color.WHITE;
    public float radius = 25;
    public float x, y;
    public float vx, vy;

    public Ball(String owner, int color, float x, float y) {
        this.owner = owner;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public void applyImpulse(float ax, float ay, float dt) {
        vx += ax;
        vy += ay;
        x += vx * dt;
        y += vy * dt;
    }

}
