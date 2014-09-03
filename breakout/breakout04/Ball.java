import java.awt.Color;
import java.awt.Graphics;

/*
 * Created on 2007/05/05
 * 
 * ボールクラス
 */

public class Ball {
    // サイズ
    public static final int SIZE = 8;

    // 位置（ボールを囲む矩形の左上隅）
    private int x, y;
    // 速度
    private int vx, vy;

    public Ball() {
        // 位置を初期化
        x = (MainPanel.WIDTH - SIZE) / 2;
        y = (MainPanel.HEIGHT - Racket.HEIGHT - SIZE * 2);

        // 速度を初期化（とりあえず固定）
        vx = 5;
        vy = 5;
    }

    /**
     * ボールを描画
     * 
     * @param g
     */
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, SIZE, SIZE);
    }

    /**
     * ボールの移動
     * 
     */
    public void move() {
        x += vx;
        y += vy;

        // 左右の壁にぶつかった場合にバウンド
        if (x < 0 || x > MainPanel.WIDTH - SIZE) {
            boundX();
        }

        // 上の壁にぶつかった場合にバウンド
        if (y < 0) {
            boundY();
        }
    }

    /**
     * X方向のバウンド
     * 
     */
    public void boundX() {
        vx = -vx;
    }

    /**
     * Y方向のバウンド
     * 
     */
    public void boundY() {
        vy = -vy;
    }

    /**
     * ななめにバウンド
     * 
     */
    public void boundXY() {
        vx = -vx;
        vy = -vy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return SIZE;
    }
}
