/*
 * Created on 2005/11/19
 *
 */

/**
 * @author mori
 *
 */
public class TreasureEvent extends Event {
    // 宝箱に入っているアイテム名
    private String itemName;

    /**
     * @param x X座標
     * @param y Y座標
     * @param itemName 手に入るアイテム名
     */
    public TreasureEvent(int x, int y, String itemName) {
        super(x, y, Chipset.TREASURE, false);
        this.itemName = itemName;
    }

    /**
     * アイテム名を返す
     * @return アイテム名
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * イベントを文字列に変換（デバッグ用）
     */
    public String toString() {
        return "TREASURE:" + super.toString() + ":" + itemName;
    }
}
