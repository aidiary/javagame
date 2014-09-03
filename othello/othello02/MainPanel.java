/*
 * 作成日: 2004/12/17
 *
 */
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel implements MouseListener {
    // マスのサイズ（GRID SIZE）
    private static final int GS = 32;
    // マスの数。オセロは8×8マス
    private static final int MASU = 8;
    // 盤面の大きさ＝メインパネルの大きさと同じ
    private static final int WIDTH = GS * MASU;
    private static final int HEIGHT = WIDTH;
    // 空白
    private static final int BLANK = 0;
    // 黒石
    private static final int BLACK_STONE = 1;
    // 白石
    private static final int WHITE_STONE = -1;

    // 盤面
    private int[][] board = new int[MASU][MASU];
    // 白の番か
    private boolean flagForWhite;
    // 石を打つ音
    private AudioClip kachi;

    public MainPanel() {
        // Othelloでpack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // 盤面を初期化する
        initBoard();
        // サウンドをロードする
        kachi = Applet.newAudioClip(getClass().getResource("kachi.wav"));
        // マウス操作を受け付けるようにする
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 盤面を描く
        drawBoard(g);
        // 石を描く
        drawStone(g);
    }

    /**
     * マウスをクリックしたとき石を打つ
     */
    public void mouseClicked(MouseEvent e) {
        // どこのマスかを調べる
        int x = e.getX() / GS;
        int y = e.getY() / GS;
        // その場所に石を打つ
        putDownStone(x, y);
        // 音を鳴らす
        kachi.play();
        // 盤面が変化したので再描画する
        repaint();
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
        board[3][3] = board[4][4] = WHITE_STONE;
        board[3][4] = board[4][3] = BLACK_STONE;

        flagForWhite = false;
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
        for (int y = 0; y < MASU; y++) {
            for (int x = 0; x < MASU; x++) {
                // マス枠を描画する
                g.setColor(Color.BLACK);
                g.drawRect(x * GS, y * GS, GS, GS);
            }
        }
    }

    /**
     * 石を描く。
     * 
     * @param g 描画オブジェクト
     */
    private void drawStone(Graphics g) {
        for (int y = 0; y < MASU; y++) {
            for (int x = 0; x < MASU; x++) {
                if (board[y][x] == BLANK) {
                    continue;
                } else if (board[y][x] == BLACK_STONE) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillOval(x * GS + 3, y * GS + 3, GS - 6, GS - 6);
            }
        }
    }

    /**
     * 盤面に石を打つ
     * 
     * @param x 石を打つ場所のx座標
     * @param y 石を打つ場所のy座標
     */
    private void putDownStone(int x, int y) {
        int stone;

        // どっちの手番か調べて石の色を決める
        if (flagForWhite) {
            stone = WHITE_STONE;
        } else {
            stone = BLACK_STONE;
        }
        // 石を打つ
        board[y][x] = stone;
        // 手番を変える
        flagForWhite = !flagForWhite;
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}