import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;

/*
 * Created on 2005/06/06
 *
 */

/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel implements Runnable, KeyListener {
    // パネルサイズ
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    // マップ
    private Map map;

    // プレイヤー
    private Player player;

    // キーの状態（押されているか、押されてないか）
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;

    // ゲームループ用スレッド
    private Thread gameLoop;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // パネルがキー入力を受け付けるようにする
        setFocusable(true);

        // マップを作成
        map = new Map("map01.dat");

        // プレイヤーを作成
        player = new Player(192, 32, "player.gif", map);

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
            if (leftPressed) {
                // 左キーが押されていれば左向きに加速
                player.accelerateLeft();
            } else if (rightPressed) {
                // 右キーが押されていれば右向きに加速
                player.accelerateRight();
            } else {
                // 何も押されてないときは停止
                player.stop();
            }

            if (upPressed) {
                // ジャンプする
                player.jump();
            }

            // プレイヤーの状態を更新
            player.update();

            // マップにいるスプライトを取得
            LinkedList sprites = map.getSprites();            
            Iterator iterator = sprites.iterator();
            while (iterator.hasNext()) {
                Sprite sprite = (Sprite)iterator.next();
                
                // スプライトの状態を更新する
                sprite.update();

                // プレイヤーと接触してたら
                if (player.isCollision(sprite)) {
                    // それがコインだったら
                    if (sprite instanceof Coin) {
                        Coin coin = (Coin)sprite;
                        // コインは消える
                        sprites.remove(coin);
                        // ちゃり～ん
                        coin.play();
                        // spritesから削除したので
                        // breakしないとiteratorがおかしくなる
                        break;
                    }
                }
            }
            
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

        // X方向のオフセットを計算
        int offsetX = MainPanel.WIDTH / 2 - (int)player.getX();
        // マップの端ではスクロールしないようにする
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, MainPanel.WIDTH - map.getWidth());

        // Y方向のオフセットを計算
        int offsetY = MainPanel.HEIGHT / 2 - (int)player.getY();
        // マップの端ではスクロールしないようにする
        offsetY = Math.min(offsetY, 0);
        offsetY = Math.max(offsetY, MainPanel.HEIGHT - map.getHeight());

        // マップを描画
        map.draw(g, offsetX, offsetY);

        // プレイヤーを描画
        player.draw(g, offsetX, offsetY);
        
        // スプライトを描画
        LinkedList sprites = map.getSprites();            
        Iterator iterator = sprites.iterator();
        while (iterator.hasNext()) {
            Sprite sprite = (Sprite)iterator.next();
            sprite.draw(g, offsetX, offsetY);
        }
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
        if (key == KeyEvent.VK_UP) {
            upPressed = true;
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
        if (key == KeyEvent.VK_UP) {
            upPressed = false;
        }
    }

    public void keyTyped(KeyEvent e) {
    }
}
