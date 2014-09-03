import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

/*
 * Created on 2005/03/09
 *
 */

/**
 * エイリアンクラス
 * 
 * @author mori
 *  
 */
public class Alien {
    // エイリアンの移動範囲
    private static final int MOVE_WIDTH = 210;

    // 移動スピード
    private int speed;

    // エイリアンの位置（x座標）
    private int x;
    // エイリアンの位置（y座標）
    private int y;
    // エイリアンの幅
    private int width;
    // エイリアンの高さ
    private int height;
    // エイリアンの画像
    private Image image;

    // エイリアンの移動範囲
    private int left;
    private int right;

    // メインパネルへの参照
    private MainPanel panel;

    public Alien(int x, int y, int speed, MainPanel panel) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.panel = panel;

        // エイリアンの初期位置から移動範囲を求める
        left = x;
        right = x + MOVE_WIDTH;

        // イメージをロード
        loadImage();
    }

    /**
     * エイリアンを移動する
     * 
     */
    public void move() {
        x += speed;

        // 移動範囲を超えていたら反転移動
        if (x < left) {
            speed = -speed;
        }
        if (x > right) {
            speed = -speed;
        }
    }

    /**
     * エイリアンの幅を返す。
     * 
     * @param width エイリアンの幅。
     */
    public int getWidth() {
        return width;
    }

    /**
     * エイリアンの高さを返す。
     * 
     * @return height エイリアンの高さ。
     */
    public int getHeight() {
        return height;
    }

    /**
     * エイリアンの位置を返す。
     * 
     * @return エイリアンの位置座標。
     */
    public Point getPos() {
        return new Point(x, y);
    }

    /**
     * エイリアンの位置を(x,y)にセットする。
     * 
     * @param x 移動先のx座標。
     * @param y 移動先のy座標。
     */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * エイリアンを描画する。
     * 
     * @param g 描画オブジェクト。
     */
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    /**
     * イメージをロードする
     *  
     */
    private void loadImage() {
        // エイリアンのイメージを読み込む
        // ImageIconを使うとMediaTrackerを使わなくてすむ
        ImageIcon icon = new ImageIcon(getClass()
                .getResource("image/alien.gif"));
        image = icon.getImage();

        // 幅と高さをセット
        width = image.getWidth(panel);
        height = image.getHeight(panel);
    }
}