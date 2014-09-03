import java.awt.Color;
import java.awt.Graphics;

/*
 * 大砲の弾
 * 
 * Created on 2006/06/02
 */

public class Shell {
    private static final double GRAVITY = 200;  // 重力（ピクセル/s^2）
    private static final int RADIUS = 10;  // 半径

    // 位置
    private double x;
    private double y;
    // 速度
    private double vx;
    private double vy;
    // 使用中か
    private boolean isUsed;
    
    public Shell() {
        x = y = -10;
        vx = vy = 0;
        isUsed = false;
    }

    /**
     * 発射
     * @param x 発射地点X座標
     * @param y 発射地点Y座標
     * @param speed 速さ（速度の大きさ）
     * @param direction 方向
     */
    public void fire(double x, double y, double speed, double direction) {
        this.x = x;
        this.y = y;
        // 速さと方向から速度を計算
        vx = speed * Math.cos(direction);
        vy = speed * Math.sin(direction);
        isUsed = true;
    }

    /**
     * 発射
     * @param x 発射地点X座標
     * @param y 発射地点Y座標
     * @param tx ターゲットのX座標
     * @param ty ターゲットのY座標
     * @param dt 何秒後にターゲットに当たるようにするか
     */
    public void fire(double x, double y, double tx, double ty, double dt) {
        this.x = x;
        this.y = y;
        // 目標地点と経過時間から初速を求める
        double dx = tx - x;
        double dy = ty - y;
        vx = dx / dt;
        vy = 1/dt * (dy - 1.0/2.0 * GRAVITY * dt * dt);
        isUsed = true;
    }

    /**
     * 弾を移動させる
     * @param dt 経過時間
     */
    public void move(double dt) {
        if (!isUsed) return;
        vy += GRAVITY * dt;  // 重力による速度変化
        x += vx * dt;  // 位置変化
        y += vy * dt;
        // 画面外に出たら使用中をfalseに
        if (x >= MainPanel.WIDTH || y >= MainPanel.HEIGHT) {
            isUsed = false;
        }
    }

    /**
     * 弾を画面に描画
     * @param g グラフィックオブジェクト
     */
    public void draw(Graphics g) {
        if (!isUsed) return;  // 使われてなければ描画しない
        g.setColor(Color.BLACK);
        // double型をint型に変換して描画
        g.fillOval((int) (x - RADIUS / 2), (int) (y - RADIUS / 2), RADIUS, RADIUS);
    }
    
    /**
     * この弾は使用中（発射中）か？
     * @return 使用中ならtrueを返す
     */
    public boolean isUsed() {
        return isUsed;
    }
}
