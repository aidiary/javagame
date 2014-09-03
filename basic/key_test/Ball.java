import java.awt.*;
import java.util.*;

public class Ball {
    // 方向を表す定数
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    // ボールの大きさ
    public static final int SIZE = 16;

    // ボールの位置 (x, y) 円の左上の座標
    private int x, y;
    // ボールの速度 (vx, vy)
    private int vx, vy;
    // ボールの色
    private Color color;

    // 乱数生成器
    private static final Random rand = new Random();

    /**
     * コンストラクタ（新しいボールオブジェクトを作る工場）
     */
    public Ball(int x, int y, int vx, int vy) {
        // ボールの属性を設定
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    /**
     * ボールを移動する。
     * 
     * @param dir ボールを移動する方向。
     */
    public void move(int dir) {
        switch (dir) {
            case UP :
                y -= vy;
                break;
            case DOWN :
                y += vy;
                break;
            case LEFT :
                x -= vx;
                break;
            case RIGHT :
                x += vx;
                break;
        }

        // 端にきたらそれ以上移動しない
        if (x < 0) {
            x = 0;
        } else if (x > MainPanel.WIDTH - SIZE) {
            x = MainPanel.WIDTH - SIZE;
        }

        if (y < 0) {
            y = 0;
        } else if (y > MainPanel.HEIGHT - SIZE) {
            y = MainPanel.HEIGHT - SIZE;
        }
    }

    /**
     * ボールの色をランダムに変える。
     */
    public void changeColor() {
        int red = rand.nextInt(256); // 赤成分は0-255
        int green = rand.nextInt(256); // 緑成分は0-255
        int blue = rand.nextInt(256); // 青成分は0-255

        color = new Color(red, green, blue);
    }

    /**
     * ボールを描画する。
     * 
     * @param g 描画オブジェクト。
     */
    public void draw(Graphics g) {
        // 色をセットする
        g.setColor(color);
        // 丸を描く（ボールのつもり）
        g.fillOval(x, y, SIZE, SIZE);
    }
}