import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/*
 * Created on 2005/10/09
 *
 */

/**
 * @author mori
 *  
 */
class MainPanel extends JPanel {
    // パネルサイズ
    private static final int WIDTH = 480;
    private static final int HEIGHT = 480;

    // 勇者のイメージ
    private Image heroImage;

    public MainPanel() {
        // パネルの推奨サイズを設定
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // イメージをロード
        loadImage();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 勇者のイメージを表示
        g.drawImage(heroImage, 0, 0, this);
    }

    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("image/hero.gif"));
        heroImage = icon.getImage();
    }
}