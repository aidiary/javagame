import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/*
 * Created on 2006/01/01
 */

/**
 * 神々のトライフォース
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
     * @return BG
     */
    public BranchGroup createSceneGraph() {
        BranchGroup bg = new BranchGroup();

        // 回転用TG
        TransformGroup spinTG = new TransformGroup();
        spinTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // 回転運動
        Alpha rotationAlpha = new Alpha(-1, 4000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha,
                spinTG);

        // 範囲を指定
        BoundingSphere bounds = new BoundingSphere();
        rotator.setSchedulingBounds(bounds);

        spinTG.addChild(rotator);

        // トライフォース
        Triforce triforce = new Triforce();
        spinTG.addChild(triforce.getBG()); // spinTGに追加！

        bg.addChild(spinTG);

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
        frame.setTitle("神々のトライフォース");
    }
}