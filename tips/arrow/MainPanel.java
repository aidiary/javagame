import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

/*
 * Created on 2006/06/10
 */

public class MainPanel extends JPanel
        implements
            Runnable,
            MouseListener,
            MouseMotionListener {
    // パネルサイズ
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    // 矢の数
    public static final int NUM_ARROW = 10;

    private double direction; // 弓の向き
    private Point cannonPos = new Point(0, 480); // 弓の位置
    private Point mousePos = new Point(); // マウスの位置

    private Arrow[] arrows = new Arrow[NUM_ARROW];

    // ダブルバッファリング用
    private Graphics dbg;
    private Image dbImage = null;

    private long beforeTime;
    private Thread gameLoop; // ゲームループ

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // 弓を作成
        for (int i = 0; i < NUM_ARROW; i++) {
            arrows[i] = new Arrow();
        }

        // 発射音をロード
        try {
            WaveEngine.load("arrow01.wav");
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        addMouseListener(this);
        addMouseMotionListener(this);

        gameLoop = new Thread(this);
        gameLoop.start();
    }

    public void run() {
        beforeTime = System.currentTimeMillis();

        while (true) {
            gameUpdate(); // ゲーム状態を更新
            gameRender(); // バッファにレンダリング
            paintScreen(); // バッファの内容を画面に描画

            WaveEngine.render();

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
        double timeDiff;

        // 前回からの時間差を求める
        timeDiff = (System.currentTimeMillis() - beforeTime) / 1000.0; // 秒単位
        // System.out.println(timeDiff);

        // 矢をを時間分だけ移動
        if (arrows != null) {
            for (int i = 0; i < NUM_ARROW; i++) {
                arrows[i].move(timeDiff);
            }
        }

        beforeTime = System.currentTimeMillis();
    }

    /**
     * バッファにレンダリング
     */
    private void gameRender() {
        // バッファの作成
        if (dbImage == null) {
            dbImage = createImage(WIDTH, HEIGHT);
            if (dbImage == null) {
                System.out.println("dbImage is null");
                return;
            } else {
                dbg = dbImage.getGraphics();
            }
        }

        // 背景を塗りつぶす
        dbg.setColor(Color.WHITE);
        dbg.fillRect(0, 0, WIDTH, HEIGHT);

        // 矢を描画
        if (arrows != null) {
            for (int i = 0; i < NUM_ARROW; i++) {
                arrows[i].draw(dbg);
            }
        }
    }

    /**
     * バッファの内容を画面に描画
     */
    private void paintScreen() {
        Graphics g = getGraphics();
        if ((g != null) && (dbImage != null)) {
            g.drawImage(dbImage, 0, 0, null);
        }
        Toolkit.getDefaultToolkit().sync();
        if (g != null) {
            g.dispose();
        }
    }

    public void mouseClicked(MouseEvent e) {
        // 発射音
        WaveEngine.play(0);

        if (e.isControlDown()) {  // CTRLを押すとマウス対象に指定時間後に命中
            for (int i = 0; i < NUM_ARROW; i++) {
                if (!arrows[i].isUsed()) { // 使っていない矢を探して発射！！！
                    // マウス座標に3.0秒後に命中
                    arrows[i].fire(cannonPos.x, cannonPos.y, mousePos.x,
                            mousePos.y, 3.0);
                    break;
                }
            }
        } else {  // 速さと角度を指定
            // 発射角
            direction = Math.atan2(mousePos.y - cannonPos.y, mousePos.x
                    - cannonPos.x);
            for (int i = 0; i < NUM_ARROW; i++) {
                if (!arrows[i].isUsed()) { // 使っていない矢を探して発射！！！
                    arrows[i].fire(cannonPos.x, cannonPos.y, 400, direction);
                    break; // 1クリックで1発だけ発射したいので抜ける
                }
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        mousePos = new Point(e.getX(), e.getY());
        // System.out.println(mousePos.x + " " + mousePos.y);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }
}
