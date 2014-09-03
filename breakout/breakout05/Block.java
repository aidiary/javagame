import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/*
 * Created on 2007/05/05
 * 
 * ブロッククラス
 */

public class Block {
    public static final int WIDTH = 40;
    public static final int HEIGHT = 16;

    // ボールの当たり位置
    public static final int NO_COLLISION = 0; // 未衝突
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int UP = 4;
    public static final int DOWN_LEFT = 5;
    public static final int DOWN_RIGHT = 6;
    public static final int UP_LEFT = 7;
    public static final int UP_RIGHT = 8;

    // 位置（左上隅の座標）
    private int x, y;

    // ボールが当たって消されたか
    private boolean isDeleted;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        isDeleted = false;
    }

    /**
     * ブロックを描画
     * 
     * @param g
     */
    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(x, y, WIDTH, HEIGHT);

        // 枠線を描画
        g.setColor(Color.BLACK);
        g.drawRect(x, y, WIDTH, HEIGHT);
    }

    /**
     * ボールと衝突したか
     * 
     * @param ball ボール
     * @return 衝突位置
     */
    public int collideWith(Ball ball) {
        Rectangle blockRect = new Rectangle(x, y, WIDTH, HEIGHT);

        int ballX = ball.getX();
        int ballY = ball.getY();
        int ballSize = ball.getSize();
        if (blockRect.contains(ballX, ballY)
                && blockRect.contains(ballX + ballSize, ballY)) {
            // ブロックの下から衝突＝ボールの左上・右上の点がブロック内
            return DOWN;
        } else if (blockRect.contains(ballX + ballSize, ballY)
                && blockRect.contains(ballX + ballSize, ballY + ballSize)) {
            // ブロックの左から衝突＝ボールの右上・右下の点がブロック内
            return LEFT;
        } else if (blockRect.contains(ballX, ballY)
                && blockRect.contains(ballX, ballY + ballSize)) {
            // ブロックの右から衝突＝ボールの左上・左下の点がブロック内
            return RIGHT;
        } else if (blockRect.contains(ballX, ballY + ballSize)
                && blockRect.contains(ballX + ballSize, ballY + ballSize)) {
            // ブロックの上から衝突＝ボールの左下・右下の点がブロック内
            return UP;
        } else if (blockRect.contains(ballX + ballSize, ballY)) {
            // ブロックの左下から衝突＝ボールの右上の点がブロック内
            return DOWN_LEFT;
        } else if (blockRect.contains(ballX, ballY)) {
            // ブロックの右下から衝突＝ボールの左上の点がブロック内
            return DOWN_RIGHT;
        } else if (blockRect.contains(ballX + ballSize, ballY + ballSize)) {
            // ブロックの左上から衝突＝ボールの右下の点がブロック内
            return UP_LEFT;
        } else if (blockRect.contains(ballX, ballY + ballSize)) {
            // ブロックの右上から衝突＝ボールの左下の点がブロック内
            return UP_RIGHT;
        }

        return NO_COLLISION;
    }

    /**
     * ブロックを消去
     * 
     */
    public void delete() {
        // TODO: ここでブロックが壊れる効果音
        // TODO: ここで派手なアクション

        isDeleted = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}
