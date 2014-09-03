import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

/*
 * Created on 2005/02/17
 *
 */

/**
 * 弾クラス
 * 
 * @author mori
 *  
 */
public class Shot {
    // 弾のスピード
    private static final int SPEED = 10;
    // 弾の保管座標（画面に表示されない場所）
    private static final Point STORAGE = new Point(-20, -20);

    // 弾の位置（x座標）
    private int x;
    // 弾の位置（y座標）
    private int y;
    // 弾の幅
    private int width;
    // 弾の高さ
    private int height;
    // 弾の画像
    private Image image;

    // メインパネルへの参照
    private MainPanel panel;

    public Shot(MainPanel panel) {
        x = STORAGE.x;
        y = STORAGE.y;
        this.panel = panel;

        // イメージをロード
        loadImage();
    }

    /**
     * 弾を移動する
     *  
     */
    public void move() {
        // 保管庫に入っているなら何もしない
        if (isInStorage())
            return;

        // 弾はy方向にしか移動しない
        y -= SPEED;
        // 画面外の弾は保管庫行き
        if (y < 0) {
            store();
        }
    }

    /**
     * 弾の位置を返す
     * 
     * @return 弾の位置座標
     */
    public Point getPos() {
        return new Point(x, y);
    }

    /**
     * 弾の位置をセットする
     * 
     * @param x 弾のx座標
     * @param y 弾のy座標
     *  
     */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 弾を保管庫に入れる
     *  
     */
    public void store() {
        x = STORAGE.x;
        y = STORAGE.y;
    }

    /**
     * 弾が保管庫に入っているか
     * 
     * @return 入っているならtrueを返す
     */
    public boolean isInStorage() {
        if (x == STORAGE.x && y == STORAGE.x)
            return true;
        return false;
    }

    /**
     * 弾を描画する
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g) {
        // 弾を描画する
        g.drawImage(image, x, y, null);
    }

    /**
     * イメージをロードする
     *  
     */
    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("image/shot.gif"));
        image = icon.getImage();

        // 幅と高さをセット
        width = image.getWidth(panel);
        height = image.getHeight(panel);
    }

}