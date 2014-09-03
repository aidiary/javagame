import java.awt.*;

/*
 * Created on 2007/04/29
 */

public class Ball {
    // ボールの大きさ
    private static final int SIZE = 10;
    // ボールの位置 (x, y) 円の左上の座標
    private int x, y;
    // ボールの速度 (vx, vy)
    protected int vx, vy;

    public Ball(int x, int y, int vx, int vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public void move() {
        // ボールを速度分だけ移動させる
        x += vx;
        y += vy;

        // 左または右に当たったらx方向速度の符号を反転させる
        if (x < 0 || x > MainPanel.WIDTH - SIZE) {
            vx = -vx;
        }

        // 上または下に当たったらy方向速度の符号を反転させる
        if (y < 0 || y > MainPanel.HEIGHT - SIZE) {
            vy = -vy;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, SIZE, SIZE);
    }
}
