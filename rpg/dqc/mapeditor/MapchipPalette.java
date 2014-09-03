/*
 * マップチップパネル
 */
package mapeditor;

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

public class MapchipPalette extends JDialog {
    public static final int WIDTH = 480;
    public static final int HEIGHT = 256;
    
    // マップチップのサイズ（ピクセル単位）
    private static final int CS = 16;
    
    // マップチップ数
    private static final int NUM_CHIPS = 480;
    // 1行のマップチップ数
    private static final int NUM_CHIPS_IN_ROW = 30;
    
    // マップチップイメージ
    private Image mapchipImage = null;
    // マップチップごとに分割したイメージ
    private Image[] divImages = null;
    
    // 選択されているマップチップ番号
    private int selectedMapchipNo = 12;  // 暗闇
    
    public MapchipPalette(JFrame owner) {
        super(owner, "マップチップパレット", false);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBounds(680, 210, WIDTH, HEIGHT);
        setResizable(false);
        
        // ダイアログの上にパネルをのせる
        MapchipPanel mapchipPanel = new MapchipPanel();
        getContentPane().add(mapchipPanel);
        
        pack();
        
        loadImage();
    }
    
    /**
     * 選択されているマップチップ番号を返す
     * 
     * @return 選択されているマップチップ番号
     */
    public int getSelectedMapchipNo() {
        return selectedMapchipNo;
    }
    
    /**
     * マップチップ番号をセット
     * 
     * @param mapchipNo マップチップ番号
     */
    public void setSelectedMapchipNo(int mapchipNo) {
        selectedMapchipNo = mapchipNo;
    }
    
    /**
     * 分割したマップチップのイメージを返す
     * 
     * @return 分割したマップチップイメージ
     */
    public Image[] getMapchipImages() {
        return divImages;
    }

    /**
     * イメージをロード
     *
     */
    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("image/mapchip_16.png"));
        mapchipImage = icon.getImage();
        
        // マップチップごとにイメージを分割
        divImages = new Image[NUM_CHIPS];
        for (int i=0; i<NUM_CHIPS; i++) {
            // 描画先を確保
            divImages[i] = createImage(CS, CS);
            // 描画
            int x = i % NUM_CHIPS_IN_ROW;
            int y = i / NUM_CHIPS_IN_ROW;
            Graphics g = divImages[i].getGraphics();
            g.drawImage(mapchipImage, 0, 0, CS, CS,
                    x * CS, y * CS,
                    x * CS + CS, y * CS + CS, this);
            g.dispose();
        }
    }
    
    private class MapchipPanel extends JPanel {
        public MapchipPanel() {
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            
            // マウスでクリックしたときそのマップチップを選択する
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX() / CS;
                    int y = e.getY() / CS;
                    
                    int mapchipNo = y * NUM_CHIPS_IN_ROW + x;
                    if (mapchipNo > NUM_CHIPS) {
                        mapchipNo = 0;
                    }
                    
                    selectedMapchipNo = mapchipNo;
                    // 選択されているマップチップを枠で囲む
                    repaint();
                }
            });
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // マップチップを描画
            g.drawImage(mapchipImage, 0, 0, this);
            g.drawImage(divImages[10], 100, 100, this);
            // 選択されているマップチップを枠で囲む
            int x = selectedMapchipNo % NUM_CHIPS_IN_ROW;
            int y = selectedMapchipNo / NUM_CHIPS_IN_ROW;
            g.setColor(Color.YELLOW);
            g.drawRect(x * CS, y * CS, CS, CS);
        }
    }
}
