import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Transform3D;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Vector3d;

/*
 * Created on 2006/01/28
 */

/**
 * ボールが跳ね回るビヘイビア
 * 
 * @author mori
 */
public class BallBehavior extends Behavior {
    private Ball ball; // ボール
    private Transform3D positionT3D;
    private WakeupOnElapsedTime trigger;

    public BallBehavior(Ball ball, long interval) {
        this.ball = ball;

        // interval間隔で呼び出すトリガを作成
        trigger = new WakeupOnElapsedTime(interval);

        // 範囲を設定（これがないと動かない）
        BoundingSphere bounds = new BoundingSphere();
        setSchedulingBounds(bounds);
    }

    /**
     * ビヘイビアの初期化
     */
    public void initialize() {
        wakeupOn(trigger);
    }

    /**
     * ビヘイビアの内容（ボールの移動）
     */
    public void processStimulus(Enumeration criteria) {
        Vector3d newPosition = new Vector3d();
        Vector3d newSpeed = new Vector3d();

        Vector3d oldPosition = ball.getPosition();
        Vector3d oldSpeed = ball.getSpeed();

        // 速度の更新（等速）
        newSpeed.x = oldSpeed.x;
        newSpeed.y = oldSpeed.y;
        newSpeed.z = oldSpeed.z;

        // 位置の更新
        newPosition.x = oldPosition.x + newSpeed.x;
        newPosition.y = oldPosition.y + newSpeed.y;
        newPosition.z = oldPosition.z + newSpeed.z;

        // 範囲を超えていたら速度を反転
        float radius = ball.getRadius();
        if (newPosition.x < -0.5 + radius || newPosition.x > 0.5 - radius) {
            newSpeed.x = -newSpeed.x;
        }
        if (newPosition.y < -0.5 + radius || newPosition.y > 0.5 - radius) {
            newSpeed.y = -newSpeed.y;
        }
        if (newPosition.z < -0.5 + radius || newPosition.z > 0.5 - radius) {
            newSpeed.z = -newSpeed.z;
        }

        // 新しい位置とスピードをセット
        ball.setPosition(newPosition);
        ball.setSpeed(newSpeed);

        // ボールを移動
        ball.move();

        // ビヘイビアに通知
        wakeupOn(trigger);
    }

}