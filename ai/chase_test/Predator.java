/*
 * Created on 2005/01/16
 *
 */
import java.awt.*;
/**
 * 追跡者クラス
 * 
 * @author mori
 *  
 */
public class Predator {
    // グリッドサイズ
    private static final int GS = 8;
    // 位置
    public int x;
    public int y;

    public Predator() {
        this(0, 0);
    }

    public Predator(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 最も基本的な方法で獲物を追跡する
     * 
     * @param prey 獲物
     */
    public void chase(Prey prey) {
        if (x > prey.x) {
            x--;
        } else if (x < prey.x) {
            x++;
        }

        if (y > prey.y) {
            y--;
        } else if (y < prey.y) {
            y++;
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