import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

/*
 * Created on 2006/05/07
 */

public class MainPanel extends JPanel implements Runnable {
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private Ball ball; // ボールオブジェト

    // ダブルバッファリング（db）用
    private Graphics dbg;
    private Image dbImage = null;

    private Thread gameLoop; // ゲームループ

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        ball = new Ball(320, 240, 9, 7);

        // ゲームループ開始
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    /**
     * ゲームループ
     */
    public void run() {
        while (true) {
            gameUpdate(); // ゲーム状態を更新（ex: ボールの移動）
            gameRender(); // バッファにレンダリング（ダブルバッファリング）
            paintScreen(); // バッファを画面に描画（repaint()を自分でする！）

            // Active Renderingではrepaint()を使わない！

            // 休止
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ゲーム状態を更新
     */
    private void gameUpdate() {
        // ボールの移動
        ball.move();
    }

    /**
     * バッファにレンダリング（ダブルバッファリング）
     */
    private void gameRender() {
        // 初回の呼び出し時にダブルバッファリング用オブジェクトを作成
        if (dbImage == null) {
            // バッファイメージ
            dbImage = createImage(WIDTH, HEIGHT);
            if (dbImage == null) {
                System.out.println("dbImage is null");
                return;
            } else {
                // バッファイメージの描画オブジェクト
                dbg = dbImage.getGraphics();
            }
        }

        // バッファをクリアする
        dbg.setColor(Color.WHITE);
        dbg.fillRect(0, 0, WIDTH, HEIGHT);

        // ボールをバッファへ描画する
        ball.draw(dbg);
    }

    /**
     * バッファを画面に描画
     */
    private void paintScreen() {
        try {
            Graphics g = getGraphics(); // グラフィックオブジェクトを取得
            if ((g != null) && (dbImage != null)) {
                g.drawImage(dbImage, 0, 0, null); // バッファイメージを画面に描画
            }
            Toolkit.getDefaultToolkit().sync();
            if (g != null) {
                g.dispose(); // グラフィックオブジェクトを破棄
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
