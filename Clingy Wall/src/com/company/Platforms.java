package com.company;

import java.awt.Rectangle;

public class Platforms {

    private int width;
    private int x;
    private int y;
    private int height;
    private Rectangle rect;

    public Platforms (int inx, int inWidth, int inHeight, int iny) {
        width = inWidth;
        height = inHeight;
        x = inx;
        y = iny;
        rect = new Rectangle(x,y,width + 50,height);
    }

    public void setY (int iny) {
        y = iny;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getRect() {
        return rect;
    }
}
