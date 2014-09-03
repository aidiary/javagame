import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/*
 * Created on 2005/03/30
 *
 */

/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel
        implements MouseListener, MouseMotionListener {

    // パネルサイズ
    private static final int WIDTH = 480;
    private static final int HEIGHT = 480;
    // グリッドサイズ
    private static final int GS = 16;
    // 行数、列数
    public static final int ROW = HEIGHT / GS;
    public static final int COL = WIDTH / GS;
    // 直線の最大の長さ
    private static final int MAX_LINE_LENGTH = 256;

    // 直線の始点
    private Point start;

    // ブレゼンハムアルゴリズムで求めた直線の座標
    private Point[] line;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        line = new Point[MAX_LINE_LENGTH];

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ブレゼンハムアルゴリズムによって求められた直線の座標が
        // line[]に入ってるのでそこを塗りつぶす
        for (int i = 0; i < MAX_LINE_LENGTH; i++) {
            if (line[i] == null) {
                break;
            }
            g.fillRect(line[i].x * GS, line[i].y * GS, GS, GS);
        }
    }

    /**
     * ブレゼンハムアルゴリズムで直線を引く
     * 
     * @param start 直線の始点
     * @param end 直線の終点
     */
    private void buildLine(Point start, Point end) {
        int nextX = start.x;
        int nextY = start.y;
        int deltaX = end.x - start.x;
        int deltaY = end.y - start.y;
        int stepX, stepY;
        int step;
        int fraction;

        for (step = 0; step < MAX_LINE_LENGTH; step++) {
            line[step] = null;
        }

        step = 0;

        if (deltaX < 0)
            stepX = -1;
        else
            stepX = 1;
        if (deltaY < 0)
            stepY = -1;
        else
            stepY = 1;

        deltaX = Math.abs(deltaX * 2);
        deltaY = Math.abs(deltaY * 2);

        line[step] = new Point(nextX, nextY);
        step++;

        if (deltaX > deltaY) {
            fraction = deltaY - deltaX / 2;
            while (nextX != end.x) {
                if (fraction >= 0) {
                    nextY += stepY;
                    fraction -= deltaX;
                }
                nextX += stepX;
                fraction += deltaY;
                line[step] = new Point(nextX, nextY);
                step++;
            }
        } else {
            fraction = deltaX - deltaY / 2;
            while (nextY != end.y) {
                if (fraction >= 0) {
                    nextX += stepX;
                    fraction -= deltaY;
                }
                nextY += stepY;
                fraction += deltaX;
                line[step] = new Point(nextX, nextY);
                step++;
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        // 始点を設定
        start = new Point(e.getPoint().x / GS, e.getPoint().y / GS);
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        // 終点を設定
        Point end = new Point(e.getPoint().x / GS, e.getPoint().y / GS);
        // ブレゼンハムアルゴリズムで直線を求める
        buildLine(start, end);
        // 再描画
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }
}