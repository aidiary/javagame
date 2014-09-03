/*
 * Created on 2005/01/16
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
    // 位置
    public int x;
    public int y;

    public Prey() {
        x = MainPanel.COL / 2;
        y = MainPanel.ROW / 2;
    }

    public Prey(int x, int y) {
        this.x = x;
        this.y = y;
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
                break;
            case DOWN :
                y++;
                break;
            case LEFT :
                x--;
                break;
            case RIGHT :
                x++;
                break;
        }
    }

    /**
     * 獲物を描画する
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x * GS, y * GS, GS, GS);
    }
}