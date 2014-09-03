import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/*
 * Created on 2005/04/24
 *
 */

/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel implements Runnable {
    // パネルサイズ
    private static final int WIDTH = 672;
    private static final int HEIGHT = 516;

    // チップセットのサイズ（単位：ピクセル）
    private static final int CS = 16;

    // 蟻の数
    private static final int MAX_ANTS = 200;

    // 蟻
    private Ant[] ants;

    // マップ
    private Map map;

    // ゲームループ用スレッド
    private Thread thread;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);

        // マップを作成
        map = new Map();

        // 蟻を作成
        ants = new Ant[MAX_ANTS];
        ants[0] = new Ant(Ant.RED_ANT, Ant.FORAGE, 5, 5, map, this);
        ants[1] = new Ant(Ant.RED_ANT, Ant.FORAGE, 5, 8, map, this);
        ants[2] = new Ant(Ant.BLACK_ANT, Ant.FORAGE, 36, 5, map, this);
        ants[3] = new Ant(Ant.BLACK_ANT, Ant.FORAGE, 36, 8, map, this);

        thread = new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(153, 255, 153));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // マップを描画
        map.draw(g);

        // 蟻を描画
        for (int i = 0; i < MAX_ANTS; i++) {
            if (ants[i] != null) {
                ants[i].draw(g);
            }
        }
    }

    /**
     * ゲームループ
     */
    public void run() {
        while (true) {
            // 各蟻の行動
            for (int i = 0; i < MAX_ANTS; i++) {
                if (ants[i] != null) {
                    ants[i].act();
                }
            }

            repaint();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void birthAnt(int type, int x, int y) {
        for (int i = 0; i < MAX_ANTS; i++) {
            // 最初のnullのところに1匹だけ追加する
            if (ants[i] == null) {
                ants[i] = new Ant(type, Ant.FORAGE, x, y, map, this);
                break;
            }
        }
    }
}