import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    // 最大爆発数
    private static final int MAX_EXPLOSION = 128;

    private Explosion[] explosion;
    private Thread thread;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        addMouseListener(this);
        addMouseMotionListener(this);

        // 爆発オブジェクトを「あらかじめ」用意
        // クリックしたときに作ると遅いかも
        explosion = new Explosion[MAX_EXPLOSION];
        for (int i = 0; i < MAX_EXPLOSION; i++) {
            explosion[i] = new Explosion();
        }

        thread = new Thread(this);
        thread.start();
    }

    /**
     * ゲームループ
     */
    public void run() {
        while (true) {
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

        // 爆発を描画
        for (int i = 0; i < MAX_EXPLOSION; i++) {
            explosion[i].draw(g);
        }
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        for (int i = 0; i < MAX_EXPLOSION; i++) {
            if (!explosion[i].isUsed()) { // 使われていないExplosionオブジェクトを検索
                explosion[i].play(x, y); // 爆発！
                break; // 1つ見つけたらOKなのでループ抜ける
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        for (int i = 0; i < MAX_EXPLOSION; i++) {
            if (!explosion[i].isUsed()) { // 使われていないExplosionオブジェクトを検索
                explosion[i].play(x, y); // 爆発！
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