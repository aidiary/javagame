import java.awt.Color;
import java.awt.Graphics;

/*
 * Created on 2005/06/06
 *
 */

/**
 * @author mori
 *  
 */
public class Player {
    // 幅
    public static final int WIDTH = 32;
    // 高さ
    public static final int HEIGHT = 32;
    // スピード
    private static final int SPEED = 6;
    // ジャンプ力
    private static final int JUMP_SPEED = 24;
    // 重力
    private static final double GRAVITY = 1.0;

    // 位置
    private double x;
    private double y;

    // 速度
    private double vx;
    private double vy;

    // 着地しているか
    private boolean onGround;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        vx = 0;
        vy = 0;
        onGround = false;
    }

    /**
     * 停止する
     */
    public void stop() {
        vx = 0;
    }

    /**
     * 左に加速する
     */
    public void accelerateLeft() {
        vx = -SPEED;
    }

    /**
     * 右に加速する
     */
    public void accelerateRight() {
        vx = SPEED;
    }

    /**
     * ジャンプする
     */
    public void jump() {
        if (onGround) {
            // 上向きに速度を加える
            vy = -JUMP_SPEED;
            onGround = false;
        }
    }

    /**
     * プレイヤーの状態を更新する
     */
    public void update() {
        // 重力で下向きに加速度がかかる
        vy += GRAVITY;

        // 速度を元に位置を更新
        x += vx;
        y += vy;
        // 着地したか調べる
        if (y > MainPanel.HEIGHT - HEIGHT) {
            vy = 0;
            y = MainPanel.HEIGHT - HEIGHT;
            onGround = true;
        }
    }

    /**
     * プレイヤーを描画
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y, WIDTH, HEIGHT);
    }
}