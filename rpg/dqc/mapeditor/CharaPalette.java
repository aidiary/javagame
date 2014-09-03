package mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CharaPalette extends JDialog {
    public static final int WIDTH = 256;
    public static final int HEIGHT = 128;
    
    // キャラクターのサイズ（ピクセル単位）
    private static final int CS = 16;
    
    // キャラクター数
    private static final int NUM_CHARAS = 128;
    // 1行のキャラクター数
    private static final int NUM_CHARAS_IN_ROW = 16;
    
    // キャラクターイメージ
    private Image charaImage = null;
    // キャラクターごとに分割したイメージ
    // divImages[charaNo][direction]
    private Image[][] divImages = null;
    
    // 選択されているキャラクター番号
    private int selectedCharaNo;
    
    public CharaPalette(JFrame owner) {
        super(owner, "キャラクターパレット", false);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBounds(680, 500, WIDTH, HEIGHT);
        setResizable(false);
        
        // ダイアログの上にパネルをのせる
        CharaPanel charaPanel = new CharaPanel();
        getContentPane().add(charaPanel);
        
        pack();
        
        loadImage();
    }
    
    /**
     * 選択されているキャラクター番号
     * 
     * @return 選択されているキャラクター番号
     */
    public int getSelectedCharaNo() {
        return selectedCharaNo;
    }
    
    /**
     * 分割したキャラクターイメージを返す
     * 
     * @return 分割したキャラクターイメージ
     */
    public Image[][] getCharaImages() {
        return divImages;
    }
    
    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("image/character_16.png"));
        charaImage = icon.getImage();
        
        // キャラクターごとにイメージを分割
        divImages = new Image[NUM_CHARAS][4];
        for (int i=0; i<NUM_CHARAS; i++) {
            for (int j=0; j<4; j++) {
                // 描画先を確保
                divImages[i][j] = createImage(CS, CS);
                // 描画
                int cx = (i % NUM_CHARAS_IN_ROW) * (CS * 2);
                int cy = (i / NUM_CHARAS_IN_ROW) * (CS * 4);
                Graphics g = divImages[i][j].getGraphics();
                g.drawImage(charaImage, 0, 0, CS, CS,
                        cx, cy + j * CS,
                        cx + CS, cy + j * CS + CS,
                        null);
                g.dispose();
            }
        }
    }
    
    public class CharaPanel extends JPanel implements MouseListener {       
        public CharaPanel() {
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            
            // マウスでクリックしたときそのキャラクター選択する
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX() / CS;
                    int y = e.getY() / CS;
                    
                    int charaNo = y * NUM_CHARAS_IN_ROW + x;
                    if (charaNo > NUM_CHARAS) {
                        charaNo = NUM_CHARAS;
                    }
                    
                    selectedCharaNo = charaNo;
                    // 選択されているキャラクターを枠で囲む
                    repaint();
                }
            });
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            for (int i=0; i<NUM_CHARAS; i++) {
                int c = (i % NUM_CHARAS_IN_ROW);
                int r = (i / NUM_CHARAS_IN_ROW);
                // i番目のキャラクターの座標
                int cx = c * (CS * 2);
                int cy = r * (CS * 4);
                g.drawImage(charaImage, c*CS, r*CS, c*CS+CS, r*CS+CS,
                        cx, cy,
                        cx+CS, cy+CS,
                        null);
            }
            
            // 選択されているキャラクターを枠で囲む
            int x = selectedCharaNo % NUM_CHARAS_IN_ROW;
            int y = selectedCharaNo / NUM_CHARAS_IN_ROW;
            g.setColor(Color.YELLOW);
            g.drawRect(x*CS, y*CS, CS, CS);
        }

        public void mouseClicked(MouseEvent e) {
            int x = e.getX() / CS;
            int y = e.getY() / CS;
            
            int charaNo = y * NUM_CHARAS_IN_ROW + x;
            if (charaNo > NUM_CHARAS) {
                charaNo = NUM_CHARAS;
            }
            
            selectedCharaNo = charaNo;
            
            repaint();
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }
}
