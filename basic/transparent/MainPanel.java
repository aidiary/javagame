import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/*
 * Created on 2006/12/03
 */

public class MainPanel extends JPanel {
    private static final int WIDTH = 240;
    private static final int HEIGHT = 240;

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // アルファ値
        AlphaComposite composite = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.5f);

        g2.setColor(Color.RED);
        g2.fillOval(50, 50, 100, 100);

        // アルファ値をセット（以後の描画は半透明になる）
        g2.setComposite(composite);

        g2.setColor(Color.BLUE);
        g2.fillRect(90, 90, 100, 100);
    }
}
