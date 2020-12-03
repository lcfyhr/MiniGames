package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.awt.*;
import java.util.Random;
import java.awt.Rectangle;

public class Main extends JPanel implements KeyListener {

    private HashSet<String> keys = new HashSet<String>();
    int getoff = 0;
    private volatile boolean spacepress = false, rpress;
    Ball p1 = new Ball(200, 700, 20, true, false);
    private volatile boolean runonce = true, sliding = false, oncube = false, moving = true, undcube = false, jumpable = true;
    Random rand = new Random();
    private BufferedImage jumpanim1, jumpanim2, jumpanim3, jumpanim4, charimg;
    Platforms[] arr = new Platforms[40];
    Blocks[] arrbl = new Blocks[80];

    public Main() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        genArr();
        p1.setY(p1.getBottom());
        try {
            charimg = ImageIO.read(new File("charimg.png"));
            jumpanim1 = ImageIO.read(new File("jumpanim1.png"));
            jumpanim2 = ImageIO.read(new File("jumpanim2.png"));
            jumpanim3 = ImageIO.read(new File("jumpanim3.png"));
            jumpanim4 = ImageIO.read(new File("jumpanim4.png"));
        } catch (IOException e) {
            //Not handled.
        }
        Thread thread = new Thread() {
            public void run() {
                int n;
                for (n = 1; n < 10; n++) {
                    n = 1;
                    repaint();
                    if (!sliding) {
                        oncube();
                    }
                    if (!oncube) {
                        slider();
                    }
                    if (!sliding && !oncube && moving) {
                        p1.mover();
                    }
                    undcube();
                    if (oncube) {
                        if (p1.isWr()) {
                            p1.setX(p1.getX() + 3);
                        } else {
                            p1.setX(p1.getX() - 3);
                        }
                    }
                    p1.bounds();
                    jumped();
                    intersects();
                    if ((p1.getX() > 50 && p1.getX() < 450 - p1.getR()) && !sliding && !oncube) {
                        p1.gravity();
                    } else {
                        if (p1.getY() < p1.getBottom() && sliding) {
                            p1.setY(p1.getY() + 2);
                            p1.setAcc(0);
                        }
                        if (p1.getX() == 50 || p1.getX() == 450 - p1.getR()) {
                            if (p1.getY() < p1.getBottom()) {
                                p1.setY(p1.getY() + 2);
                            }
                            if (p1.getY() >= p1.getBottom()) {
                                p1.setAcc(0);
                            }
                        }
                    }

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        };
        thread.start();
    }

    public void genArr () {
        for (int h = 0; h < 39; h++) {
            int inx = 0;
            int inw = 500;
            int inh = 10;
            int iny = (700 + p1.getR()) - h*1200;
            arr[h] = new Platforms(inx, inw, inh, iny);
        }
        for (int h = 0; h < 79; h +=2) {
            int inx = rand.nextInt(201) + 100;
            int inx1 = inx + 50;
            int iny = (350 + p1.getR()) - (int) (Math.ceil(h/2))*500 - (int) (Math.ceil(h/4))*200;
            int inr = 50;
            arrbl[h] = new Blocks(inx, iny, inr);
            arrbl[h + 1] = new Blocks(inx1, iny, inr);
        }
    }

    public void oncube() {
        Rectangle thecube = new Rectangle ();
        Rectangle player = new Rectangle(p1.getX() - 1, p1.getY() - 1, p1.getR() + 2, p1.getR() + 2);
        for (int h = p1.getFlr(); h < (p1.getFlr() * 4) + 4; h++) {
            Rectangle top = new Rectangle(arrbl[h].getX() + 1, arrbl[h].getY() - 10, arrbl[h].getR() - 2, 11 );
            if (player.intersects(top) && p1.getY() < arrbl[h].getY() + 30) {
                oncube = true;
                p1.setY(arrbl[h].getY() - p1.getR());
                thecube = top;
                if (!spacepress) {
                    getoff = 1;
                }
                if (p1.getX() <= 50) {
                    p1.setWr(true);
                }
                if (p1.getX() >= 450 - p1.getR()) {
                    p1.setWl(true);
                }
            }
            if (getoff == 1 && spacepress) {
                oncube = false;
            }
        }
        if (!player.intersects(thecube)) {
            oncube = false;
        }
        if (oncube) {
            p1.setAcc(0);
            sliding = false;
            p1.setJct(2);
        } else {
            getoff = 0;
        }
    }

    private void undcube () {
        Rectangle thecube = new Rectangle();
        Rectangle player = new Rectangle(p1.getX() - 1, p1.getY() - 1, p1.getR() + 2, p1.getR() + 2);
        for (int h = p1.getFlr(); h < (p1.getFlr() * 4) + 4; h++) {
            Rectangle bottom = new Rectangle(arrbl[h].getX() + 5, arrbl[h].getY() + arrbl[h].getR(), arrbl[h].getR() - 10, 1);
            if (player.intersects(bottom)) {
                p1.setY(arrbl[h].getY() + arrbl[h].getR());
                p1.setAcc(3);
                undcube = true;
                thecube = new Rectangle(arrbl[h].getX() - 5, arrbl[h].getY() + arrbl[h].getR(), arrbl[h].getR() + 10, 1);
            }
        }
        if (!player.intersects(thecube)) {
            undcube = false;
        }
    }

    public void slider () {
        Rectangle thecube = new Rectangle();
        Rectangle player = new Rectangle(p1.getX() - 1, p1.getY() - 1, p1.getR() + 2, p1.getR() + 2);
        for (int h = p1.getFlr(); h < (p1.getFlr() * 4) + 4; h++) {
            Rectangle arrectr = new Rectangle(arrbl[h].getX() + arrbl[h].getR(), arrbl[h].getY() + 10, 1, arrbl[h].getR() - 20);
            Rectangle arrectl = new Rectangle( arrbl[h].getX(), arrbl[h].getY() + 10, 1, arrbl[h].getR() - 20);
            if (player.intersects(arrectr) || player.intersects(arrectl)) {
                sliding = true;
                moving = false;
                p1.setAcc(0);
                if (player.intersects(arrectr)) {
                    thecube = arrectr;
                    p1.setX(arrbl[h].getX() + arrbl[h].getR());
                    p1.setWr(true);
                }
                if (player.intersects(arrectl)) {
                    thecube = arrectl;
                    p1.setX(arrbl[h].getX() - p1.getR());
                    p1.setWl(true);
                }
            }
            if (spacepress && sliding) {
                sliding = false;
                moving = true;
            }
        }
        if (!player.intersects(thecube)) {
            sliding = false;
        }
        if (p1.getY() + p1.getR() >= p1.getBottom()) {
            moving = true;
        }
    }

    public void intersects() {
        for (int h = p1.getFlr() + 1; h < 39; h++) {
            if (p1.getY() + p1.getR() < arr[h].getY()) {
                p1.setFlr(p1.getFlr() + 1);
                p1.setBottom(arr[h].getY() - p1.getR());
            }
        }
    }

    public void jumped () {
        Rectangle bottom = new Rectangle();
        Rectangle player = new Rectangle(p1.getX() - 1, p1.getY() - 1, p1.getR() + 2, p1.getR() + 2);
        for (int h = p1.getFlr(); h < (p1.getFlr() * 4) + 4; h++) {
            Rectangle thecube = new Rectangle (arrbl[h].getX() + 5, arrbl[h].getY() + arrbl[h].getR(), arrbl[h].getR() - 10,20 );
            if (player.intersects(thecube)) {
                jumpable = false;
                bottom = thecube;
            }
            if (!player.intersects(bottom)) {
                jumpable = true;
            }
        }
        if (spacepress) {
            moving = true;
        }
        if (p1.getY() == p1.getBottom() || sliding) {
            p1.setJct(2);
        }
        if (runonce) {
            if (spacepress) {
                if (p1.getX() == 50) {
                    p1.setWr(true);
                }
                if (p1.getX() == 450 - p1.getR()) {
                    p1.setWl(true);
                }
                if (p1.getJct() > 0 && jumpable) {
                    for (int i = 0; i < 1; i++) {
                        p1.setAcc(-13);
                    }
                }
                p1.setJct(p1.getJct()-1);
                runonce = false;
                }
            }
        if (!spacepress) {
            runonce = true;
        }
    }

    public void keyTyped (KeyEvent e) {}

    public void keyPressed (KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_SPACE:
                spacepress = true;
                break;
            case KeyEvent.VK_R:
                rpress = true;
                break;
        }
    }

