import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/*
 * Created on 2007/05/05
 * 
 * ボールを打つラケットクラス
 */

public class Racket {
    // ラケットのサイズ
    public static final int WIDTH = 80;
    public static final int HEIGHT = 5;

    // ボールの当たり位置
    public static final int NO_COLLISION = 0;  // 未衝突
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    // ラケットの中心位置
    private int centerPos;

    public Racket() {
        // ラケットの位置を画面の真ん中で初期化
        centerPos = MainPanel.WIDTH / 2;
    }

    /**
     * ラケットの描画
     * 
     * @param g
     */
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(centerPos - WIDTH / 2, MainPanel.HEIGHT - HEIGHT, WIDTH,
                HEIGHT);
    }

    /**
     * ラケットの移動
     * 
     * @param pos 移動先座標
     */
    public void move(int pos) {
        centerPos = pos;

        // ラケットが画面の端から飛び出ないようにする
        if (centerPos < WIDTH / 2) { // 左端
            centerPos = WIDTH / 2;
        } else if (centerPos > MainPanel.WIDTH - WIDTH / 2) { // 右端
            centerPos = MainPanel.WIDTH - WIDTH / 2;
        }
    }

    /**
     * ボールが当たった位置を返す
     * 
     * @param ball ボール
     * @return ボールに当たったらtrue
     */
    public int collideWith(Ball ball) {
        // ラケットの矩形
        Rectangle racketRectLeft = new Rectangle(
                centerPos - WIDTH / 2, MainPanel.HEIGHT - HEIGHT,
                WIDTH / 2, HEIGHT);
        Rectangle racketRectRight = new Rectangle(
                centerPos, MainPanel.HEIGHT - HEIGHT,
                WIDTH / 2, HEIGHT);
        // ボールの矩形
        Rectangle ballRect = new Rectangle(
                ball.getX(), ball.getY(),
                ball.getSize(), ball.getSize());

        // ラケットとボールの矩形領域が重なったら当たっている
        if (racketRectLeft.intersects(ballRect)) {
            return LEFT;
        } else if (racketRectRight.intersects(ballRect)) {
            return RIGHT;
        }

        return NO_COLLISION;
    }
}
