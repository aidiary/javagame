import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

/*
 * Created on 2005/03/19
 *
 */

/**
 * ビームクラス
 * 
 * @author mori
 *  
 */
public class Beam {
    // ビームのスピード
    private static final int SPEED = 5;
    // ビームの保管座標（画面に表示されない場所）
    private static final Point STORAGE = new Point(-20, -20);

    // ビームの位置（x座標）
    private int x;
    // ビームの位置（y座標）
    private int y;
    // ビームの幅
    private int width;
    // ビームの高さ
    private int height;
    // ビームの画像
    private Image image;

    // メインパネルへの参照
    private MainPanel panel;

    public Beam(MainPanel panel) {
        x = STORAGE.x;
        y = STORAGE.y;
        this.panel = panel;

        // イメージをロード
        loadImage();
    }

    /**
     * ビームを移動する
     */
    public void move() {
        // 保管庫に入っているなら何もしない
        if (isInStorage())
            return;

        // ビームはy方向にしか移動しない
        y += SPEED;
        // 画面外のビームは保管庫行き
        if (y > MainPanel.HEIGHT) {
            store();
        }
    }

    /**
     * ビームの位置を返す
     * 
     * @return ビームの位置座標
     */
    public Point getPos() {
        return new Point(x, y);
    }

    /**
     * ビームの位置をセットする
     * 
     * @param x ビームのx座標
     * @param y ビームのy座標
     */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * ビームの幅を返す。
     * 
     * @param width ビームの幅。
     */
    public int getWidth() {
        return width;
    }

    /**
     * ビームの高さを返す。
     * 
     * @return height ビームの高さ。
     */
    public int getHeight() {
        return height;
    }

    /**
     * ビームを保管庫に入れる
     */
    public void store() {
        x = STORAGE.x;
        y = STORAGE.y;
    }

    /**
     * ビームが保管庫に入っているか
     * 
     * @return 入っているならtrueを返す
     */
    public boolean isInStorage() {
        if (x == STORAGE.x && y == STORAGE.x)
            return true;
        return false;
    }

    /**
     * ビームを描画する
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g) {
        // ビームを描画する
        g.drawImage(image, x, y, null);
    }

    /**
     * イメージをロードする
     *  
     */
    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("image/beam.gif"));
        image = icon.getImage();

        // 幅と高さをセット
        width = image.getWidth(panel);
        height = image.getHeight(panel);
    }
}