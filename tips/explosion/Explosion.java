import java.awt.Graphics;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

/*
 * Created on 2006/01/29
 */

/**
 * @author mori
 */
public class Explosion {
    // 爆発イメージは全部で16枚
    private static final int NUM_IMAGES = 16;
    // 爆発イメージのサイズ
    private static final int SIZE = 96;

    // 爆発イメージ（全オブジェクトで共有）
    private static Image explosionImage;
    // アニメーション用のカウンタ
    private int counter;

    // 爆発の位置
    private int x;
    private int y;

    //  爆発アニメーションを表示中か
    private boolean used;

    private Timer timer = new Timer();
    //  爆発タスク
    private ExplosionTask task = null;

    public Explosion() {
        x = y = 0;
        counter = 0;
        used = false;

        // 爆発エフェクトのイメージを読み込む
        ImageIcon icon = new ImageIcon(getClass().getResource("explosion.gif"));
        explosionImage = icon.getImage();
    }

    public void draw(Graphics g) {
        if (task != null) {
            g.drawImage(explosionImage, x, y, x + SIZE, y + SIZE, counter
                    * SIZE, 0, counter * SIZE + SIZE, SIZE, null);
        }
    }

    public void play(int x, int y) {
        // クリックした位置が中央になるように
        this.x = x - SIZE / 2;
        this.y = y - SIZE / 2;

        counter = 0;

        used = true; // 使用中

        // 爆発アニメーション開始
        task = new ExplosionTask();
        timer.schedule(task, 0L, 80L);
    }

    /**
     * 爆発アニメーションを表示中か
     * 
     * @return 表示中ならtrueを返す
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * 爆発アニメーション用タスク
     */
    class ExplosionTask extends TimerTask {
        public void run() {
            counter++;
            if (counter == NUM_IMAGES - 1) { // 最後のイメージまでいったら
                task.cancel(); // タスクを中止
                task = null;
                used = false; // 使用中でない
                return; // アニメーションはループしない
            }
        }
    }
}