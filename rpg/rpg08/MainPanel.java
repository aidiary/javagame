import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

/*
 * Created on 2005/10/09
 *
 */

/**
 * @author mori
 *  
 */
class MainPanel extends JPanel implements KeyListener, Common {
    // パネルサイズ
    public static final int WIDTH = 480;
    public static final int HEIGHT = 480;

    // マップ
    private Map map;
    // 勇者
    private Chara hero;

    public MainPanel() {
        // パネルの推奨サイズを設定
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // パネルがキー操作を受け付けるように登録する
        setFocusable(true);
        addKeyListener(this);
        
        // マップを作成
        map = new Map(this);
        // 勇者を作成
        hero = new Chara(1, 1, "image/hero.gif", map, this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // X方向のオフセットを計算
        int offsetX = MainPanel.WIDTH / 2 - hero.getX() * CS;
        // マップの端ではスクロールしないようにする
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, MainPanel.WIDTH - Map.WIDTH);
        
        // Y方向のオフセットを計算
        int offsetY = MainPanel.HEIGHT / 2 - hero.getY() * CS;
        // マップの端ではスクロールしないようにする
        offsetY = Math.min(offsetY, 0);
        offsetY = Math.max(offsetY, MainPanel.HEIGHT - Map.HEIGHT);

        // マップを描く
        map.draw(g, offsetX, offsetY);

        // 勇者を描く
        hero.draw(g, offsetX, offsetY);
    }

    public void keyPressed(KeyEvent e) {
        // 押されたキーを調べる
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_LEFT :
                // 左キーだったら勇者を1歩左へ
                hero.move(LEFT);
                break;
            case KeyEvent.VK_RIGHT :
                // 右キーだったら勇者を1歩右へ
                hero.move(RIGHT);
                break;
            case KeyEvent.VK_UP :
                // 上キーだったら勇者を1歩上へ
                hero.move(UP);
                break;
            case KeyEvent.VK_DOWN :
                // 下キーだったら勇者を1歩下へ
                hero.move(DOWN);
                break;
        }

        // 勇者の位置を動かしたので再描画
        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
