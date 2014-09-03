/*
 * Created on 2005/12/25
 *
 */

/**
 * @author mori
 *
 */
public class DoorEvent extends Event {

    /**
     * @param x X座標
     * @param y Y座標
     */
    public DoorEvent(int x, int y) {
        super(x, y, Chipset.DOOR, true);
    }
    
    /**
     * イベントを文字列に変換（デバッグ用）
     */
    public String toString() {
        return "DOOR:" + super.toString();
    }
}
