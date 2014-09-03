/*
 * Created on 2005/04/16
 *
 */
import java.awt.*;
import java.util.Random;
/**
 * 追跡者クラス
 * 
 * @author mori
 *  
 */
public class Predator {
    // グリッドサイズ
    private static final int GS = 8;
    // 追跡可能な最大距離
    private static final int MAX_PATH_LENGTH = 256;

    // 位置
    public int x;
    public int y;

    private Random rand = new Random();

    public Predator() {
        this(0, 0);
    }

    public Predator(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * ブレッドクラム追跡
     * 
     * @param prey 獲物
     */
    public void chase(Prey prey) {
        // 見つけた足跡のインデックス番号
        int foundCrumb = -1;

        // 獲物の足跡を取得
        Point[] trail = prey.getBreadCrumb();

        // 周囲8マスに足跡がないか調べる
        // 獲物に近い（trail[0]）場所から探していくのがミソ
        // 最初に見つかるのはプレイヤーに一番近い場所なので
        // だんだん近づいていく
        for (int i = 0; i < Prey.MAX_TRAIL_LENGTH; i++) {
            if (trail[i] == null)
                break;
            // 左上にあった！
            if (trail[i].x == x - 1 && trail[i].y == y - 1) {
                foundCrumb = i;
                break;
            }
            // 上にあった！
            if (trail[i].x == x && trail[i].y == y - 1) {
                foundCrumb = i;
                break;
            }
            // 右上にあった！
            if (trail[i].x == x - 1 && trail[i].y == y - 1) {
                foundCrumb = i;
                break;
            }
            // 右にあった！
            if (trail[i].x == x + 1 && trail[i].y == y) {
                foundCrumb = i;
                break;
            }
            // 右下にあった！
            if (trail[i].x == x + 1 && trail[i].y == y + 1) {
                foundCrumb = i;
                break;
            }
            // 下にあった！
            if (trail[i].x == x && trail[i].y == y + 1) {
                foundCrumb = i;
                break;
            }
            // 左下にあった！
            if (trail[i].x == x - 1 && trail[i].y == y + 1) {
                foundCrumb = i;
                break;
            }
            // 左にあった！
            if (trail[i].x == x - 1 && trail[i].y == y) {
                foundCrumb = i;
                break;
            }
        }

        if (foundCrumb >= 0) {
            // 獲物の足跡が見つかったらそこに移動
            x = trail[foundCrumb].x;
            y = trail[foundCrumb].y;
        } else {
            // 見つからなかったらランダムに移動
            x += rand.nextInt(3) - 1;
            y += rand.nextInt(3) - 1;
        }

        if (x < 0) {
            x = 0;
        } else if (x > MainPanel.COL - 1) {
            x = MainPanel.COL - 1;
        }
        if (y < 0) {
            y = 0;
        } else if (y > MainPanel.ROW - 1) {
            y = MainPanel.ROW - 1;
        }
    }

    /**
     * 追跡者を描画する
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x * GS, y * GS, GS, GS);
    }
}