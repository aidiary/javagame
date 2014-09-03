import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/*
 * Created on 2006/12/03
 */

public class MainPanel extends JPanel implements MouseMotionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // スポットライトの半径
    private static final int RADIUS = 128;

    // 背景イメージ
    private Image bgImage;
    // お化けイメージ
    private Image obakeImage;

    // スポットライト
    private Spotlight spotlight;

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        addMouseMotionListener(this);

        ImageIcon icon = new ImageIcon(getClass().getResource("castle.jpg"));
        bgImage = icon.getImage();

        icon = new ImageIcon(getClass().getResource("obake.gif"));
        obakeImage = icon.getImage();

        // 画面の中央にスポットライトをセット
        spotlight = new Spotlight(WIDTH / 2, HEIGHT / 2, RADIUS);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 背景を描画
        g.drawImage(bgImage, 0, 0, this);

        // お化けを描画
        g.drawImage(obakeImage, 20, 400, this);

        // スポットライト
        Rectangle2D screen = new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
        // 画面全体を覆うマスク
        Area mask = new Area(screen);
        // マスクからスポットライト部分を取り去る
        mask.subtract(new Area(spotlight.getSpot()));

        // マスクを背景の上に描画
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fill(mask);
    }

    public void mouseDragged(MouseEvent e) {
        spotlight.setSpot(e.getX(), e.getY(), RADIUS);
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }
}
