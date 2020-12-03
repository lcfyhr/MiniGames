package com.company;

import java.awt.*;

public class Ball {

    private int x;
    private int y;
    private int r;
    private int dt = 150, acc = 0, vel = 0;
    private int bottom = 700 + r;
    private int flr;
    private int jct;
    private volatile boolean wr, wl, falling;
    private Rectangle rect;

    public Ball(int inx, int iny, int inr, boolean inwr, boolean inwl) {
        x = inx;
        y = iny;
        r = inr;
        wr = inwr;
        wl = inwl;
        flr = 0;
        rect = new Rectangle(x,y,r,r);
    }

    public void mover() {
        if (wr) {
            x += 5;
        } else if (wl) {
            x -= 5;
        }
    }

    public void bounds () {
        if (x > 450 - r) {
            x = 450 - r;
            if (y == bottom) {
                wl = true;
                wr = false;
            } else {
                jct = 2;
            }
        }
        if (x < 50) {
            x = 50;
            if (y == bottom) {
                wr = true;
                wl = false;
            } else {
                jct = 2;
            }
        }
        if (y > bottom) {
            y = bottom;
        }
    }

    public void gravity() {
            dt++;
            if (dt >= 200) {
                dt = 200;
            }
            if (dt >= 150) {
                if (acc <= 10) {
                    acc++;
                }
                vel = acc * (dt - 65) / 45;
                y += vel;
                if (acc == 0) {
                    dt = 150;
                }
            }
            if (y > bottom) {
                y = bottom;
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

    public int getAcc() {
        return acc;
    }

    public int getFlr() {
        return flr;
    }

    public int getBottom () {
        return bottom;
    }

    public int getJct() {
        return jct;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setFlr(int flr) {
        this.flr = flr;
    }

    public void setJct(int jct) {
        this.jct = jct;
    }

    public void setWr (boolean direction) {
        wr = direction;
        wl = false;
    }

    public void setWl (boolean direction) {
        wl = direction;
        wr = false;
    }

    public void setFall (boolean infalling) {
        falling = infalling;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }

    public void setBottom(int bottom1) {
        bottom = bottom1;
    }

    public boolean isWl() {
        return wl;
    }

    public boolean isWr() {
        return wr;
    }

    public boolean isFalling() {
        return falling;
    }

    public Rectangle getRect() {
        return rect;
    }
}
