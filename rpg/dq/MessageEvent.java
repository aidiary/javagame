/*
 * Created on 2006/02/04
 */

/**
 * @author mori
 */
public class MessageEvent extends Event {
    private String message;

    /**
     * @param x X座標
     * @param y Y座標
     * @param isHit ぶつかるか
     */
    public MessageEvent(int x, int y, int chipNo, String message) {
        super(x, y, chipNo, false);
        this.message = message;
    }
    
    /**
     * メッセージを返す
     * @return メッセージ
     */
    public String getMessage() {
        return message;
    }
    
    public String toString() {
        return "MESSAGE:" + super.toString() + ":" + message;
    }
}
