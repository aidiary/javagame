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

        // (x, y)に石が打てる場合だけ打つ
        if (canPutDown(x, y)) {
            // その場所に石を打つ
            putDownStone(x, y);
            // 音を鳴らす
            kachi.play();
        }
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

    /**
     * 石が打てるかどうか調べる。
     * 
     * @param x 石を打とうとしている場所のx座標。
     * @param y 石を打とうとしている場所のy座標。
     * @return 石が打てるならtrue、打てないならfalseを返す。
     *  
     */
    private boolean canPutDown(int x, int y) {
        // (x,y)が盤面の外だったら打てない
        if (x >= MASU || y >= MASU)
            return false;
        // (x,y)にすでに石があったら打てない
        if (board[y][x] != BLANK)
            return false;
        // 8方向のうち一箇所でもひっくり返せればこの場所に打てる
        // ひっくり返せるかどうかはもう1つのcanPutDownで調べる
        if (canPutDown(x, y, 1, 0))
            return true; // 右
        if (canPutDown(x, y, 0, 1))
            return true; // 下
        if (canPutDown(x, y, -1, 0))
            return true; // 左
        if (canPutDown(x, y, 0, -1))
            return true; // 上
        if (canPutDown(x, y, 1, 1))
            return true; // 右下
        if (canPutDown(x, y, -1, -1))
            return true; // 左上
        if (canPutDown(x, y, 1, -1))
            return true; // 右上
        if (canPutDown(x, y, -1, 1))
            return true; // 左下

        // どの方向もだめな場合はここには打てない
        return false;
    }

    /**
     * vecX、vecYの方向にひっくり返せる石があるか調べる。
     * 
     * @param x 石を打とうとしている場所のx座標。
     * @param y 石を打とうとしている場所のy座標。
     * @param vecX 調べる方向を示すx方向ベクトル。
     * @param vecY 調べる方向を示すy方向ベクトル。
     * @return 石が打てるならtrue、打てないならfalseを返す。
     *  
     */
    private boolean canPutDown(int x, int y, int vecX, int vecY) {
        int putStone;

        // 打つ石はどれか
        if (flagForWhite) {
            putStone = WHITE_STONE;
        } else {
            putStone = BLACK_STONE;
        }

        // 隣の場所へ。どの隣かは(vecX, vecY)が決める。
        x += vecX;
        y += vecY;
        // 盤面外だったら打てない
        if (x < 0 || x >= MASU || y < 0 || y >= MASU)
            return false;
        // 隣が自分の石の場合は打てない
        if (board[y][x] == putStone)
            return false;
        // 隣が空白の場合は打てない
        if (board[y][x] == BLANK)
            return false;

        // さらに隣を調べていく
        x += vecX;
        y += vecY;
        // となりに石がある間ループがまわる
        while (x >= 0 && x < MASU && y >= 0 && y < MASU) {
            // 空白が見つかったら打てない（１1つもはさめないから）
            if (board[y][x] == BLANK)
                return false;
            // 自分の石があればはさめるので打てる
            if (board[y][x] == putStone)
                return true;
            x += vecX;
            y += vecY;
        }
        // 相手の石しかない場合はいずれ盤面の外にでてしまうのでこのfalse
        return false;
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