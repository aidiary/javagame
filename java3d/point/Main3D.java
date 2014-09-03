import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/*
 * Created on 2006/01/01
 */

/**
 * 点を打つ
 * 
 * @author mori
 */
public class Main3D extends Applet {
    private SimpleUniverse universe;

    public Main3D() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse
                .getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        add(canvas, BorderLayout.CENTER);

        universe = new SimpleUniverse(canvas);

        // シーンを構築
        BranchGroup scene = createSceneGraph();
        scene.compile();

        // 視点をセット
        universe.getViewingPlatform().setNominalViewingTransform();

        // マウス操作
        orbitControls(canvas);

        universe.addBranchGraph(scene);
    }

    /**
     * シーンを構築する
     * 
     * @return BG
     */
    public BranchGroup createSceneGraph() {
        BranchGroup bg = new BranchGroup();

        // ここに3Dオブジェクトを追加

        // 座標軸を追加
        Axis axis = new Axis();
        bg.addChild(axis.getBG());

        // 点を追加
        Point3f[] vertices = { // 点の座標
                new Point3f(-0.5f, 0.5f, -0.5f),
                new Point3f(0.5f, 0.5f, -0.5f),
                new Point3f(0.5f, 0.5f, 0.5f),
                new Point3f(-0.5f, 0.5f, 0.5f),
                new Point3f(-0.5f, -0.5f, -0.5f),
                new Point3f(0.5f, -0.5f, -0.5f),
                new Point3f(0.5f, -0.5f, 0.5f),
                new Point3f(-0.5f, -0.5f, 0.5f)
        };

        Color3f[] colors = { // 点の色
                new Color3f(1.0f, 1.0f, 0.0f),
                new Color3f(1.0f, 1.0f, 0.0f),
                new Color3f(1.0f, 1.0f, 0.0f),
                new Color3f(1.0f, 1.0f, 0.0f),
                new Color3f(1.0f, 1.0f, 0.0f),
                new Color3f(1.0f, 1.0f, 0.0f),
                new Color3f(1.0f, 1.0f, 0.0f),
                new Color3f(1.0f, 1.0f, 0.0f)
        };

        // Geometry
        PointArray geo = new PointArray(8, PointArray.COORDINATES
                | PointArray.COLOR_3);
        geo.setCoordinates(0, vertices); // 頂点座標をセット
        geo.setColors(0, colors); // 色をセット

        // Appearance
        Appearance app = new Appearance();
        PointAttributes attr = new PointAttributes(5.0f, false); // 点の大きさ
        app.setPointAttributes(attr);

        bg.addChild(new Shape3D(geo, app));

        // ここまで

        return bg;
    }

    /**
     * マウス操作を可能にする
     * 
     * @param canvas キャンバス
     */
    private void orbitControls(Canvas3D canvas) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 100);

        OrbitBehavior orbit = new OrbitBehavior(canvas,
                OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds);

        ViewingPlatform vp = universe.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame(new Main3D(), 256, 256);
        frame.setTitle("点を打つ");
    }
}