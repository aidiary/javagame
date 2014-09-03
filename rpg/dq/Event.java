/*
 * Created on 2005/12/25
 *
 */

/**
 * @author mori
 *
 */
public abstract class Event {
    // X座標
    public int x;
    // Y座標
    public int y;
    // チップ番号
    public int chipNo;
    // ぶつかるか
    public boolean isHit;
    
    /**
     * コンストラクタ
     * @param x X座標
     * @param y Y座標
     * @param chipNo チップ番号
     * @param isHit ぶつかるか
     */
    public Event(int x, int y, int chipNo, boolean isHit) {
        this.x = x;
        this.y = y;
        this.chipNo = chipNo;
        this.isHit = isHit;
    }
    
    /**
     * イベントを文字列に変換（デバッグ用）
     */
    public String toString() {
        return x + ":" + y + ":" + chipNo + ":" + isHit;
    }
}
