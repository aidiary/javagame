/*
 * Created on 2007/04/01
 */
package dqc;

public abstract class MoveEvent extends Event {
    // 移動先マップ名
    protected String destMapName;
    // 移動先X座標
    protected int destX;
    // 移動先Y座標
    protected int destY;
    
    public MoveEvent(int x, int y, int imageNo, String destMapName, int destX, int destY) {
        super(x, y, imageNo, true);
        this.destMapName = destMapName;
        this.destX = destX;
        this.destY = destY;
    }

    public abstract void start(Hero hero, Map map, MessageWindow msgWnd);
    
    /**
     * 移動先マップ名を返す
     * 
     * @return 移動先マップ名
     */
    public String getDestMapName() {
        return destMapName;
    }
    
    /**
     * 移動先X座標を返す
     * 
     * @return 移動先X座標
     */
    public int getDestX() {
        return destX;
    }
    
    /**
     * 移動先Y座標を返す
     * 
     * @return 移動先Y座標
     */
    public int getDestY() {
        return destY;
    }
}
