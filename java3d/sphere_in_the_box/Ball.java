import javax.media.j3d.Appearance;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;

/*
 * Created on 2006/01/28
 */

/**
 * ボール
 * 
 * @author mori
 */
public class Ball extends TransformGroup {
    private Vector3d position; // 位置
    private Vector3d speed; // 速度
    private Transform3D positionT3D = new Transform3D(); // 移動用
    private float radius = 0.1f; // ボールの半径
    private Appearance app;

    public Ball(double x, double y, double z, double vx, double vy, double vz) {
        position = new Vector3d(x, y, z);
        speed = new Vector3d(vx, vy, vz);

        // 移動可能
        this.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        app = new Appearance();
        app.setTexture(loadTexture("carpet.jpg"));

        // ボールを追加
        this.addChild(new Sphere(radius, Sphere.GENERATE_TEXTURE_COORDS, 100, app));

        move();
    }

    /*
     * ボールを移動
     */
    public void move() {
        positionT3D.setTranslation(position);
        this.setTransform(positionT3D);
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d p) {
        position.x = p.x;
        position.y = p.y;
        position.z = p.z;
    }

    public Vector3d getSpeed() {
        return speed;
    }

    public void setSpeed(Vector3d s) {
        speed.x = s.x;
        speed.y = s.y;
        speed.z = s.z;
    }

    public float getRadius() {
        return radius;
    }

    /**
     * テクスチャをロード
     * 
     * @param filename ファイル名
     * @return テクスチャ
     */
    private Texture loadTexture(String filename) {
        Texture texture;

        texture = new TextureLoader(getClass().getResource(filename), null)
                .getTexture();

        return texture;
    }
}