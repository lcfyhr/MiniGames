package com.company;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Main extends JPanel implements KeyListener {

    private static BufferedImage charimage;
    private static BufferedImage charimage2;
    private static BufferedImage charimage3;
    private static BufferedImage backimage;
    private static BufferedImage jumpimage;
    private static BufferedImage thwompimage;
    private static BufferedImage coinimage;
    private static BufferedImage bullb;
    private HashSet<String> keys = new HashSet<String>();
    private volatile boolean leftpressed;
    private volatile boolean rightpressed;
    private volatile boolean uppressed;
    private volatile boolean enterpress;
    private volatile boolean rpress;
    private static int width = 1480;
    private static int height = 800;
    private int floor = 680;
    private int mx = 100;
    private int mh = 80;
    private int my = floor - mh;
    private int mw = 40;
    private int jumpcount = 1;
    private int jumpphase = 16;
    private int highscore;
    private int score = 0;
    private volatile boolean running = true;
    private volatile boolean jumping = true;
    private volatile boolean thwompready = true;
    private volatile boolean thwompfall = false;
    private volatile boolean thwompreset = false;
    private volatile boolean continueanim;
    private volatile boolean gathered = false;
    private int charphase = 1;
    private int bx = 1600, by = floor - 100 , bw = -90, bh = 80;
    private int tx = 1600, ty = floor - 650 , tw = 120, th = 160;
    private int cx = 1200, cr = 20, cy = my + cr;
    private int speed = 10;
    private volatile boolean playing;
    private int hit = 0;
    private int newn = 0;
    private volatile boolean gameover = false;
    private volatile boolean gamestart = false;

    public Main() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        try {
            backimage = ImageIO.read(new File("overworld_bg.png"));
            charimage = ImageIO.read(new File("MarioWalk1.png"));
            charimage2 = ImageIO.read(new File("MarioWalk3.png"));
            charimage3 = ImageIO.read(new File("MarioWalk7.png"));
            jumpimage = ImageIO.read(new File("MarioJump.png"));
            bullb = ImageIO.read(new File("BulletBill.png"));
            thwompimage = ImageIO.read(new File("Thwomp.png"));
            coinimage = ImageIO.read(new File("Coin.png"));
        } catch (IOException e) {
            //Not handled.
        }
        updates();
    }

    public int getMx() {
        return(mx);
    }

    public int getMy() {
        return(my);
    }

    public int getMw() {
        return(mw);
    }

    public void gamestarter () {
        if (enterpress) {
            gamestart = true;
        }
    }

    public void movements () {
        if (rightpressed && !leftpressed && mx <= 1390) {
            running = true;
            mx = mx + Math.abs(mw / 3);
        }
        if (rightpressed && mw < 0) {
            mx = mx - Math.abs(mw);
            mw *= -1;
        }
        if (rightpressed && leftpressed) {
            running = false;
        }
        if (leftpressed && !rightpressed && mx >= 47) {
            running = true;
            mx = mx - Math.abs(mw / 3);
        }
        if (leftpressed && mw > 0) {
            mx = mx + Math.abs(mw);
            mw *= -1;
        }
        if (!leftpressed && !rightpressed) {
            running = false;
        }
        if (uppressed && jumpcount == 1) {
            jumpcount = 0;
            jumping = true;
        }
        if (running) {
            if (charphase == 1) {
                charphase = 2;
            } else if (charphase == 2) {
                charphase = 3;
            } else if (charphase == 3) {
                charphase = 4;
            } else if (charphase == 4) {
                charphase = 1;
            }
        }
        if (!running) {
            charphase = 1;
        }
        if (jumping) {
            if (jumpphase == 0) {
                jumpphase = 1;
                my = my - 50;
            } else if (jumpphase == 1) {
                jumpphase = 2;
                my = my - 35;
            } else if (jumpphase == 2) {
                jumpphase = 3;
                my = my - 25;
            } else if (jumpphase == 3) {
                jumpphase = 4;
                my = my - 18;
            } else if (jumpphase == 4) {
                jumpphase = 5;
                my = my - 13;
            } else if (jumpphase == 5) {
                jumpphase = 6;
                my = my - 9;
            } else if (jumpphase == 6) {
                jumpphase = 7;
                my = my - 5;
            } else if (jumpphase == 7) {
                jumpphase = 8;
                my = my - 2;
            } else if (jumpphase == 8) {
                jumpphase = 9;
                my = my + 2;
            } else if (jumpphase == 9) {
                jumpphase = 10;
                my = my + 5;
            } else if (jumpphase == 10) {
                jumpphase = 11;
                my = my + 9;
            } else if (jumpphase == 11) {
                jumpphase = 12;
                my = my + 13;
            } else if (jumpphase == 12) {
                jumpphase = 13;
                my = my + 18;
            } else if (jumpphase == 13) {
                jumpphase = 14;
                my = my + 25;
            } else if (jumpphase == 14) {
                jumpphase = 15;
                my = my + 35;
            } else if (jumpphase == 15) {
                jumpphase = 16;
                my = my + 50;
            }
        }
        if (my >= floor - mh - 5) {
            my = floor - mh;
            jumpcount = 1;
            jumpphase = 0;
            jumping = false;
        }
        if (mx > 200) {
            playing = true;
        }
        if (playing) {
            bx = bx + (bw/Math.abs(bw)) * speed;
            if (bx < -120 || bx > 1600) {
                bw *= -1;
            }
            if (speed < 50 && gathered) {
                speed = speed + 2;
            } else if (speed > 50) {
                speed = 50;
            }
        }
    }

    public void Thwomping () {
        if (playing) {
            if (tx > mx && thwompready) {
                tx = tx - 10;
            } else if (tx < mx && thwompready) {
                tx = tx + 10;
            }
            if (mx > tx && mx < (tx + tw) && thwompready) {
                thwompfall = true;
                thwompready = false;
            }
            if (thwompfall) {
                ty = ty + 13;
            }
            if (thwompfall && ty > floor - th) {
                thwompfall = false;
                thwompreset = true;
            }
            if (thwompreset && ty != floor - 650) {
                ty = ty - 8;
            }
            if (ty < floor - 640) {
                thwompready = true;
                thwompreset = false;
            }
        }
    }

    public void newCoins () {
        Random rand = new Random();
        if (gathered) {
            if (mx > width / 2) {
                cx = rand.nextInt(width/2 - 100) + 100;
            } else {
                cx = rand.nextInt((width - 100) - width/2) + width/2;
            }
        }
    }

    public void Scoring () throws FileNotFoundException {
        File scorefile = new File("Highscore.txt");
        Scanner FileScore = new Scanner(scorefile);
        if (!gamestart) {
            highscore = FileScore.nextInt();
            FileScore.close();
        }
        if (gameover) {
            PrintWriter scorefileout = new PrintWriter(scorefile);
            scorefileout.println(highscore);
            scorefileout.close();
        }
        if (gathered) {
            score++;
        }
        if (score > highscore) {
            highscore = score;
        }
    }

    public void Colliding () {
        Rectangle mhbf = new Rectangle(mx, my, mw, mh);
        Rectangle mhbb = new Rectangle(mx + mw, my, Math.abs(mw), mh);
        Rectangle bb = new Rectangle(bx + bw, by, Math.abs(bw), bh);
        Rectangle bbf = new Rectangle(bx, by, bw, bh);
        Rectangle tt = new Rectangle(tx, ty, tw, th);
        Rectangle cc = new Rectangle(cx, cy, cr, cr);

        if (mw > 0) {
            if (mhbf.intersects(tt)) {
                hit = 1;
            }
            if (mhbf.intersects(cc)) {
                gathered = true;
            } else {
                gathered = false;
            }
            if (bw > 0) {
                if (mhbf.intersects(bbf)) {
                    hit = 1;
                }
            } else if (bw < 0) {
                if (mhbf.intersects(bb)) {
                    hit = 1;
                }
            }
        }
        if (mw < 0) {
            if (mhbb.intersects(tt)) {
                hit = 1;
            }
            if (mhbb.intersects(cc)) {
                gathered = true;
            } else {
                gathered = false;
            }
            if (bw > 0) {
                if (mhbb.intersects(bbf)) {
                    hit = 1;
                }
            } else if (bw < 0) {
                if (mhbb.intersects(bb)) {
                    hit = 1;
                }
            }
        }
        if (hit == 1) {
            gameover = true;
        }
    }

    public void resetgame () {
        if (rpress && newn == 125) {
            mw = 40;
            mh = 80;
            jumpcount = 1;
            jumping = false;
            jumpphase = 16;
            mx = 100;
            floor = 680;
            my = floor - mh;
            charphase = 1;
            bx = 1600;
            by = floor - 100;
            bw = -90;
            bh = 80;
            tx = 1600;
            ty = floor - 650;
            tw = 120;
            th = 160;
            speed = 10;
            hit = 0;
            newn = 0;
            cx = 1200;
            score = 0;
            gathered = false;
            gameover = false;
            gamestart = false;
            playing = false;
            continueanim = false;
            thwompready = true;
            thwompfall = false;
        }
    }

    public void updates () {
        Thread thread = new Thread() {
            public void run() {
                int n;
                for (n = 1; n < 10; n++) {
                    try {
                        Scoring();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (!gameover && gamestart) {
                        movements();
                        Colliding();
                        Thwomping();
                        newCoins();
                    }
                    gamestarter();
                    if (gameover && newn < 125) {
                        continueanim = true;
                    } else if (newn > 125) {
                        continueanim = false;
                        newn = 125;
                    }
                    if (gameover && newn == 125) {
                        resetgame();
                    }
                    n = 1;
                    repaint();
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException ex) {
                    }
                    if (continueanim) {
                        newn = newn + 1;
                    }
                }
            }
        }; thread.start();
    }


    public void keyTyped (KeyEvent e) {}

    public void keyPressed (KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_LEFT:
                leftpressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightpressed = true;
                break;
            case KeyEvent.VK_UP:
                uppressed = true;
                break;
            case KeyEvent.VK_ENTER:
                enterpress = true;
                break;
            case KeyEvent.VK_R:
                rpress = true;
                break;
        }
    }

    public void keyReleased (KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_LEFT:
                leftpressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightpressed = false;
                break;
            case KeyEvent.VK_UP:
                uppressed = false;
                break;
            case KeyEvent.VK_ENTER:
                enterpress = false;
                break;
            case KeyEvent.VK_R:
                rpress = false;
                break;
        }
    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Monospaced", Font.BOLD, 100));
        if (!gamestart && !gameover) {
            g.setColor(Color.BLACK);
            g.fillRect(0,0, width, height);
            g.setColor(Color.WHITE);
            g.drawString("Mario Mania", width/2 - width/4, height/2 - height/8);
            g.setFont(new Font("Serif", Font.ITALIC, 50));
            g.drawString("Press Enter to Start", width/3, height/2 + height/6);
        }
        if (gamestart) {
            g.clearRect(0,0,width,height);
            g.drawImage(backimage, 0, 0, width, height, null);
            g.drawImage(coinimage, cx, cy, cr, cr, null);
            if (!jumping) {
                if (charphase == 1 || charphase == 3) {
                    g.drawImage(charimage, mx, my, mw, mh, null);
                } else if (charphase == 2) {
                    g.drawImage(charimage2, mx, my, mw, mh, null);
                } else if (charphase == 4) {
                    g.drawImage(charimage3, mx, my, mw, mh, null);
                }
            } else if (jumping) {
                g.drawImage(jumpimage, mx, my, mw, mh, null);
            }
            g.drawImage(bullb, bx, by, bw, bh,null);
            g.drawImage(thwompimage, tx, ty, tw, th,null);
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            g.drawString("Score: " + score, width - 220, 30);
            g.drawString("Highscore: " + highscore, width - 220, 60);
            paintHitBox(g);

        }

        if (gameover) {
            g.setColor(Color.BLACK);
            g.fillRect(0, -2 * height + newn * height/100, width, 2 * height);
            g.setFont(new Font("Monospaced", Font.BOLD, 100));
            g.setColor(Color.WHITE);
            g.drawString("Game Over", width / 2 - width / 5, height / 2 - 30);
            //g.drawString(newn + ", " + (-height + newn  * height/100),  width/2, height/2 + 250);
        }

        if (newn >= 100) {
            g.setColor(Color.CYAN);
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            g.drawString("Your Final Score: " + score, width/2 - width/9, height/2 + 20);
            g.drawString("All-Time High Score: " + highscore, width/2 - width/9, height/2 + 50);
        }

        if (newn == 125) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Serif", Font.ITALIC, 50));
            g.drawString("Press R to Restart", width/2 - width/7, height/2 + 110);
        }
    }

    public void paintHitBox (Graphics g) {
        g.setColor(Color.GREEN);
        if (mw < 0) {
            g.drawRect(mx + mw, my, Math.abs(mw), mh);
        } else if (mw > 0) {
            g.drawRect(mx, my, mw, mh);
        }
        if (bw < 0) {
            g.drawRect(bx + bw, by, Math.abs(bw), bh);
        } else if (bw > 0) {
            g.drawRect(bx, by, bw, bh);
        }
        g.drawRect(tx, ty, tw, th);
        g.drawRect(cx, cy, cr, cr);
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("MarioRun");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        Main Mainstuff = new Main();
        frame.add(Mainstuff);
        frame.setVisible(true);
    }
}
