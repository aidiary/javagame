import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

/*
 * Created on 2005/08/07
 *
 */

/**
 * @author mori
 *
 */
public class Explosion extends Thread {
    // 爆発イメージのサイズ
    private static final int SIZE = 20;

    // 爆発イメージ
    private Image explosionImage;
    // アニメーション用のカウンタ
    private int count;

    // 爆発の位置
    private int x;
    private int y;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;

        count = 0;

        // 爆発エフェクトのイメージを読み込む
        ImageIcon icon = new ImageIcon(getClass()
                .getResource("image/explosion.gif"));
        explosionImage = icon.getImage();
        
        start();
    }

    public void draw(Graphics g) {
        g.drawImage(explosionImage, x, y, x + SIZE, y + SIZE, count * SIZE, 0, count * SIZE + SIZE, SIZE, null);
    }

    public void run() {
        while (true) {
            count++;
            if (count == 15) {
                return;
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
