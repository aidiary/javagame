import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

/*
 * Created on 2005/04/10
 *
 */

/**
 * @author mori
 *
 */
public class MainPanel extends JPanel implements Runnable {
    // パネルサイズ
    private static final int WIDTH = 640;
    private static final int HEIGHT = 640;
    // グリッドサイズ
    private static final int GS = 8;
    // 行数、列数
    public static final int ROW = HEIGHT / GS;
    public static final int COL = WIDTH / GS;
    // 方向定数
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;

    // 追跡者
    private Predator predator;

    // スレッド
    private Thread thread;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);

        // 追跡者
        predator = new Predator(0, 0);

        // 星型経路を作成
        // 閉ループを作っている
        predator.buildPathSegment(new Point(40, 5), new Point(15, 70));
        predator.buildPathSegment(new Point(15, 70), new Point(75, 30));
        predator.buildPathSegment(new Point(75, 30), new Point(5, 30));
        predator.buildPathSegment(new Point(5, 30), new Point(65, 70));
        predator.buildPathSegment(new Point(65, 70), new Point(40, 5));

        // 作成した経路を表示
        // predator.showPath();

        thread = new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 追跡者を描画
        predator.draw(g);
    }

    public void run() {
        while (true) {
            // 追跡者を移動
            predator.move();

            repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
