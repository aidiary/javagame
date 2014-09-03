import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/*
 * Created on 2007/05/05
 * 
 * ブロック崩しのゲーム画面用パネル
 */

public class MainPanel extends JPanel implements Runnable, MouseMotionListener {
    public static final int WIDTH = 360;
    public static final int HEIGHT = 480;

    private Racket racket; // ラケット
    private Ball ball; // ボール

    private Thread gameLoop; // ゲームループ

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addMouseMotionListener(this);

        racket = new Racket();
        ball = new Ball();

        gameLoop = new Thread(this);
        gameLoop.start();
    }

    /**
     * ゲームループ
     * 
     */
    public void run() {
        while (true) {
            ball.move(); // ボールの移動
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

        // 背景
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        racket.draw(g); // ラケット
        ball.draw(g); // ボール
    }

    public void mouseMoved(MouseEvent e) {
        int x = e.getX(); // マウスのX座標
        racket.move(x); // ラケットを移動
        repaint();
    }

    public void mouseDragged(MouseEvent e) {
    }
}
