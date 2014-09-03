/*
 * イベントローダー
 * 
 */
package dqc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class EventLoader {
    // 呼び出し元のMapへの参照
    private Map map;
    
    public EventLoader(Map map) {
        this.map = map;
    }
    
    public ArrayList load(String eventFile) {
        // 呼び出し元に戻すイベントリスト
        ArrayList eventList = new ArrayList();
        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream(eventFile), "Shift_JIS"));
            String line;
            while ((line = br.readLine()) != null) {
                // 空行を読み飛ばす
                if (line.equals("")) continue;
                // コメント行を読み飛ばす
                if (line.startsWith("#")) continue;
                StringTokenizer st = new StringTokenizer(line, ",");
                // イベント情報を取得する
                // イベントタイプを取得してイベントごとに処理する
                String eventType = st.nextToken();
                if (eventType.equals("BGM")) {  // BGMイベント
                    map.setBGM(st.nextToken());
                } else if (eventType.equals("CHARA")) {  // キャラクターイベント
                    eventList.add(createChara(st));
                } else if (eventType.equals("TREASURE")) {  // 宝箱イベント
                    eventList.add(createTreasure(st));
                } else if (eventType.equals("DOOR")) {  // ドアイベント
                    eventList.add(createDoor(st));
                } else if (eventType.equals("MOVE")) {  // 移動イベント
                    eventList.add(createMove(st));
                }
            }
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return eventList;
    }

    /**
     * CHARAイベントを読み込んでCharaオブジェクトを生成
     * 
     * @param st
     */
    private Chara createChara(StringTokenizer st) {
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        int imageNo = Integer.parseInt(st.nextToken());
        int direction = Integer.parseInt(st.nextToken());
        int moveType = Integer.parseInt(st.nextToken());
        String message = st.nextToken();
        Chara chara = new Chara(x, y, imageNo, direction, moveType, message, map);

        return chara;
    }
    
    /**
     * TREASUREイベントを読み込んでTreasureオブジェクトを生成
     * 
     * @param st
     */
    private Treasure createTreasure(StringTokenizer st) {
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        int imageNo = Integer.parseInt(st.nextToken());
        String itemName = st.nextToken();
        Treasure treasure = new Treasure(x, y, imageNo, itemName);
        
        return treasure;
    }

    /**
     * DOORイベントを読み込んでDoorオブジェクトを生成
     * 
     * @param st
     */
    private Door createDoor(StringTokenizer st) {
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        int imageNo = Integer.parseInt(st.nextToken());
        Door door = new Door(x, y, imageNo);
        
        return door;
    }
    
    /**
     * MOVEイベントを読み込んでMoveオブジェクトを生成
     * 
     * @param st
     */
    private Move createMove(StringTokenizer st) {
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        int imageNo = Integer.parseInt(st.nextToken());
        String destMapName = st.nextToken();
        int destX = Integer.parseInt(st.nextToken());
        int destY = Integer.parseInt(st.nextToken());
        Move move = new Move(x, y, imageNo, destMapName, destX, destY);
        
        return move;
    }
}
