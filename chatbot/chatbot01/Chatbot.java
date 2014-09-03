/*
 * 人工無脳クラス
 * 
 * Created on 2007/01/08
 */

public class Chatbot {
    // 人工無脳の名前
    private String name;

    public Chatbot(String name) {
        this.name = name;
    }

    /**
     * 入力メッセージに対する反応メッセージを返す
     * 
     * @param message 入力メッセージ
     * @return 反応メッセージ
     */
    public String getResponse(String message) {

        // ここが改造のしどころ

        return message; // とりあえずおうむ返し
    }

    /**
     * 人工無脳の名前を返す
     * 
     * @return 名前
     */
    public String getName() {
        return name;
    }
}
