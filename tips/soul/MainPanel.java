import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/*
 * Created on 2007/02/04
 */

public class MainPanel extends JPanel implements Runnable, MouseMotionListener {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    // ソウル
    private Soul soul;

    // 時間
    private double t = 0.0;

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        soul = new Soul(WIDTH / 2, HEIGHT / 2, 100, 180, Color.CYAN);

        addMouseMotionListener(this);

        // ゲームループ開始
        Thread gameLoop = new Thread(this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // ソウルを描画
        soul.draw(g);
    }

    public void run() {
        while (true) {
            // 時間をすすめる
            // ゲームループは1秒間に20回呼ばれるので1回で0.05秒だけ進める
            t += 0.05;

            soul.update(t);

            repaint();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        soul.setCenter(e.getX(), e.getY());
        repaint();
    }
}
