import java.awt.Graphics;

/*
 * Created on 2005/05/13
 *
 */

/**
 * 追跡者と獲物の親クラス
 * @author mori
 *
 */
public abstract class Chara {
    // 位置
    public int x;
    public int y;

    public Chara(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * dirで指定された方向に移動する
     * 012
     * 345
     * 678
     * @param dir 移動方向
     */
    public void move(int dir) {
        switch (dir) {
            case 0:
                x--; y--;
                break;
            case 1:
                y--;
                break;
            case 2:
                x++; y--;
                break;
            case 3:
                x--;
                break;
            case 4:
                break;
            case 5:
                x++;
                break;
            case 6:
                x--; y++;
                break;
            case 7:
                y++;
                break;
            case 8:
                x++; y++;
                break;
        }
        
        // 画面外に出てないかチェック
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
     * 獲物を描画する
     * 
     * @param g 描画オブジェクト
     */
    public abstract void draw(Graphics g);
}
