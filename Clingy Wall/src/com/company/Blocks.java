package com.company;

import java.awt.Rectangle;

public class Blocks {
    private int x;
    private int y;
    private int r;
    private Rectangle rect;

    public Blocks (int inx, int iny, int inr) {
        x = inx;
        y = iny;
        r = inr;
        rect = new Rectangle(x, y, r, r);
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

    public Rectangle getRect() {
        return rect;
    }
}
