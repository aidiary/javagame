import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import javax.swing.JPanel;

/*
 * Created on 2005/04/23
 *
 */

/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel {
    // パネルサイズ
    private static final int WIDTH = 240;
    private static final int HEIGHT = 240;

    // チップセットのサイズ（単位：ピクセル）
    private static final int CS = 16;

    // マップ
    private Map map;

    // A*クラス
    private Astar astar;

    // スタート地点とゴール地点を設定
    // 『ゲーム開発者のためのAI入門』のp.129と同じ
    private static Point START_POS = new Point(1, 1);
    private static Point GOAL_POS = new Point(13, 13);

    // A*で求めた最短経路のパス
    private LinkedList path;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);

        // マップ
        map = new Map("maze.dat");

        astar = new Astar(map);
        path = astar.searchPath(START_POS, GOAL_POS);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // マップを描画
        map.draw(g);

        // スタート地点とゴール地点を描画
        g.setColor(Color.RED);
        g.fillRect(START_POS.x * CS, START_POS.y * CS, CS, CS);

        g.setColor(Color.BLUE);
        g.fillRect(GOAL_POS.x * CS, GOAL_POS.y * CS, CS, CS);

        // 最短経路パスを描画
        if (path != null) {
            g.setColor(Color.YELLOW);
            for (int i = 0; i < path.size(); i++) {
                Node node = (Node) path.get(i);
                Point pos = node.pos;
                g.fillRect(pos.x * CS + 7, pos.y * CS + 7, CS - 14, CS - 14);
            }
        }
    }
}