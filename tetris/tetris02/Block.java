import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/*
 * Created on 2006/03/25
 */

public class Block {
    // ブロックのサイズ
    public static final int ROW = 4;
    public static final int COL = 4;

    // 1マスのサイズ
    private static final int TILE_SIZE = Field.TILE_SIZE;

    // ブロックの形を格納
    private int[][] block = new int[ROW][COL];

    // 位置（単位：マス）
    private Point pos;

    public Block() {
        init();

        // 四角いブロックを作成
        // □□□□
        // □■■□
        // □■■□
        // □□□□
        block[1][1] = 1;
        block[1][2] = 1;
        block[2][1] = 1;
        block[2][2] = 1;

        pos = new Point(4, 4);
    }

    /**
     * ブロックの初期化
     */
    public void init() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                block[i][j] = 0;
            }
        }
    }

    /**
     * ブロックの描画
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g) {
        g.setColor(Color.MAGENTA);
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (block[i][j] == 1) {
                    // posの位置を基準とする点に注意！
                    g.fillRect((pos.x + j) * TILE_SIZE,
                            (pos.y + i) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }
}
