/*
 * 作成日: 2004/12/17
 *
 */
import java.awt.*;
import javax.swing.*;
/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel {
    // マスのサイズ
    private static final int GS = 32;
    // マスの数。オセロは8×8マス
    private static final int MASU = 8;
    // 盤面の大きさ＝メインパネルの大きさと同じ
    private static final int WIDTH = GS * MASU;
    private static final int HEIGHT = WIDTH;
    // 空白
    private static final int BLANK = 0;

    // 盤面
    private int[][] board = new int[MASU][MASU];

    public MainPanel() {
        // Othelloでpack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // 盤面を初期化する
        initBoard();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 盤面を描く
        drawBoard(g);
    }

    /**
     * 盤面を初期化する。
     *  
     */
    private void initBoard() {
        for (int y = 0; y < MASU; y++) {
            for (int x = 0; x < MASU; x++) {
                board[y][x] = BLANK;
            }
        }
    }

    /**
     * 盤面を描く。
     * 
     * @param g 描画オブジェクト。
     */
    private void drawBoard(Graphics g) {
        // マスを塗りつぶす
        g.setColor(new Color(0, 128, 128));
        g.fillRect(0, 0, WIDTH, HEIGHT);
//        for (int y = 0; y < MASU; y++) {
//            for (int x = 0; x < MASU; x++) {
//                // マス枠を描画する
//                g.setColor(Color.BLACK);
//                g.drawRect(x * GS, y * GS, GS, GS);
//            }
//        }
        // マス枠を描画
        g.setColor(Color.black);
        // 縦線
        for(int i = 1; i < MASU; i++) {
            g.drawLine(i*GS, 1, i*GS, HEIGHT);
        }
        // 横線
        for(int i = 1; i < MASU; i++) {
            g.drawLine(0, i*GS, WIDTH, i*GS);
        }
        // 外枠
        g.drawRect(0, 0, WIDTH, HEIGHT);
    }
}