import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/*
 * Created on 2005/12/17
 *
 */

/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel
        implements
            MouseListener,
            MouseMotionListener {

    // パネルのサイズ（単位：ピクセル）
    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;

    // チップセットのサイズ（単位：ピクセル）
    public static final int CHIP_SIZE = 32;

    // マップ
    private int[][] map;
    // マップの大きさ（単位：マス）
    private int row;
    private int col;
    
    // マップチップパレット
    private PaletteDialog paletteDialog;
    private Image[] mapChipImages;

    public MainPanel(PaletteDialog paletteDialog) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        addMouseListener(this);
        addMouseMotionListener(this);
        
        // パレットダイアログ
        this.paletteDialog = paletteDialog;
        mapChipImages = paletteDialog.getMapChipImages();

        // マップを初期化
        initMap(16, 16);
    }

    /**
     * マップを初期化する
     * @param row 行数
     * @param col 列数
     */
    public void initMap(int r, int c) {
        row = r;
        col = c;
        map = new int[r][c];

        // パレットで選択されているマップチップの番号を取得
        int mapChipNo = paletteDialog.getSelectedMapChipNo();

        // そのマップチップでマップを埋めつくす
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                map[i][j] = mapChipNo;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // map[][]に保存されているマップチップ番号をもとに画像を描画する
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                g.drawImage(mapChipImages[map[i][j]], j * CHIP_SIZE, i * CHIP_SIZE, null);
            }
        }
    }

    /**
     * マウスでマップ上をクリックしたとき
     */
    public void mouseClicked(MouseEvent e) {
        // マウスポインタの座標から座標（マス）を求める
        int x = e.getX() / CHIP_SIZE;
        int y = e.getY() / CHIP_SIZE;

        // パレットから取得した番号をセット
        if (x >= 0 && x < col && y >= 0 && y < row) {
            map[y][x] = paletteDialog.getSelectedMapChipNo();
        }

        repaint();
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    /**
     * マップ上でマウスをドラッグしたとき
     */
    public void mouseDragged(MouseEvent e) {
        // マウスポインタの座標から座標（マス）を求める
        int x = e.getX() / CHIP_SIZE;
        int y = e.getY() / CHIP_SIZE;

        // パレットから取得した番号をセット
        if (x >= 0 && x < col && y >= 0 && y < row) {
            map[y][x] = paletteDialog.getSelectedMapChipNo();
        }

        repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }
}