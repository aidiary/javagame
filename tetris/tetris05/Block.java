import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/*
 * Created on 2006/04/23
 */

public class Block {
    // ブロックのサイズ
    public static final int ROW = 4;
    public static final int COL = 4;

    // 1マスのサイズ
    private static final int TILE_SIZE = Field.TILE_SIZE;

    // 移動方向
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;

    // ブロックの形を格納
    private int[][] block = new int[ROW][COL];

    // 位置（単位：マス）
    private Point pos;

    // フィールドへの参照
    private Field field;

    public Block(Field field) {
        this.field = field;

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

        pos = new Point(4, -4);
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

    /**
     * dirの方向にブロックを移動
     * 
     * @param dir 方向
     */
    public void move(int dir) {
        switch (dir) {
            case LEFT :
                Point newPos = new Point(pos.x - 1, pos.y);
                if (field.isMovable(newPos, block)) {  // 衝突しなければ位置を更新
                    pos = newPos;
                }
                break;
            case RIGHT :
                newPos = new Point(pos.x + 1, pos.y);
                if (field.isMovable(newPos, block)) {
                    pos = newPos;
                }
                break;
            case DOWN :
                newPos = new Point(pos.x, pos.y + 1);
                if (field.isMovable(newPos, block)) {
                    pos = newPos;
                }
                break;
        }
    }

    /**
     * ブロックを回転される
     */
    public void turn() {
        int[][] turnedBlock = new int[ROW][COL];

        // 回転したブロック
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                turnedBlock[j][ROW - 1 - i] = block[i][j];
            }
        }

        // 回転可能か調べる
        if (field.isMovable(pos, turnedBlock)) {
            block = turnedBlock;
        }
    }

    /**
     * バーブロックに変更。回転効果がわかるようにするための一時メソッド。
     */
    public void createBarBlock() {
        init();

        // □■□□
        // □■□□
        // □■□□
        // □■□□
        block[0][1] = 1;
        block[1][1] = 1;
        block[2][1] = 1;
        block[3][1] = 1;
    }
}
