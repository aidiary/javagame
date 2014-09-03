import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

/*
 * Created on 2005/10/10
 *
 */

/**
 * @author mori
 *
 */
public class Chara implements Common { 
    // キャラクターのイメージ
    private Image image;

    // キャラクターの座標（単位：マス）
    private int x, y;
    // キャラクターの向いている方向（LEFT,RIGHT,UP,DOWNのどれか）
    private int direction;
    // キャラクターのアニメーションカウンタ
    private int count;
    
    // キャラクターアニメーション用スレッド
    private Thread threadAnime;
    
    // マップへの参照
    private Map map;

    // パネルへの参照
    private MainPanel panel;

    public Chara(int x, int y, String filename, Map map, MainPanel panel) {
        this.x = x;
        this.y = y;

        direction = DOWN;
        count = 0;
        
        this.map = map;
        this.panel = panel;

        // イメージをロード
        loadImage(filename);

        // キャラクターアニメーション用スレッド開始
        threadAnime = new Thread(new AnimationThread());
        threadAnime.start();
    }
    
    public void draw(Graphics g, int offsetX, int offsetY) {
        // countとdirectionの値に応じて表示する画像を切り替える
        g.drawImage(image, x * CS + offsetX, y * CS + offsetY, x * CS + offsetX + CS, y * CS + offsetY + CS,
            count * CS, direction * CS, CS + count * CS, direction * CS + CS, panel);
    }

    public void move(int dir) {
        // dirの方向でぶつからなければ移動する
        switch (dir) {
            case LEFT:
                if (!map.isHit(x-1, y)) x--;
                direction = LEFT;
                break;
            case RIGHT:
                if (!map.isHit(x+1, y)) x++;
                direction = RIGHT;
                break;
            case UP:
                if (!map.isHit(x, y-1)) y--;
                direction = UP;
                break;
            case DOWN:
                if (!map.isHit(x, y+1)) y++;
                direction = DOWN;
                break;
        }
    }

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }

    private void loadImage(String filename) {
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        image = icon.getImage();
    }
    
    // アニメーションクラス
    private class AnimationThread extends Thread {
        public void run() {
            while (true) {
                // countを切り替える
                if (count == 0) {
                    count = 1;
                } else if (count == 1) {
                    count = 0;
                }
                
                panel.repaint();

                // 300ミリ秒休止＝300ミリ秒おきにキャラクターの絵を切り替える
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } 
            }
        }
    }
}
