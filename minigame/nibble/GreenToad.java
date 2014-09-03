/*
 * Created on 2004/12/26
 *
 */
import java.awt.*;
/**
 * @author mori
 *
 */
public class GreenToad extends Toad {
    /**
     * posの位置に蛙を作成する。
     * 緑蛙のエネルギーは1。
     * 
     * @param pos 蛙の座標。
     */
    public GreenToad(Point pos, MainPanel panel) {
        super(1, pos, panel);
    }
    
    /**
     * 緑蛙は移動しない。
     *  
     */
    public void move() {
    }

    /**
     * 蛙の画像をロードする。
     * 
     * @param panel MainPanelへの参照。
     */
    protected void loadImage(MainPanel panel) {
        MediaTracker tracker = new MediaTracker(panel);
        toadImage = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("green_toad.gif"));
        tracker.addImage(toadImage, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
