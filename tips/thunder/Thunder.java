import java.awt.Color;
import java.awt.Graphics;

/*
 * Created on 2006/02/04
 */

/**
 * @author mori
 */
public class Thunder {
    private static final double SPEED = 10;
    private static final int HEIGHT = 50;

    private double x;
    private double y;
    private double width;
    private boolean used;

    public Thunder() {
        this.x = y = -10;
        this.width = 0;
    }

    /**
     * サンダーを放つ
     * 
     * @param x 始点のX座標
     * @param y 始点のY座標
     * @param width サンダーの幅
     */
    public void lightning(int x, int y, double width) {
        this.x = x;
        this.y = y;
        this.width = width;
        used = true;
    }

    /**
     * 移動
     */
    public void move() {
        y += SPEED;

        // 画面外に出たら
        if (y > MainPanel.HEIGHT) {
            used = false;
        }
    }

    /**
     * 描画
     * 
     * @param g
     */
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect((int) x, (int) y, (int) width, HEIGHT);
    }

    /**
     * サンダーを使用中か
     * 
     * @return 表示中ならtrueを返す
     */
    public boolean isUsed() {
        return used;
    }
}