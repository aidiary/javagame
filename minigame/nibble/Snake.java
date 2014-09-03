/*
 * 作成日: 2004/12/14
 *
 */
import java.awt.*;
/**
 * 蛇を表すクラス
 * 
 * @author mori
 *  
 */
public class Snake {
    /**
     * 上向きを表す定数
     */
    public static final int UP = 1;
    /**
     * 右向きを表す定数
     */
    public static final int RIGHT = 2;
    /**
     * 下向きを表す定数
     */
    public static final int DOWN = 3;
    /**
     * 左向きを表す定数
     */
    public static final int LEFT = 4;

    // 蛇の最大長
    private static final int MAXSIZE = 256;
    // 蛇の身体の座標
    private Point[] body;
    // 蛇の頭の位置
    private int head;
    // 蛇のサイズ
    private int size;
    // 蛇の方向
    private int dir;
    // 蛇のイメージ
    private Image snakeImage;

    /**
     * (0, 0)の座標に蛇を作成する
     *  
     */
    public Snake(MainPanel panel) {
        this(0, 0, panel);
    }

    /**
     * (x, y)の座標に蛇を作成する
     * 
     * @param x 蛇のx座標
     * @param y 蛇のy座標
     */
    public Snake(int x, int y, MainPanel panel) {
        body = new Point[MAXSIZE];
        size = 1;
        head = size - 1;
        dir = RIGHT;
        for (int i = 0; i < MAXSIZE; i++) {
            body[i] = new Point(x, y);
        }
        // 蛇のイメージをロードする
        loadImage(panel);
    }

    /**
     * 蛇が蛙を食べたかどうか
     * 
     * @param toad 蛙
     * @return 蛙を食べたらtrue、食べなかったらfalse
     */
    public boolean eat(Toad toad) {
        // 蛙の位置を取得
        Point toadPos = toad.getPos();
        // 蛇の頭の座標と蛙の座標が一致していれば食べられる
        if (body[head].x == toadPos.x && body[head].y == toadPos.y) {
            expand(toad.getEnergy());
            return true;
        }
        return false;
    }

    /**
     * 蛇の身体をiだけ伸ばす
     * 
     * @param n 伸ばす量
     */
    public void expand(int n) {
        for (int i = 0; i < n; i++) {
            // サイズを1伸ばす
            size++;
            // 頭は配列の最後の要素（body[]のsize-1の位置）
            head = size - 1;
            // 頭の位置はbody[head-1]の位置と同じにしておく
            body[head] = new Point(body[head - 1].x, body[head - 1].y);
        }
    }

    /**
     * 蛇のサイズを返す
     * 
     * @return 蛇のサイズ
     */
    public int getSize() {
        return size;
    }

    /**
     * 蛇の向いている方向を返す
     * 
     * @return 蛇の向いている方向
     */
    public int getDir() {
        return dir;
    }

    /**
     * 蛇の向きをセットする
     * 
     * @param dir 蛇の向いている方向
     */
    public void setDir(int dir) {
        this.dir = dir;
    }

    /**
     * 蛇の頭が画面外にあるか
     * 
     * @return 画面外だったらtrue、画面内ならfalse
     */
    public boolean isOutOfField() {
        if (body[head].x < 0 || body[head].x > MainPanel.COL - 1
                || body[head].y < 0 || body[head].y > MainPanel.ROW - 1) {
            return true;
        }

        return false;
    }

    /**
     * 蛇を移動する
     *  
     */
    public void move() {
        // 体をずらす
        // 0123
        // ■■■■
        //  0123
        //  ■■■■
        // iの位置には元のi+1の値が入る
        // 頭は移動する方向によって決まる
        for (int i = 0; i < head; i++) {
            body[i] = body[i + 1];
        }
        // 頭はdirの方向に移動させる
        switch (dir) {
            case UP :
                body[head] = new Point(body[head].x, body[head].y - 1);
                break;
            case RIGHT :
                body[head] = new Point(body[head].x + 1, body[head].y);
                break;
            case DOWN :
                body[head] = new Point(body[head].x, body[head].y + 1);
                break;
            case LEFT :
                body[head] = new Point(body[head].x - 1, body[head].y);
                break;
        }
    }

    /**
     * 頭が自分の体に触れていないか
     * 
     * @return 触れていたらtrue、触れていなかったらfalseを返す
     */
    public boolean touchOwnBody() {
        for (int i = 0; i < head; i++) {
            if (body[head].x == body[i].x && body[head].y == body[i].y) {
                return true;
            }
        }

        return false;
    }

    /**
     * 蛇を描画する
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g) {
        for (int i = 0; i < size; i++) {
            g.drawImage(snakeImage, body[i].x * MainPanel.GS, body[i].y
                    * MainPanel.GS, null);
        }
    }

    /**
     * 蛇の画像をロードする
     * 
     * @param panel MainPanelへの参照
     */
    private void loadImage(MainPanel panel) {
        MediaTracker tracker = new MediaTracker(panel);
        snakeImage = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("snake.gif"));
        tracker.addImage(snakeImage, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}