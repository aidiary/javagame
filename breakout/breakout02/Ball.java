import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/*
 * Created on 2007/05/05
 * 
 * ボールクラス
 */

public class Ball {
    // サイズ
    private static final int SIZE = 8;

    // 位置（ボールを囲む矩形の左上隅）
    private int x, y;
    // 速度
    private int vx, vy;

    // 乱数生成器
    private Random rand;

    public Ball() {
        rand = new Random(System.currentTimeMillis());

        // 位置を初期化
        x = rand.nextInt(MainPanel.WIDTH - SIZE);
        y = 0;

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
            vx = -vx;
        }

        // 上下の壁にぶつかった場合にバウンド
        if (y < 0 || y > MainPanel.HEIGHT - SIZE) {
            vy = -vy;
        }
    }
}
