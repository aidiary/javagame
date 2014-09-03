import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/*
 * Created on 2005/04/16
 *
 */

/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel implements Runnable {
    // パネルサイズ
    private static final int WIDTH = 480;
    private static final int HEIGHT = 320;

    // 追跡者
    private Predator predator;
    // マップ
    private Map map;

    // スレッド
    private Thread thread;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);

        // マップ
        map = new Map("map.dat");

        // 追跡者
        predator = new Predator(1, 1, map);

        thread = new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // マップを描画
        map.draw(g);

        // 追跡者を描画
        predator.draw(g);
    }

    public void run() {
        while (true) {
            // マップを巡回する
            predator.patrol();

            repaint();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}