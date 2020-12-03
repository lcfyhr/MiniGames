package com.company;

import org.w3c.dom.css.Rect;

import java.util.Random;
import java.awt.Rectangle;

public class Wall {

    private int x;
    private int h;
    private int h2;
    private int w;
    Random rand = new Random();
    private volatile boolean scored = false;
    Rectangle rect1, rect2;

    public Wall(int inx) {
        x = inx;
        h = rand.nextInt(350) + 100;
        h2 = h + 250;
        w = 100;
    }

    public void createrect() {
        rect1 = new Rectangle(x, 0, w, h);
        rect2 = new Rectangle(x, h2, w, 800);
    }

    public void moving() {
        x -= 5;
    }

    public void shift() {
        if (x < -100) {
            x += 2000;
            h = rand.nextInt(350) + 100;
            h2 = h + 250;
            scored = true;
        } else {
            scored = false;
        }
    }

    public int getX() {
        return (x);
    }

    public int getH() {
        return h;
    }

    public int getH2() {
        return h2;
    }

    public int getW() {
        return w;
    }

    public boolean isScored() {
        return scored;
    }

    public Rectangle getRect1() {
        return rect1;
    }

    public Rectangle getRect2() {
        return rect2;
    }
}

