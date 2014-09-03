/*
 * イベントクラス
 * 
 */
package dqc;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public abstract class Event {
    public static final int CS = 32;
    // マップチップの1行の数
    public static final int NUM_CHIPS_IN_ROW = 30;
    
    // 座標（マス単位）
    protected int x, y;
    // 座標（ピクセル単位）
    protected int px, py;
    // イメージ番号
    protected int imageNo;
    // 移動可能か
    protected boolean isMovable;

    // マップチップイメージ（クラス変数なのでオブジェクトで共有）
    protected static Image mapchipImage;
    
    public Event(int x, int y, int imageNo, boolean isMovable) {
        this.x = x;
        this.y = y;
        px = x * CS;
        py = y * CS;
        this.imageNo = imageNo;
        this.isMovable = isMovable;
        
        // イメージをロード
        if (mapchipImage == null) {
            loadImage();
        }
    }
    
    /**
     * 描画
     * 
     * @param g グラフィックスオブジェクト
     * @param offsetX X方向オフセット
     * @param offsetY Y方向オフセット
     * @param mapchipImage マップチップイメージ
     */
    public void draw(Graphics g, int offsetX, int offsetY) {
        int cx = (imageNo % NUM_CHIPS_IN_ROW) * CS;
        int cy = (imageNo / NUM_CHIPS_IN_ROW) * CS;
        g.drawImage(mapchipImage,
                px - offsetX,
                py - offsetY,
                px - offsetX + CS,
                py - offsetY + CS,
                cx,
                cy,
                cx + CS,
                cy + CS,
                null);
    }
    
    public abstract void start(Hero hero, Map map, MessageWindow msgWnd);
    
    /**
     * X座標を返す
     * 
     * @return 位置のX座標（マス単位）
     */
    public int getX() {
        return x;
    }
    
    /**
     * Y座標を返す
     * 
     * @return 位置のY座標（マス単位）
     */
    public int getY() {
        return y;
    }
    
    /**
     * X座標をセット
     * 
     * @param x X座標（マス単位）
     */
    public void setX(int x) {
        this.x = x;
        px = x * CS;
    }
    
    /**
     * Y座標をセット
     * 
     * @param x Y座標（マス単位）
     */
    public void setY(int y) {
        this.y = y;
        py = y * CS;
    }
    
    /**
     * X座標（ピクセル単位）を返す
     * 
     * @return 位置のX座標（ピクセル単位）
     */
    public int getPx() {
        return px;
    }

    /**
     * Y座標（ピクセル単位）を返す
     * 
     * @return 位置のY座標（ピクセル単位）
     */
    public int getPy() {
        return py;
    }
    
    /**
     * イメージ番号を返す
     * 
     * @return イメージ番号
     */
    public int getImageNo() {
        return imageNo;
    }
    
    /**
     * 移動可能か調べる
     * 
     * @return 移動可能ならtrue
     */
    public boolean isMovable() {
        return isMovable;
    }
    
    /**
     * イメージをロード
     * 
     */
    private void loadImage() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        mapchipImage = toolkit.getImage(getClass().getClassLoader().getResource("image/mapchip.png"));
    }
    
    /**
     * イベントの文字列を返す（デバッグ用）
     * 
     * @return イベント文字列
     */
    public String toString() {
        return x + "," + y + "," + px + "," + py + "," + imageNo + "," + isMovable;
    }
}
