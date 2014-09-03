import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/*
 * Created on 2006/02/04
 */

/**
 * 
 * @author mori
 */
public class Main extends Applet {
    private SimpleUniverse universe;

    public Main() {
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
        Transform3D viewPlatformTransform = new Transform3D();
        viewPlatformTransform.setTranslation(new Vector3d(0.0, 0.0, 10.0));
        universe.getViewingPlatform().getViewPlatformTransform().setTransform(viewPlatformTransform);

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

        // ここでシーンを作成する

        // ボールを追加
        Ball ball = new Ball(0, 20.0, 0);
        BallBehavior ballBehavior = new BallBehavior(ball, 20);
        bg.addChild(ball);
        bg.addChild(ballBehavior);

        // 箱を追加
        bg.addChild(createFloor());

        // 光源を追加
        bg.addChild(createAmbientLight());
        bg.addChild(createDirectionalLight());
        
        return bg;
    }

    private BranchGroup createFloor() {
        BranchGroup bg = new BranchGroup();
        TransformGroup tg;
        Transform3D t3d = new Transform3D();
        
        Appearance app = new Appearance();
        app.setTexture(loadTexture("carpet.jpg"));

        Box floor = new Box(3.0f, 0.01f, 3.0f, Box.GENERATE_TEXTURE_COORDS, app);
        t3d.setTranslation(new Vector3d(0.0f, -2.0, 0.0f));
        tg = new TransformGroup(t3d);
        tg.addChild(floor);
        bg.addChild(tg);
        
        return bg;
    }

    public Light createAmbientLight() {
        BoundingSphere bounds = new BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY);
        Light light = new AmbientLight();
        light.setInfluencingBounds(bounds);
        return light;
    }

    public Light createDirectionalLight() {
        BoundingSphere bounds = new BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY);
        Light light = new DirectionalLight(new Color3f(1.0f, 1.0f, 1.0f),
                new Vector3f(1.0f, -1.0f, -1.0f));
        light.setInfluencingBounds(bounds);
        return light;
    }

    /**
     * マウス操作を可能にする
     * 
     * @param canvas キャンバス
     */
    private void orbitControls(Canvas3D canvas) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY);

        OrbitBehavior orbit = new OrbitBehavior(canvas,
                OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds);

        ViewingPlatform vp = universe.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
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
    
    public static void main(String[] args) {
        MainFrame frame = new MainFrame(new Main(), 256, 256);
        frame.setTitle("ボールの自由落下");
    }
}