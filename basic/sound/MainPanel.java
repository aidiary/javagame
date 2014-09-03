import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/*
 * Created on 2006/02/25
 */

public class MainPanel extends JPanel implements Runnable {
    // パネルサイズ
    public static final int WIDTH = 240;
    public static final int HEIGHT = 240;

    // 音が鳴るボール
    private SoundBall ball;
    // アニメーション用スレッド
    private Thread thread;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setSize(WIDTH, HEIGHT);

        // ボールを作成
        ball = new SoundBall(0, 0, 5, 4);

        // スレッドを起動
        thread = new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ボールを描画
        ball.draw(g);
    }

    public void run() {
        // プログラムが終了するまでフレーム処理を繰り返す
        while (true) {
            // 各ボールを速度分だけ移動させる
            ball.move();

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

