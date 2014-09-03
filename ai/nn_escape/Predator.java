import java.awt.Color;
import java.awt.Graphics;

/*
 * Created on 2005/05/13
 *
 */

/**
 * 追跡者クラス
 * プレイヤーが操作する
 * @author mori
 *
 */
public class Predator extends Chara {
    private static final int GS = MainPanel.GS;

    public Predator(int x, int y) {
        super(x, y);
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
