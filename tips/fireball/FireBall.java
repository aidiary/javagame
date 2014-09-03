import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

/*
 * Created on 2006/01/29
 */

/**
 * @author mori
 */
public class FireBall {
    // 位置
    private double x, y;
    // 速度（向きと大きさ）
    private double vx, vy;
    private double speed;

    // イメージ
    private static Image fireballImage;

    // 使用中か
    private boolean used;

    // MainPanelへの参照
    private MainPanel panel;

    public FireBall() {
        x = y = -10;
        vx = vy = 0;
        speed = 10;

        used = false;
        
        // ファイアボールのイメージを読み込む
        ImageIcon icon = new ImageIcon(getClass().getResource("fireball.gif"));
        fireballImage = icon.getImage();
    }

    /**
     * ファイアボールを放つ
     * 
     * @param start 始点
     * @param target 終点
     */
    public void shot(Point start, Point target) {
        x = start.x;
        y = start.y;
        // 始点と終点から角度を計算する
        double direction = Math.atan2(target.y - start.y, target.x - start.x);
        vx = Math.cos(direction) * speed;
        vy = Math.sin(direction) * speed;
        // System.out.println("vx: " + vx + " vy: " + vy);
        used = true;
    }

    /**
     * 移動
     */
    public void move() {
        x += vx;
        y += vy;

        // 画面外に出たら
        if (x < 0 || x > MainPanel.WIDTH || y < 0 || y > MainPanel.HEIGHT) {
            used = false;
        }
    }

    /**
     * 描画
     * 
     * @param g
     */
    public void draw(Graphics g) {
        g.drawImage(fireballImage, (int)x, (int)y, null);
    }

    /**
     * ファイアボールを使用中か
     * 
     * @return 表示中ならtrueを返す
     */
    public boolean isUsed() {
        return used;
    }
}