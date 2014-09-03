/*
 * Created on 2005/04/16
 *
 */
import java.awt.*;
/**
 * 獲物クラス
 * 
 * @author mori
 *  
 */
public class Prey {
    // グリッドサイズ
    private static final int GS = 8;
    // 方向定数
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;
    // 記録する足跡の最大数
    public static final int MAX_TRAIL_LENGTH = 128;

    // 位置
    public int x;
    public int y;

    // 足跡配列
    private Point[] trail;

    public Prey() {
        x = MainPanel.COL / 2;
        y = MainPanel.ROW / 2;
    }

    public Prey(int x, int y) {
        this.x = x;
        this.y = y;

        trail = new Point[MAX_TRAIL_LENGTH];
    }

    /**
     * dirで指定された方向に移動する
     * 
     * @param dir 移動方向
     */
    public void move(int dir) {
        switch (dir) {
            case UP :
                y--;
                // 移動とともに足跡を記録する
                dropBreadCrumb();
                break;
            case DOWN :
                y++;
                dropBreadCrumb();
                break;
            case LEFT :
                x--;
                dropBreadCrumb();
                break;
            case RIGHT :
                x++;
                dropBreadCrumb();
                break;
        }
    }

    /**
     * 獲物を描画する
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g) {
        drawTrail(g);

        g.setColor(Color.BLUE);
        g.fillRect(x * GS, y * GS, GS, GS);
    }

    public Point[] getBreadCrumb() {
        return trail;
    }

    /**
     * 足跡を記録する（パンくずを落とす）
     *  
     */
    private void dropBreadCrumb() {
        // 一番新しい足跡は配列の最初に記録するため
        // 要素を1つずつ後ろにずらす
        for (int i = MAX_TRAIL_LENGTH - 1; i > 0; i--) {
            trail[i] = trail[i - 1];
        }

        // 新しい足跡を記録する
        trail[0] = new Point(x, y);
    }

    /**
     * 足跡を表示する
     * 
     * @param g 描画オブジェクト
     */
    private void drawTrail(Graphics g) {
        g.setColor(new Color(255, 204, 153));
        for (int i = 0; i < MAX_TRAIL_LENGTH; i++) {
            if (trail[i] == null)
                break;
            g.fillRect(trail[i].x * GS, trail[i].y * GS, GS, GS);
        }
    }
}