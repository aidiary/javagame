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
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/*
 * Created on 2006/01/04
 * 
 * /** 水晶球
 * 
 * @author mori
 */
public class CrystalBall extends Applet {
    private SimpleUniverse universe;

    public CrystalBall() {
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

        // 絨毯
        Appearance app = new Appearance();
        app.setTexture(loadTexture("carpet.jpg"));  // 絨毯のテクスチャ
        Box floor = new Box(0.8f, 0.01f, 0.8f, Box.GENERATE_TEXTURE_COORDS, app);

        // 水晶球
        app = new Appearance();
        Material mat = new Material();
        mat.setAmbientColor(new Color3f(0.0f, 0.0f, 1.0f));
        mat.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f));
        app.setMaterial(mat);

        TransparencyAttributes transAttr = new TransparencyAttributes(
                TransparencyAttributes.BLENDED, 0.5f);
        app.setTransparencyAttributes(transAttr);

        Sphere sphere = new Sphere(0.3f, Sphere.GENERATE_NORMALS, 100, app);

        // 水晶球移動用
        Transform3D t3d = new Transform3D();
        t3d.set(new Vector3f(0.0f, 0.3f, 0.0f));
        TransformGroup tg = new TransformGroup(t3d);
        tg.addChild(sphere);

        bg.addChild(floor);
        bg.addChild(tg);
        
        // 光源
        BoundingSphere bounds = new BoundingSphere();

        Light light = new AmbientLight();
        light.setInfluencingBounds(bounds);
        bg.addChild(light);

        light = new DirectionalLight(new Color3f(1.0f, 1.0f, 1.0f), new Vector3f(1.0f, -1.0f, -1.0f));
        light.setInfluencingBounds(bounds);
        bg.addChild(light);

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

    /**
     * テクスチャをロード
     * @param filename ファイル名
     * @return テクスチャ
     */
    private Texture loadTexture(String filename) {
        Texture texture;
        
        texture = new TextureLoader(getClass().getResource(filename), null).getTexture();

        return texture;
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame(new CrystalBall(), 256, 256);
        frame.setTitle("水晶球");
    }
}