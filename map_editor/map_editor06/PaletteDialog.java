import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Created on 2005/12/25
 *
 */

/**
 * @author mori
 *  
 */
public class PaletteDialog extends JDialog {
    // パネルのサイズ（単位：ピクセル）
    public static final int WIDTH = 256;
    public static final int HEIGHT = 256;

    // マップチップのサイズ（単位：ピクセル）
    private static final int CHIP_SIZE = 32;

    // マップチップの数
    private static final int NUM_CHIPS = 64;
    private static final int NUM_CHIPS_IN_ROW = 8;

    // マップチップのイメージ
    private Image mapChipImage;
    // マップチップをチップごとに分割したイメージ
    private Image[] mapChipImages;

    // 選択されているマップチップの番号
    private int selectedMapChipNo;

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
     * 選択されているマップチップ番号を返す
     * @return 選択されているマップチップ番号
     */
    public int getSelectedMapChipNo() {
        return selectedMapChipNo;
    }

    /**
     * 分割されたマップチップイメージを返す
     * @return 分割されたマップチップイメージ
     */
    public Image[] getMapChipImages() {
        return mapChipImages;
    }

    /**
     * マップチップイメージをロード
     */
    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource(
                "image/mapchip.gif"));
        mapChipImage = icon.getImage();

        // マップチップごとにイメージを分割
        mapChipImages = new Image[NUM_CHIPS];
        for (int i = 0; i < NUM_CHIPS; i++) {
            // 描画先を確保
            mapChipImages[i] = createImage(CHIP_SIZE, CHIP_SIZE);

            // 描画先に書き込む
            int x = i % NUM_CHIPS_IN_ROW;
            int y = i / NUM_CHIPS_IN_ROW;
            Graphics g = mapChipImages[i].getGraphics();
            g.drawImage(mapChipImage, 0, 0, CHIP_SIZE, CHIP_SIZE,
                    x * CHIP_SIZE, y * CHIP_SIZE,
                    x * CHIP_SIZE + CHIP_SIZE, y * CHIP_SIZE + CHIP_SIZE, null);
        }
    }

    // パレットパネル
    private class PalettePanel extends JPanel {
        public PalettePanel() {
            setPreferredSize(new Dimension(PaletteDialog.WIDTH, PaletteDialog.HEIGHT));

            // マウスでクリックしたときそのマップチップを選択する
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX() / CHIP_SIZE;
                    int y = e.getY() / CHIP_SIZE;
                    
                    // マップチップ番号は左上から0,1,2と数える
                    int mapChipNo = y * NUM_CHIPS_IN_ROW + x;
                    if (mapChipNo > NUM_CHIPS) {
                        mapChipNo = NUM_CHIPS;
                    }

                    selectedMapChipNo = mapChipNo;
                    
                    // 選択されているマップチップを枠で囲む
                    repaint();
                }
            });
        }

        public void paintComponent(Graphics g) {
            g.setColor(new Color(32, 0, 0));
            g.fillRect(0, 0, PaletteDialog.WIDTH, PaletteDialog.HEIGHT);

            // マップチップイメージを描画
            g.drawImage(mapChipImage, 0, 0, this);
            
            // 選択されているマップチップを枠で囲む
            int x = selectedMapChipNo % NUM_CHIPS_IN_ROW;
            int y = selectedMapChipNo / NUM_CHIPS_IN_ROW;
            g.setColor(Color.RED);
            g.drawRect(x * CHIP_SIZE, y * CHIP_SIZE, CHIP_SIZE, CHIP_SIZE);
        }
    }
}