/*
 * Created on 2007/04/01
 */
package dqc;

public class Treasure extends SearchEvent {
    // 宝箱のアイテム名
    private String itemName;
    
    /**
     * コンストラクタ
     * 
     * @param x X座標
     * @param y Y座標
     * @param itemName アイテム名
     */
    public Treasure(int x, int y, int imageNo, String itemName) {
        super(x, y, imageNo, true);  // 宝箱は移動可能
        
        this.itemName = itemName;
    }
    
    /**
     * アイテム名を返す
     * 
     * @return アイテム名
     */
    public String getItemName() {
        return itemName;
    }
    
    // TODO: itemNameから本物のアイテムオブジェクトを作成して返す
    // heroオブジェクトを引数で受けてhaveItemでもよい

    /**
     * イベントの文字列を返す（デバッグ用）
     * 
     * @return イベント文字列
     */
    public String toString() {
        return "TREASURE," + x + "," + y + "," + imageNo + "," + itemName;
    }

    public void start(Hero hero, Map map, MessageWindow msgWnd) {
        DQC.soundManager.playSE("treasure");
        msgWnd.setMessage(getItemName() + "を手に入れた。");
        // TODO: アイテム入手処理
        // hero.take(treasure.getItem());
        msgWnd.show();
        map.removeEvent(this);
    }
}
