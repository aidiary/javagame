/*
 * Created on 2007/04/01
 */
package dqc;

public class Door extends OpenEvent {
    // TODO: 鍵によって開けられるようにドアの種類を追加
    
    public Door(int x, int y, int imageNo) {
        super(x, y, imageNo);  // ドアは移動不可
    }
    
    /**
     * イベントの文字列を返す（デバッグ用）
     * 
     * @return イベント文字列
     */
    public String toString() {
        return "DOOR," + x + "," + y + "," + imageNo;
    }

    public void start(Hero hero, Map map, MessageWindow msgWnd) {
        DQC.soundManager.playSE("door");
        map.removeEvent(this);
    }
}