    public void keyReleased (KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_SPACE:
                spacepress = false;
                break;
            case KeyEvent.VK_R:
                rpress = false;
                break;
        }
    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        int dy = (500 - p1.getY());
        for (int h = 0; h < 39; h++) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(arr[h].getX(), arr[h].getY() + dy, arr[h].getWidth(), arr[h].getHeight());
        }
        for (int h = 0; h < 79; h++) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(arrbl[h].getX(), arrbl[h].getY() + dy, arrbl[h].getR(), arrbl[h].getR());
        }
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, -3200, 50, 4000);
        g.fillRect(450, -3200, 50, 4000);
        g.setColor(Color.BLACK);
        g.fillRect(p1.getX(), p1.getY() + dy, p1.getR(), p1.getR());
        if (p1.getJct() == 0) {
            int i = 0;
            i++;
            if (i == 4) {
                i = 0;
            }
            if (i == 0) {
                g.drawImage(jumpanim1, p1.getX(), p1.getY() + dy, 20, 20, null);
            }
            if (i == 1) {
                g.drawImage(jumpanim2, p1.getX(), p1.getY() + dy, 20, 20, null);
            }
            if (i == 2) {
                g.drawImage(jumpanim3, p1.getX(), p1.getY() + dy, 20, 20, null);
            }
            if (i == 3) {
                g.drawImage(jumpanim4, p1.getX(), p1.getY() + dy, 20, 20, null);
            }
        } else {
            g.drawImage(charimg, p1.getX(), p1.getY(), 20, 20, null);
        }
        if (jumpable) {
            g.setColor(Color.RED);
            g.fillOval(20, p1.getY() + dy, 20, 20);
        } else {
            g.setColor(Color.GREEN);
            g.fillOval(20, p1.getY() + 20 + dy, 20, 20);
        }
        g.setColor(Color.RED);
        g.drawRect(p1.getX() - 1, p1.getY() - 1 + dy, p1.getR() + 2, p1.getR() + 2);
        for (int h = p1.getFlr(); h < (p1.getFlr() * 4) + 4; h++) {
            g.drawRect(arrbl[h].getX() + 5, arrbl[h].getY() + arrbl[h].getR() + dy, arrbl[h].getR() - 10,1);
            g.drawRect(arrbl[h].getX() - 10, arrbl[h].getY() + dy, arrbl[h].getR() + 20, 1);
            g.drawRect(arrbl[h].getX(), arrbl[h].getY() + 10 + dy, 1, arrbl[h].getR() - 20);
            g.drawRect(arrbl[h].getX() + arrbl[h].getR(), arrbl[h].getY() + 10 + dy, 1, arrbl[h].getR() - 20);
        }
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Clingy Wall");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 770);
        frame.add(new Main());
        frame.setVisible(true);
    }
}
