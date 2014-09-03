import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/*
 * Created on 2006/02/24
 */

public class MainPanel extends JPanel implements Runnable {
    // パネルサイズ
    public static final int WIDTH = 240;
    public static final int HEIGHT = 240;
    // ボールの大きさ
    private static final int SIZE = 10;
    // ボールの位置 (x, y)、円の左上の座標
    private int x;
    private int y;
    // ボールの速度 (vx, vy)
    private int vx;
    private int vy;
    // アニメーション用スレッド
    private Thread thread;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // ボールの位置を初期化
        x = 0;
        y = 0;

        // ボールの速度を初期化
        vx = 2;
        vy = 1;

        // スレッドを起動
        thread = new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 赤いボールを描く
        g.setColor(Color.RED);
        g.fillOval(x, y, SIZE, SIZE);
    }

    // メインループ
    public void run() {
        // プログラムが終了するまで繰り返す
        while (true) {
            // ボールを速度分だけ移動させる
            x += vx;
            y += vy;

            // 左または右に当たったらx方向速度の符号を反転させる
            if (x < 0 || x > WIDTH - SIZE) {
                vx = -vx;
            }

            // 上または下に当たったらy方向速度の符号を反転させる
            if (y < 0 || y > HEIGHT - SIZE) {
                vy = -vy;
            }

            // ボールを再描画
            repaint();

            // 20ミリ秒だけ休止
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

