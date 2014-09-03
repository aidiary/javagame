import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Transform3D;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/*
 * Created on 2006/02/04
 */

/**
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
        BoundingSphere bounds = new BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY);
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
        Vector3d newVelocity = new Vector3d();
        
        Vector3d oldPosition = ball.getPosition();
        Vector3d oldVelocity = ball.getVelocity();
        Vector3d acceleration = ball.getAcceleration();
        
        // 速度の更新
        newVelocity.x = oldVelocity.x + acceleration.x;
        newVelocity.y = oldVelocity.y + acceleration.y;
        newVelocity.z = oldVelocity.z + acceleration.z;

        // 位置の更新
        newPosition.x = oldPosition.x + newVelocity.x;
        newPosition.y = oldPosition.y + newVelocity.y;
        newPosition.z = oldPosition.z + newVelocity.z;

        // 範囲を超えていたら速度を反転
        float radius = ball.getRadius();
        if (newPosition.y < -2.0 + radius) {
            newPosition.y = -2.0 + radius;  // 床にめりこまないように
            newVelocity.y = (-newVelocity.y) * 0.8;  // 速度はだんだん落とす
        }

        // 新しい位置と速度をセット
        ball.setPosition(newPosition);
        ball.setVelocity(newVelocity);

        // ボールを移動
        ball.move();

        // ビヘイビアに通知
        wakeupOn(trigger);
    }

}