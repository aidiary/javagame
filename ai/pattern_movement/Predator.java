import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/*
 * Created on 2005/04/10
 *
 */

/**
 * 追跡者クラス
 * 
 * @author mori
 *
 */
public class Predator {
    // グリッドサイズ
    private static final int GS = 8;
    // 追跡可能な最大距離
    private static final int MAX_PATH_LENGTH = 4096;

    // 位置
    public int x;
    public int y;

    // 追跡経路
    private Point[] path;
    // 追跡経路中の何ステップめか
    private int currentStep;

    public Predator() {
        this(0, 0);
    }

    public Predator(int x, int y) {
        this.x = x;
        this.y = y;
        
        path = new Point[MAX_PATH_LENGTH];
    }

    /**
     * 移動する
     */
    public void move() {
        // もし最後まで移動したら最初の地点へ戻る
        // 経路がループしてたら永遠に往復する
        if (path[currentStep] == null) {
            currentStep = 0;
        }

        x = path[currentStep].x;
        y = path[currentStep].y;
        currentStep++;
    }

    /**
     * 追跡者を描画する
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g) {
//        g.setColor(Color.YELLOW);
//        for (int i=0; i<MAX_PATH_LENGTH; i++) {
//            if (path[i] == null) break;
//            g.fillRect(path[i].x * GS, path[i].y * GS, GS, GS);
//        }

        g.setColor(Color.RED);
        g.fillRect(x * GS, y * GS, GS, GS);
    }
    
    
    /**
     * 経路を作成する
     * 
     * @param start 始点
     * @param end 終点
     */
    public void buildPathSegment(Point start, Point end) {       
        int nextX = start.x;
        int nextY = start.y;
        int deltaX = end.x - start.x;
        int deltaY = end.y - start.y;
        int stepX, stepY;
        int step = 0;
        int fraction;
        
        // パスの最後の位置を探し、そこから追加する
        for (int i = 0; i<MAX_PATH_LENGTH; i++) {
            if (path[i] == null) {
                step = i;
                break;
            }
        }

        if (deltaX < 0) stepX = -1; else stepX = 1;
        if (deltaY < 0) stepY = -1; else stepY = 1;
        
        deltaX = Math.abs(deltaX * 2);
        deltaY = Math.abs(deltaY * 2);
        
        path[step] = new Point(nextX, nextY);
        step++;
        
        if (deltaX > deltaY) {
            fraction = deltaY * 2 - deltaX;
            while (nextX != end.x) {
                if (fraction >= 0) {
                    nextY += stepY;
                    fraction -= deltaX;
                }
                nextX += stepX;
                fraction += deltaY;
                path[step] = new Point(nextX, nextY);
                step++;
            }
        } else {
            fraction = deltaX * 2 - deltaY;
            while (nextY != end.y) {
                if (fraction >= 0) {
                    nextX += stepX;
                    fraction -= deltaY;
                }
                nextY += stepY;
                fraction += deltaX;
                path[step] = new Point(nextX, nextY);
                step++;
            }
        }
    }
    
    /**
     * パスを表示する
     */
    public void showPath() {
        for (int i=0; i<MAX_PATH_LENGTH; i++) {
            if (path[i] != null) {
                System.out.println(path[i]);
            }
        }
    }
}
