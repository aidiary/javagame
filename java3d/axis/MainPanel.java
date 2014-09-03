import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/*
 * Created on 2006/07/28
 */

public class MainPanel extends JPanel {
    // パネルサイズ
    private static final int WIDTH = 640;
    private static final int HEIGHT = 640;

    // 世界の範囲（光源などが届く範囲）
    private static final int BOUND_SIZE = 100;

    // 視点の初期位置
    private static final Point3d USER_POS = new Point3d(0, 5, 20);

    // 世界
    private SimpleUniverse universe;
    // BG（ここにいろんなものを接続する）
    private BranchGroup sceneBG;
    // 影響範囲
    private BoundingSphere bounds;

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());

        // 3Dモデルを描画するキャンパスを作成
        GraphicsConfiguration config = SimpleUniverse
                .getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add(canvas3D, BorderLayout.CENTER);
        canvas3D.setFocusable(true);
        canvas3D.requestFocus();

        // 世界を作成
        universe = new SimpleUniverse(canvas3D);

        createSceneGraph(); // 世界（シーングラフ）を構築
        initUserPosition(); // ユーザの視点を初期化
        orbitControls(canvas3D); // マウス操作

        universe.addBranchGraph(sceneBG);
    }

    /**
     * 世界を構築
     */
    private void createSceneGraph() {
        // sceneBGにいろいろ接続することで世界が構成される
        sceneBG = new BranchGroup();
        // 世界の範囲（光源などの及ぶ範囲）
        bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUND_SIZE);

        lightScene(); // 光源をsceneBGに追加
        addBackground(); // 空をsceneBGに追加

        // 座標軸を追加
        Axis axis = new Axis();
        sceneBG.addChild(axis.getBG());

        sceneBG.compile();
    }

    /**
     * 光源をsceneBGに追加
     */
    private void lightScene() {
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

        // 環境光（AmbientLight）
        // ぼやっとした光、これがないと真っ暗
        AmbientLight ambientLight = new AmbientLight(white);
        ambientLight.setInfluencingBounds(bounds); // 光源の及ぶ範囲を設定
        sceneBG.addChild(ambientLight); // sceneBGに光源を追加

        // 平行光源（表面がきらりと光る）
        Vector3f lightDirection = new Vector3f(-1.0f, -1.0f, -1.0f); // 光源の向き
        DirectionalLight dirLight = new DirectionalLight(white, lightDirection);
        dirLight.setInfluencingBounds(bounds);
        sceneBG.addChild(dirLight);
    }

    /**
     * 空をsceneBGに追加
     */
    private void addBackground() {
        Background back = new Background();
        back.setApplicationBounds(bounds);
        back.setColor(0.0f, 0.0f, 0.5f); // 青い空
        sceneBG.addChild(back);
    }

    /**
     * ユーザの視点を初期化
     */
    private void initUserPosition() {
        ViewingPlatform vp = universe.getViewingPlatform(); // SimpleUniverseのデフォルトを取得
        TransformGroup steerTG = vp.getViewPlatformTransform(); // vpのTGを取得

        Transform3D t3d = new Transform3D(); // 視点（vp）移動用のT3D
        steerTG.getTransform(t3d); // 現在の視点を取得

        // 新しい視点を設定
        // ユーザの位置、視線先の座標、方向を指定
        // 視点の先は原点
        t3d.lookAt(USER_POS, new Point3d(0, 0, 0), new Vector3d(0, 1, 0));
        t3d.invert();

        steerTG.setTransform(t3d); // 変更した視点を設定
    }

    /**
     * マウス操作
     * 
     * @param canvas
     *            キャンバス
     */
    private void orbitControls(Canvas3D canvas) {
        OrbitBehavior orbit = new OrbitBehavior(canvas,
                OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds); // 影響を及ぼす範囲を設定

        ViewingPlatform vp = universe.getViewingPlatform(); // 視点を取得
        vp.setViewPlatformBehavior(orbit); // 視点がマウスで制御可能になる
    }
}
