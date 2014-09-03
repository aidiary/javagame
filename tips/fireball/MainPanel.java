import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/*
 * Created on 2006/01/29
 */

/**
 * @author mori
 */
public class MainPanel extends JPanel
        implements
            Runnable,
            MouseListener,
            MouseMotionListener {
    // パネルサイズ
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    // 最大ファイアボール数
    private static final int MAX_FIRES = 128;

    private FireBall[] fireball;
    private Thread thread;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        addMouseListener(this);
        addMouseMotionListener(this);

        // オブジェクトを「あらかじめ」用意
        // クリックしたときに作ると遅いかも
        fireball = new FireBall[MAX_FIRES];
        for (int i = 0; i < MAX_FIRES; i++) {
            fireball[i] = new FireBall();
        }

        thread = new Thread(this);
        thread.start();
    }

    /**
     * ゲームループ
     */
    public void run() {
        while (true) {
            // ファイアボールの移動
            for (int i=0; i<MAX_FIRES; i++) {
                if (fireball[i].isUsed()) {
                    fireball[i].move();
                }
            }

            repaint();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 背景を黒で塗りつぶす
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 中心を描画
        g.setColor(Color.YELLOW);
        g.fillOval(320, 240, 2, 2);
        
        // ファイアボールを描画
        for (int i = 0; i < MAX_FIRES; i++) {
            if (fireball[i].isUsed()) {
                fireball[i].draw(g);
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        Point start = new Point(320, 240);
        Point target = new Point(e.getX(), e.getY());
        for (int i = 0; i < MAX_FIRES; i++) {
            if (!fireball[i].isUsed()) { // 使われていないオブジェクトを検索
                fireball[i].shot(start, target); // ファイア！
                break; // 1つ見つけたらOKなのでループ抜ける
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        Point start = new Point(320, 240);
        Point target = new Point(e.getX(), e.getY());
        for (int i = 0; i < MAX_FIRES; i++) {
            if (!fireball[i].isUsed()) { // 使われていないオブジェクトを検索
                fireball[i].shot(start, target); // ファイア！
                break; // 1つ見つけたらOKなのでループ抜ける
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}