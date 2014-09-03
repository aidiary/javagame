import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JPanel;

/*
 * Created on 2006/04/30
 */

public class MainPanel extends JPanel implements Runnable {
    // パネルサイズ
    public static final int WIDTH = 480;
    public static final int HEIGHT = 480;

    private static final int NUM_BALL = 3;

    // パネルに描画するイメージ
    // BufferedImageはピクセル操作が可能
    private BufferedImage bufImage;
    private Graphics bufG;

    // ダブルバッファリング用
    private Image dbImage = null;
    private Graphics dbg;

    // 画像のピクセルへのアクセス、変更ができるWritableRaster
    private WritableRaster raster;

    // メタボール
    private Metaball[] metaball;

    public MainPanel() {
        // パネルの推奨サイズを設定
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // メタボール
        metaball = new Metaball[NUM_BALL];
        metaball[0] = new Metaball(WIDTH / 2, HEIGHT / 2, 5, 4);
        metaball[1] = new Metaball(WIDTH / 2, HEIGHT / 2, -3, -6);
        metaball[2] = new Metaball(WIDTH / 2, HEIGHT / 2, -4, 7);

        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while (true) {
            gameUpdate(); // ゲーム状態更新
            gameRender(); // レンダリング
            paintScreen(); // 画面描画

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ゲーム状態を更新する
     */
    private void gameUpdate() {
        // メタボールの移動
        for (int i = 0; i < NUM_BALL; i++) {
            metaball[i].move();
        }
    }

    /**
     * レンダリング
     */
    private void gameRender() {
        // ダブルバッファリング用オブジェクトの生成
        if (dbImage == null) {
            dbImage = createImage(WIDTH, HEIGHT);
            if (dbImage == null) {
                System.out.println("dbImage is null");
                return;
            } else {
                dbg = dbImage.getGraphics();
            }
        }

        // ピクセル操作を行うBufferedImageの生成
        if (bufImage == null) {
            bufImage = new BufferedImage(WIDTH, HEIGHT,
                    BufferedImage.TYPE_INT_RGB);
            bufG = bufImage.getGraphics();
            raster = bufImage.getRaster(); // ピクセル操作に使う
        }

        // BufferedImageを黒く塗りつぶす
        bufG.setColor(Color.BLACK);
        bufG.fillRect(0, 0, WIDTH, HEIGHT);

        // メタボールを重ね合わせる
        for (int i = 0; i < NUM_BALL; i++) {
            metaball[i].draw(raster); // rasterを渡してdraw()内でピクセル操作を行う
        }

        dbg.drawImage(bufImage, 0, 0, this);
    }

    /**
     * バッファの内容を画面に描画
     */
    private void paintScreen() {
        Graphics g;
        try {
            g = getGraphics();
            if ((g != null) & (dbImage != null)) {
                g.drawImage(dbImage, 0, 0, null);
            }
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
