package com.company;

public class Ball {

    private int x;
    private int y;
    private int r;
    private int vel = 0;
    private int acc = 10;
    private int t = 0;

    public Ball (int inx, int iny, int inr) {
        x = inx;
        y = iny;
        r = inr;
    }

    public void gravity() {
        t++;
        if (t >= 200) {
            t = 200;
        }
        if (t >= 75) {
            if (acc <= 10) {
                acc++;
            }
            vel = acc * (t-50) / 40;
            y += vel;
            if (acc == 0) {
                t = 100;
            }
            if (y > 700) {
                y = 700;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public int getVel() {
        return vel;
    }

    public int getAcc() {
        return acc;
    }

    public int getT() {
        return t;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }

    public void setT(int inint) {
        t = inint;
    }
}
