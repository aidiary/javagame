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
    // ボールの数
    private static final int NUM_BALL = 4;
    // ボールを格納する配列
    private Ball[] ball;
    // アニメーション用スレッド
    private Thread thread;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setSize(WIDTH, HEIGHT);

        // ボールを格納する配列を作成
        ball = new Ball[NUM_BALL];
        // ボールを作成
        ball[0] = new Ball(0, 0, 1, 2);
        ball[1] = new Ball(10, 10, 3, -2);
        ball[2] = new Ball(50, 0, -2, 3);
        ball[3] = new Ball(0, 0, 12, 8);

        // スレッドを起動
        thread = new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 各ボールを描画
        for (int i = 0; i < NUM_BALL; i++) {
            ball[i].draw(g);
        }
    }

    // メインループ
    public void run() {
        // プログラムが終了するまでフレーム処理を繰り返す
        while (true) {
            // 各ボールを速度分だけ移動させる
            for (int i = 0; i < NUM_BALL; i++) {
                ball[i].move();
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
