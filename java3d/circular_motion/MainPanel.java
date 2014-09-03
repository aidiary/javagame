import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/*
 * Created on 2007/02/04
 */

public class MainPanel extends JPanel implements Runnable {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    // ボール数
    private static final int NUM_BALL = 3;

    // ボール
    private Ball[] ball;
    // 時間
    private double t = 0.0;

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        ball = new Ball[NUM_BALL];
        ball[0] = new Ball(WIDTH / 2, HEIGHT / 2, 200, 90, Color.CYAN);
        ball[1] = new Ball(WIDTH / 2, HEIGHT / 2, 100, 120, Color.RED);
        ball[2] = new Ball(WIDTH / 4, HEIGHT / 4, 100, 150, Color.ORANGE);

        // ゲームループ開始
        Thread gameLoop = new Thread(this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 座標軸を描画
        g.setColor(Color.YELLOW);
        g.drawLine(0, HEIGHT / 2, WIDTH, HEIGHT / 2);
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);

        // ボールを描画
        for (int i = 0; i < NUM_BALL; i++) {
            ball[i].draw(g);
        }
    }

    public void run() {
        while (true) {
            // 時間をすすめる
            // ゲームループは1秒間に20回呼ばれるので1回で0.05秒だけ進める
            t += 0.05;

            for (int i = 0; i < NUM_BALL; i++) {
                ball[i].update(t);
            }

            repaint();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
