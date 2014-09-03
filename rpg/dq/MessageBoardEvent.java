/*
 * Created on 2006/02/06
 */

/**
 * @author mori
 */
public class MessageBoardEvent extends Event {
    private String message;
    
    public MessageBoardEvent(int x, int y, String message) {
        super(x, y, Chipset.MSGBOARD, true);
        this.message = message;
    }
    
    /**
     * メッセージを読む
     * @return メッセージ
     */
    public String read() {
        // TODO: サーバーから読み取る
        return message;
    }

    /**
     * メッセージを書き込む
     * @param message
     */
    public void write(String message) {
        // TODO:サーバーへ書き込む
    }

    /**
     * メッセージを返す
     * @return メッセージ
     */
    public String getMessage() {
        return message;
    }

    public String toString() {
        return "BOARD:" + super.toString() + ":" + message;
    }
}
