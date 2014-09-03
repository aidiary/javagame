package dqc;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

/*
 * キャラクタークラス
 */

public class Chara extends TalkEvent {
    // 方向定数
    public static final int DOWN = 0;
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    // キャラクターファイルの1行の人数
    protected static final int NUM_CHARAS_IN_ROW = 16;
    
    // 移動スピード（ピクセル単位）
    protected static final int SPEED = 4;

    // 移動確率
    public static final double PROB_MOVE = 0.005;
    
    // 移動タイプ
    public static final int STOP = 0;
    public static final int MOVE_AROUND = 1;
    
    // 向いている方向
    protected int direction;
    // 移動タイプ
    protected int moveType;
    
    // 移動（スクロール）中か
    protected boolean isMoving;
    // 移動中の場合の移動ピクセル数
    protected int movingLength;
    
    // アニメーションカウンタ
    protected int counter;

    // キャラクターイメージ（クラス変数なのでオブジェクトで共有）
    protected static Image charaImage;
    
    // マップへの参照
    protected Map map;

    public Chara(int x, int y, int imageNo, int direction, int moveType, String message, Map map) {
        super(x, y, imageNo, message);
        this.direction = direction;
        this.moveType = moveType;
        this.map = map;

        // イメージをロード
        if (charaImage == null) {
            loadImage();
        }
        
        // アニメーションタイマー起動
        Timer animationTimer = new Timer();
        animationTimer.schedule(new AnimationTask(), 0, 300);
    }

    /**
     * キャラクターの描画（Event.draw()のオーバーライド）
     * 
     * @param g グラフィックスオブジェクト
     * @param offsetX X方向オフセット
     * @param offsetY Y方向オフセット
     */
    public void draw(Graphics g, int offsetX, int offsetY) {
        int cx = (imageNo % NUM_CHARAS_IN_ROW) * (CS * 2);
        int cy = (imageNo / NUM_CHARAS_IN_ROW) * (CS * 4);
        
        g.drawImage(charaImage,
                px - offsetX,
                py - offsetY,
                px - offsetX + CS,
                py - offsetY + CS,
                cx + counter * CS,
                cy + direction * CS,
                cx + counter * CS + CS,
                cy + direction * CS + CS,
                null);
    }

    /**
     * 移動処理
     * 
     */
    public void move() {
        switch (direction) {
            case DOWN :
                if (moveDown()) {
                    // 移動が完了した
                }
                break;
            case UP :
                if (moveUp()) {
                    // 移動が完了した
                }
                break;
            case LEFT :
                if (moveLeft()) {
                    // 移動が完了した
                }
                break;
            case RIGHT :
                if (moveRight()) {
                    // 移動が完了した
                }
                break;
        }
    }

    /**
     * 下へ移動する
     * 
     * @return 1マス移動が完了したらtrueを返す。移動中はfalseを返す。
     */
    protected boolean moveDown() {
        // 1マス先の座標
        int nextX = x;
        int nextY = y + 1;
        if (nextY > map.getRow() - 1) nextY = map.getRow() - 1;
        // 移動可能な場所ならば移動開始
        if (map.isMovable(nextX, nextY)) {
            py += SPEED;
            if (py > map.getHeight() - CS) py = map.getHeight() - CS;
            movingLength += SPEED;
            // 移動が1マス分を超えていたら
            if (movingLength >= CS) {
                // 座標を更新
                y++;
                if (y > map.getRow() - 1) y = map.getRow() - 1;
                py = y * CS;
                // 移動完了
                isMoving = false;
                return true;
            }
        } else {
            isMoving = false;
            // 元の位置に戻す
            px = x * CS;
            py = y * CS;
        }
        
        return false;
    }
    
    /**
     * 上へ移動する
     * 
     * @return 1マス移動が完了したらtrueを返す。移動中はfalseを返す。
     */
    protected boolean moveUp() {
        // 1マス先の座標
        int nextX = x;
        int nextY = y - 1;
        if (nextY < 0) nextY = 0;
        // 移動可能な場所ならば移動開始
        if (map.isMovable(nextX, nextY)) {
            py -= SPEED;
            if (py < 0) py = 0;
            movingLength += SPEED;
            // 移動が1マス分を超えていたら
            if (movingLength >= CS) {
                // 座標を更新
                y--;
                if (y < 0) y = 0;
                py = y * CS;
                // 移動完了
                isMoving = false;
                return true;
            }
        } else {
            isMoving = false;
            // 元の位置に戻す
            px = x * CS;
            py = y * CS;
        }
        
        return false;
    }
    
