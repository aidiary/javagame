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
public class Teleport {
    // 六芒星のイメージは全部で7枚
    private static final int NUM_IMAGES = 8;
    // イメージのサイズ
    private static final int SIZE = 32;

    // イメージ（全オブジェクトで共有）
    private static Image hexagramImage;
    // アニメーション用カウンタ
    private int counter;

    // 位置
    private int x, y;

    // テレポート中か
    private boolean isInTeleport;

    private Timer timer = new Timer();
    // アニメーション用タスク
    private TeleportTask task = null;

    public Teleport() {
        x = y = 0;
        counter = 0;
        isInTeleport = false;

        // 六芒星のイメージを読み込む
        ImageIcon icon = new ImageIcon(getClass().getResource("hexagram.gif"));
        hexagramImage = icon.getImage();
    }

    public void draw(Graphics g) {
        if (task != null) {
            
            g.drawImage(hexagramImage, x, y, x + SIZE, y + SIZE, counter
                    * SIZE, 0, counter * SIZE + SIZE, SIZE, null);
        }
    }
    
    public void  play(Wizard wizard, int targetX, int targetY) {
        this.x = wizard.getX() - SIZE / 2;
        this.y = wizard.getY() - SIZE / 2;
        
        counter = 0;
        
        isInTeleport = true;  // 使用中
        
        // アニメーション開始
        task = new TeleportTask(wizard, targetX, targetY);
        timer.schedule(task, 0L, 80L);
    }
    
    public boolean isInTeleport() {
        return isInTeleport;
    }
    
    /**
     * アニメーション用タスク
     */
    class TeleportTask extends TimerTask {
        private Wizard wizard;
        private int toX, toY;
        
        public TeleportTask(Wizard wizard, int toX, int toY) {
            this.wizard = wizard;
            this.toX = toX;
            this.toY = toY;
        }

        public void run() {
            while (true) {
                counter++;
                if (counter == NUM_IMAGES - 1) { // 最後のイメージまでいったら
                    wizard.setX(toX);
                    wizard.setY(toY);
                    
                    task.cancel(); // タスクを中止
                    task = null;
                    isInTeleport = false; // テレポート終了
                    return; // アニメーションはループしない
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}