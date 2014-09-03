/*
 * Created on 2007/04/15
 */
package dqc;

public class Hero extends Chara {

    public Hero(int x, int y, int imageNo, int direction, int moveType, String message, Map map) {
        super(x, y, imageNo, direction, moveType, message, map);
    }
    
    /**
     * 移動処理（オーバーライド）
     * 
     */
    public void move() {
        switch (direction) {
            case DOWN :
                if (moveDown()) {
                    // 移動が完了した
                    // 移動イベント
                    Event evt = map.getEvent(x, y);
                    if (evt instanceof MoveEvent) {
                        evt.start(this, map, null);
                    }
                }
                break;
            case UP :
                if (moveUp()) {
                    // 移動が完了した
                    // 移動イベント
                    Event evt = map.getEvent(x, y);
                    if (evt instanceof MoveEvent) {
                        evt.start(this, map, null);
                    }
                }
                break;
            case LEFT :
                if (moveLeft()) {
                    // 移動が完了した
                    // 移動イベント
                    Event evt = map.getEvent(x, y);
                    if (evt instanceof MoveEvent) {
                        evt.start(this, map, null);
                    }
                }
                break;
            case RIGHT :
                if (moveRight()) {
                    // 移動が完了した
                    // 移動イベント
                    Event evt = map.getEvent(x, y);
                    if (evt instanceof MoveEvent) {
                        evt.start(this, map, null);
                    }
                }
                break;
        }
    }
    
    /**
     * 向いている方向にキャラクターがいたら話す
     * 
     */
    public void talk(MessageWindow msgWnd) {
        // キャラクターの向いている方向の1マスとなりの座標を求める
        int nextX = 0;
        int nextY = 0;
        switch (direction) {
            case DOWN:
                nextX = x;
                nextY = y + 1;
                // カウンターがある場合はさらに先を指定
                if (map.getMapchipNo(nextX, nextY) == 388) {  // TODO: 388はカウンター
                    nextY++;
                }
                break;
            case UP:
                nextX = x;
                nextY = y - 1;
                // カウンターがある場合はさらに先を指定
                if (map.getMapchipNo(nextX, nextY) == 388) {  // TODO: 388はカウンター
                    nextY--;
                }
                break;
            case LEFT:
                nextX = x - 1;
                nextY = y;
                // カウンターがある場合はさらに先を指定
                if (map.getMapchipNo(nextX, nextY) == 388) {  // TODO: 388はカウンター
                    nextX--;
                }
                break;
            case RIGHT:
                nextX = x + 1;
                nextY = y;
                // カウンターがある場合はさらに先を指定
                if (map.getMapchipNo(nextX, nextY) == 388) {  // TODO: 388はカウンター
                    nextX++;
                }
                break;
        }
        
        // その方向にキャラクターがいるか調べる
        Event evt = map.getEvent(nextX, nextY);
        if (evt != null && evt instanceof TalkEvent) {
            evt.start(this, map, msgWnd);
        } else {
            msgWnd.setMessage("その方向には誰もいない。");
            msgWnd.show();
        }
    }
    
    /**
     * 足元を調べる
     * 
     */
    public void search(MessageWindow msgWnd) {
        Event evt = map.getEvent(x, y);
        if (evt != null && evt instanceof SearchEvent) {
            evt.start(this, map, msgWnd);
        } else {
            msgWnd.setMessage("しかし何も見つからなかった。");
            msgWnd.show();
        }
    }
    
    /**
     * 開ける
     *
     */
    public void open(MessageWindow msgWnd) {
        // キャラクターの向いている方向の1マスとなりの座標を求める
        int nextX = 0;
        int nextY = 0;
        switch (direction) {
            case DOWN:
                nextX = x;
                nextY = y + 1;
                break;
            case UP:
                nextX = x;
                nextY = y - 1;
                break;
            case LEFT:
                nextX = x - 1;
                nextY = y;
                break;
            case RIGHT:
                nextX = x + 1;
                nextY = y;
                break;
        }
        
        // 扉があるか調べる
        Event evt = map.getEvent(nextX, nextY);
        if (evt instanceof OpenEvent) {
            evt.start(this, map, msgWnd);
        }
    }
}
