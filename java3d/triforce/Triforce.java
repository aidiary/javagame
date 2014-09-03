import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/*
 * Created on 2006/01/01
 */

/**
 * トライフォースを構築する
 * 
 * @author mori
 */
public class Triforce {
    private BranchGroup triforceBG;

    public Triforce() {
        triforceBG = new BranchGroup();

        // Geometryの生成
        TriangleArray t1 = new TriangleArray(3, TriangleArray.COORDINATES
                | TriangleArray.COLOR_3);
        TriangleArray t2 = new TriangleArray(3, TriangleArray.COORDINATES
                | TriangleArray.COLOR_3);
        TriangleArray t3 = new TriangleArray(3, TriangleArray.COORDINATES
                | TriangleArray.COLOR_3);

        // 頂点のセット
        Color3f yellow = new Color3f(1.0f, 1.0f, 0.0f); // トライフォースの色

        // 上の三角形
        t1.setCoordinate(0, new Point3f(0.0f, 0.6f, 0.0f));
        t1.setCoordinate(1, new Point3f(-0.3f, 0.0f, 0.0f));
        t1.setCoordinate(2, new Point3f(0.3f, 0.0f, 0.0f));
        t1.setColor(0, yellow);
        t1.setColor(1, yellow);
        t1.setColor(2, yellow);

        // 左下の三角形
        t2.setCoordinate(0, new Point3f(-0.3f, 0.0f, 0.0f));
        t2.setCoordinate(1, new Point3f(-0.6f, -0.6f, 0.0f));
        t2.setCoordinate(2, new Point3f(0.0f, -0.6f, 0.0f));
        t2.setColor(0, yellow);
        t2.setColor(1, yellow);
        t2.setColor(2, yellow);

        // 右下の三角形
        t3.setCoordinate(0, new Point3f(0.3f, 0.0f, 0.0f));
        t3.setCoordinate(1, new Point3f(0.0f, -0.6f, 0.0f));
        t3.setCoordinate(2, new Point3f(0.6f, -0.6f, 0.0f));
        t3.setColor(0, yellow);
        t3.setColor(1, yellow);
        t3.setColor(2, yellow);

        // Appearanceの設定
        Appearance app = new Appearance();

        PolygonAttributes pAttr = new PolygonAttributes();
        pAttr.setCullFace(PolygonAttributes.CULL_NONE); // 裏面も描画

        app.setPolygonAttributes(pAttr);

        // BGに追加
        triforceBG.addChild(new Shape3D(t1, app));
        triforceBG.addChild(new Shape3D(t2, app));
        triforceBG.addChild(new Shape3D(t3, app));
    }

    /**
     * 構築したBGを返す
     * 
     * @return BG
     */
    public BranchGroup getBG() {
        return triforceBG;
    }
}