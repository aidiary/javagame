import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Created on 2005/12/17
 *
 */

/**
 * @author mori
 *  
 */
public class PaletteDialog extends JDialog {
    // パネルのサイズ（単位：ピクセル）
    private static final int WIDTH = 256;
    private static final int HEIGHT = 256;

    // マップチップのサイズ（単位：ピクセル）
    private static final int CHIP_SIZE = 32;

    // マップチップのイメージ
    private Image mapChipImage;

    public PaletteDialog(JFrame owner) {
        // モードレスダイアログ
        super(owner, "マップチップパレット", false);
        // 位置とサイズ
        setBounds(600, 0, WIDTH, HEIGHT);
        setResizable(false);

        // ダイアログにパネルを追加
        PalettePanel palettePanel = new PalettePanel();
        getContentPane().add(palettePanel);
        pack();

        // マップチップイメージをロード
        loadImage();
    }

    /**
     * マップチップイメージをロード
     */
    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource(
                "image/mapchip.gif"));
        mapChipImage = icon.getImage();
    }

    // パレットパネル
    private class PalettePanel extends JPanel {
        private static final int NUM_CHIPS = 64;

        public PalettePanel() {
            setPreferredSize(new Dimension(PaletteDialog.WIDTH,
                    PaletteDialog.HEIGHT));
        }

        public void paintComponent(Graphics g) {
            g.setColor(new Color(32, 0, 0));
            g.fillRect(0, 0, PaletteDialog.WIDTH, PaletteDialog.HEIGHT);

            // マップチップイメージを描画
            g.drawImage(mapChipImage, 0, 0, this);
        }
    }
}