    /**
     * 左へ移動する
     * 
     * @return 1マス移動が完了したらtrueを返す。移動中はfalseを返す。
     */
    protected boolean moveLeft() {
        // 1マス先の座標
        int nextX = x - 1;
        int nextY = y;
        if (nextX < 0) nextX = 0;
        // 移動可能な場所ならば移動開始
        if (map.isMovable(nextX, nextY)) {
            px -= SPEED;
            if (px < 0) px = 0;
            movingLength += SPEED;
            // 移動が1マス分を超えていたら
            if (movingLength >= CS) {
                // 座標を更新
                x--;
                if (x < 0) x = 0;
                px = x * CS;
                // 移動完了
                isMoving = false;
                return true;
            }
        } else {
            isMoving = false;
            // 元の位置に戻す
            px = x * CS;
            py = y * CS;
        }
        
        return false;
    }
    
    /**
     * 右へ移動する
     * 
     * @return 1マス移動が完了したらtrueを返す。移動中はfalseを返す。
     */
    protected boolean moveRight() {
        // 1マス先の座標
        int nextX = x + 1;
        int nextY = y;
        if (nextX > map.getCol() - 1) nextX = map.getCol() - 1;
        // 移動可能な場所ならば移動開始
        if (map.isMovable(nextX, nextY)) {
            px += SPEED;
            if (px > map.getWidth() - CS) px = map.getWidth() - CS;
            movingLength += SPEED;
            // 移動が1マス分を超えていたら
            if (movingLength >= CS) {
                // 座標を更新
                x++;
                if (x > map.getCol() - 1) x = map.getCol() - 1;
                px = x * CS;
                // 移動完了
                isMoving = false;
                return true;
            }
        } else {
            isMoving = false;
            // 元の位置に戻す
            px = x * CS;
            py = y * CS;
        }
        
        return false;
    }
    
    /**
     * 方向をセット
     * 
     * @param direction 方向定数
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }
    
    /**
     * 方向を返す
     * 
     * @return 方向定数
     */
    public int getDirection() {
        return direction;
    }
    
    /**
     * 移動（スクロール）中か調べる
     * @return
     */
    public boolean isMoving() {
        return isMoving;
    }
    
    /**
     * 移動（スクロール）中かをセット
     * 
     * @param flag 移動（スクロール）を開始するならtrue
     */
    public void setMoving(boolean flag) {
        isMoving = flag;
        // 移動距離を初期化
        movingLength = 0;
    }
    
    /**
     * 移動タイプを返す
     * 
     * @return 移動タイプ
     */
    public int getMoveType() {
        return moveType;
    }
    
    /**
     * メッセージを返す
     * 
     * @return メッセージ
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * マップへの参照をセット
     * 
     * @param map マップへの参照
     */
    public void setMap(Map map) {
        this.map = map;
    }
    
    /**
     * イメージをロード
     * 
     */
    private void loadImage() {
        charaImage = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("image/character.png"));
    }

    /**
     * イベントの文字列を返す（デバッグ用）
     * 
     * @return イベント文字列
     */
    public String toString() {
        return "CHARA," + x + "," + y + "," + imageNo + "," + direction + "," + moveType + "," + message;
    }

    /**
     * アニメーション用タイマータスククラス
     * 
     */
    private class AnimationTask extends TimerTask {
        public void run() {
            // カウンターの切替
            if (counter == 0) {
                counter = 1;
            } else if (counter == 1) {
                counter = 0;
            }
        }
    }

    /**
     * イベント開始
     * 
     */
    public void start(Hero hero, Map map, MessageWindow msgWnd) {
        // 主人公の方へ向ける
        switch (hero.getDirection()) {
            case DOWN:
                setDirection(UP);
                break;
            case UP:
                setDirection(DOWN);
                break;
            case LEFT:
                setDirection(RIGHT);
                break;
            case RIGHT:
                setDirection(LEFT);
                break;
        }
        // メッセージをセットする
        msgWnd.setMessage(getMessage());
        // メッセージウィンドウを表示
        msgWnd.show();
    }
}
