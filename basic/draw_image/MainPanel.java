import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

import javax.swing.JPanel;

/*
 * Created on 2006/02/25
 */

public class MainPanel extends JPanel {
    // パネルサイズ
    private static final int WIDTH = 369;
    private static final int HEIGHT = 254;
    // イメージ
    private Image image;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // イメージを読み込む
        image = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("image.gif"));

        // MediaTrackerに登録
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(image, 0);
        // イメージ読み込み完了まで待機
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(image, 0, 0, this);
    }
}
