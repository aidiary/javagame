/*
 * Created on 2005/11/12
 *
 */

/**
 * @author mori
 *
 */
public abstract class Event {
    // X座標
    protected int x;
    // Y座標
    protected int y;
    // チップ番号
    protected int chipNo;
    // ぶつかるか
    protected boolean isHit;
    
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
