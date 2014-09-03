import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/*
 * 人工無脳クラス
 * 
 * Created on 2007/03/10
 */

public class Chatbot {
    // 人工無脳の名前
    private String name;
    // 辞書
    private ArrayList dic;
    // 乱数生成器
    private Random rand;

    public Chatbot(String name) {
        this.name = name;

        dic = new ArrayList();
        rand = new Random(System.currentTimeMillis());

        // 辞書のロード
        loadDic();
    }

    /**
     * 入力メッセージに対する反応メッセージを返す
     * 
     * @param message 入力メッセージ
     * @return 反応メッセージ
     */
    public String getResponse(String message) {
        // 辞書からランダムに会話をひっぱってくる
        String response = (String)dic.get(rand.nextInt(dic.size()));

        return response;
    }

    /**
     * 人工無脳の名前を返す
     * 
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 辞書のロード
     */
    private void loadDic() {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream("dic/basic.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                dic.add(line); // 辞書に追加
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
