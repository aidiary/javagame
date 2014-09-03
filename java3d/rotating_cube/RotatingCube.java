import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class RotatingCube extends Applet {
    public RotatingCube() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        add(canvas, BorderLayout.CENTER);

        SimpleUniverse universe = new SimpleUniverse(canvas);

        // シーンを構築
        BranchGroup scene = createSceneGraph();
        scene.compile();
        
        // 視点をセット
        universe.getViewingPlatform().setNominalViewingTransform();
        
        universe.addBranchGraph(scene);
    }
    
    public BranchGroup createSceneGraph() {
        BranchGroup bg = new BranchGroup();

        // キューブを傾ける
        Transform3D rotate = new Transform3D();
        Transform3D tempRotate = new Transform3D();

        rotate.rotX(Math.PI / 4.0);
        tempRotate.rotY(Math.PI / 4.0);
        rotate.mul(tempRotate);

        TransformGroup rotateTG = new TransformGroup(rotate);

        // キューブを回転する
        TransformGroup spinTG = new TransformGroup();
        spinTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        // カラーキューブを作成してspinTGに追加
        ColorCube cube = new ColorCube(0.4);
        spinTG.addChild(cube);

        // 回転運動
        Alpha rotationAlpha = new Alpha(-1, 4000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, spinTG);
        // 範囲を指定
        BoundingSphere bounds = new BoundingSphere();
        rotator.setSchedulingBounds(bounds);
        spinTG.addChild(rotator);

        rotateTG.addChild(spinTG);
        bg.addChild(rotateTG);

        return bg;
    }
    
    public static void main(String[] args) {
        MainFrame frame = new MainFrame(new RotatingCube(), 256, 256);
    }
}
