import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Random;

/*
 * Created on 2004/12/26
 *
 */

/**
 * @author mori
 *
 */
public class GoldToad extends Toad {
    private static final double PROB_MOVE = 0.5;
    // 乱数発生器（このクラスから作られる全蛙で共有）
    private static Random rand = new Random();
    
    /**
     * posの位置に蛙を作成する。
     * 緑蛙のエネルギーは1。
     * 
     * @param pos 蛙の座標。
     */
    public GoldToad(Point pos, MainPanel panel) {
        super(10, pos, panel);
    }
    
    /**
     * 青蛙は0.1の確率で移動。
     *  
     */
    public void move() {
        // PROB_MOVEの確率でランダムに移動する
        if (rand.nextDouble() < PROB_MOVE) {
            int dir = rand.nextInt(4);
            switch (dir) {
                case 0 : // 上へ移動
                    pos.y--;
                    break;
                case 1 : // 右へ移動
                    pos.x++;
                    break;
                case 2 : // 下へ移動
                    pos.y++;
                    break;
                case 3 : // 左へ移動
                    pos.x--;
                    break;
            }
        }

        // 画面内かチェックする
        if (pos.x < 0)
            pos.x = 0;
        if (pos.x > MainPanel.COL - 1)
            pos.x = MainPanel.COL - 1;
        if (pos.y < 0)
            pos.y = 0;
        if (pos.y > MainPanel.ROW - 1)
            pos.y = MainPanel.ROW - 1;
    }

    /**
     * 蛙の画像をロードする。
     * 
     * @param panel MainPanelへの参照。
     */
    protected void loadImage(MainPanel panel) {
        MediaTracker tracker = new MediaTracker(panel);
        toadImage = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("gold_toad.gif"));
        tracker.addImage(toadImage, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
