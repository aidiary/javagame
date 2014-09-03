import java.awt.geom.Ellipse2D;

/*
 * Created on 2006/12/29
 */

public class Spotlight {
    // スポットライトの範囲（円形）
    protected Ellipse2D.Double spot;

    public Spotlight() {
        this(0, 0, 0);
    }

    /**
     * コンストラクタ
     * 
     * @param x スポットライト中心のX座標
     * @param y スポットライト中心のY座標
     * @param radius スポットライトの半径
     */
    public Spotlight(int x, int y, int radius) {
        this.spot = new Ellipse2D.Double(x - radius, y - radius, radius * 2,
                radius * 2);
    }

    /**
     * スポットライトの位置をセット
     * 
     * @param x スポットライト中心のX座標
     * @param y スポットライト中心のY座標
     * @param radius スポットライトの半径
     */
    public void setSpot(int x, int y, int radius) {
        spot.x = x - radius;
        spot.y = y - radius;
        spot.width = radius * 2;
        spot.height = radius * 2;
    }

    /**
     * スポットライトの円を返す
     * @return
     */
    public Ellipse2D getSpot() {
        return spot;
    }
}
