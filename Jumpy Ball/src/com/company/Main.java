package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class Main extends JPanel implements KeyListener {
    Ball b = new Ball(100,100,50);
    Wall[] arr = new Wall[5];
    private HashSet<String> keys = new HashSet<String>();
    private volatile boolean spacepress, rpress, dead = false, start = false;
    private int score = 0;
    private static BufferedImage jb;
    private static BufferedImage gameover;
    int t = 0;

    public Main () {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        try {
            jb = ImageIO.read(new File("jumpy ball.png"));
            gameover = ImageIO.read(new File("gameover.png"));
        } catch (IOException e) {
            //Not handled.
        }
        for (int h = 0; h < 5; h++) {
            arr[h] = new Wall(1700 + h*400);
        }
        Thread thread = new Thread() {
            public void run() {
                int n;
                for (n = 1; n < 10; n++) {
                    n = 1;
                    starter();
                    resetter();
                    if (!dead && start) {
                        repaint();
                        b.gravity();
                        for (int h = 0; h < 5; h++) {
                            arr[h].moving();
                            arr[h].shift();
                            arr[h].createrect();
                            if (arr[h].isScored()) {
                                score++;
                            }
                        }
                        jumped();
                        intersects();
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

    public void starter () {
        if (rpress) {
            start = true;
        }
    }

    public void jumped () {
        if(spacepress) {
            for (int i = 0; i < 1; i++) {
                b.setAcc(-10);
            }
        }
    }

    public void resetter() {
        if (dead && rpress) {
            score = 0;
            dead = false;
            start = false;
            b.setX(100);
            b.setY(100);
            b.setT(100);
            for (int h = 0; h < 5; h++) {
                arr[h] = new Wall(1700 + h*400);
            }
        }
    }

    public void intersects() {
        Rectangle circ = new Rectangle(b.getX(),b.getY(),b.getR(),b.getR());
        for (int h = 0; h < 5; h++) {
            if (circ.intersects(arr[h].getRect1()) || circ.intersects(arr[h].getRect2())) {
                dead = true;
            }
        }
        if (b.getY() >= 700 || b.getY() < 0) {
            dead = true;
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
        if (!start) {
            g.drawImage(jb,0,0,1450,770,null);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Monospaced", Font.BOLD, 50));
            g.drawString("PRESS R TO PLAY", 500, 500);
        }
        if (start) {
            g.setColor(Color.DARK_GRAY);
            g.setFont(new Font("Monospaced", Font.BOLD, 850));
            if (score < 10) {
                g.drawString(score + "", 500, 675);
            } else if (score < 100) {
                g.drawString(score + "", 200, 675);
            } else {
                g.drawString(score + "", -60, 675);
            }
            g.setColor(Color.BLACK);
            g.fillOval(b.getX(), b.getY(), b.getR(), b.getR());
            g.setColor(Color.RED);
            for (int h = 0; h < 5; h++) {
                g.fillRect(arr[h].getX(), 0, arr[h].getW(), arr[h].getH());
                g.fillRect(arr[h].getX(), arr[h].getH2(), arr[h].getW(), 800);
            }
            //g.setColor(Color.green);
            //g.drawRect(b.getX(), b.getY(), b.getR(), b.getR());
            //for (int h = 0; h < 5; h++) {
            //    g.drawRect(arr[h].getX(), 0, arr[h].getW(), arr[h].getH());
            //    g.drawRect(arr[h].getX(), arr[h].getH2(), arr[h].getW(), 800);
           // }
        }
        if (dead) {
            g.drawImage(gameover, 0, 0, 1450, 770, null);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Monospaced", Font.BOLD, 40));
            g.drawString("PRESS R TO RESTART", 530, 550);
        }
    }


    public static void main(String[] args) {
	// write your code here
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Jumpy Ball");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1450, 770);
        frame.add(new Main());
        frame.setVisible(true);
    }
}
