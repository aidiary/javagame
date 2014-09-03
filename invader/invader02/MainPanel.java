import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

/*
 * Created on 2005/02/09
 *
 */

/**
 * メインパネル
 * 
 * @author mori
 *  
 */
public class MainPanel extends JPanel implements Runnable, KeyListener {
    // パネルサイズ
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    // 方向定数
    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    // プレイヤー
    private Player player;
    // 弾
    private Shot shot;

    // キーの状態（このキー状態を使ってプレイヤーを移動する）
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean firePressed = false;

    // ゲームループ用スレッド
    private Thread gameLoop;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // パネルがキー入力を受け付けるようにする
        setFocusable(true);

        // プレイヤーを作成
        player = new Player(0, HEIGHT - 20, this);

        // 弾を作成
        shot = new Shot(this);

        // キーイベントリスナーを登録
        addKeyListener(this);

        // ゲームループ開始
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    /**
     * ゲームループ
     */
    public void run() {

        while (true) {
            // プレイヤーを移動する
            // 何も押されていないときは移動しない
            if (leftPressed) {
                player.move(LEFT);
            } else if (rightPressed) {
                player.move(RIGHT);
            }

            // 発射ボタンが押されたら弾を発射
            if (firePressed) {
                if (shot.isInStorage()) {
                    // 弾が保管庫にあれば発射できる
                    // 弾の座標をプレイヤーの座標にすれば発射される
                    Point pos = player.getPos();
                    shot.setPos(pos.x + player.getWidth() / 2, pos.y);
                }
            }

            // 弾を移動する
            shot.move();

            // 再描画
            repaint();

            // 休止
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 描画処理
     * 
     * @param 描画オブジェクト
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 背景を黒で塗りつぶす
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // プレイヤーを描画
        player.draw(g);

        // 弾を描画
        shot.draw(g);
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
     * キーが押されたらキーの状態を「押された」に変える
     * 
     * @param e キーイベント
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            firePressed = true;
        }
    }

    /**
     * キーが離されたらキーの状態を「離された」に変える
     * 
     * @param e キーイベント
     */
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if (key == KeyEvent.VK_SPACE) {
            firePressed = false;
        }
    }
}