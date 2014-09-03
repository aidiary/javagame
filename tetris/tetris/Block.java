import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

/*
 * Created on 2005/08/17
 *
 */

/**
 * @author mori
 *
 */
public abstract class Block {
    // ブロックのサイズ
    public static final int MAX_Y = 4;
    public static final int MAX_X = 4;

    // マスのサイズ
    private static final int TILE_SIZE = Board.TILE_SIZE;

    // 移動方向
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;

    // ブロックの名前
    public static final int BAR = 0;
    public static final int Z_SHAPE = 1;
    public static final int SQUARE = 2;
    public static final int L_SHAPE = 3;
    public static final int REVERSE_Z_SHAPE = 4;
    public static final int T_SHAPE = 5;
    public static final int REVERSE_L_SHAPE = 6;
    public static final int WALL = 7;

    protected int[][] block;
    protected int imageNo;

    // 位置
    protected Point pos;

    // ボードへの参照
    protected Board board;

    public Block(Board board) {
        block = new int[MAX_Y][MAX_X];
        for (int y=0; y<MAX_Y; y++) {
            for (int x=0; x<MAX_X; x++) {
                block[y][x] = 0;
            }
        }

        imageNo = 6;
        pos = new Point(4, -4);
        
        this.board = board;
    }

    public void draw(Graphics g, Image blockImage) {
        for (int y=0; y<MAX_Y; y++) {
            for (int x=0; x<MAX_X; x++) {
                if (block[y][x] == 1) {
                    g.drawImage(blockImage, (pos.x+x) * TILE_SIZE, (pos.y+y) * TILE_SIZE, (pos.x+x)*TILE_SIZE+TILE_SIZE, (pos.y+y)*TILE_SIZE+TILE_SIZE,
                            imageNo * TILE_SIZE, 0, imageNo * TILE_SIZE + TILE_SIZE, TILE_SIZE, null);
                }
            }
        }
    }
    
    /**
     * dirの方向にブロックを移動
     * @param dir 方向
     * @return ブロックが固定されたらtrue
     */
    public boolean move(int dir) {
        switch (dir) {
            case LEFT:
                Point newPos = new Point(pos.x-1, pos.y);
                if (board.isMovable(newPos, block)) {
                    pos = newPos;
                }
                break;
            case RIGHT:
                newPos = new Point(pos.x+1, pos.y);
                if (board.isMovable(newPos, block)) {
                    pos = newPos;
                }
                break;
            case DOWN:
                newPos = new Point(pos.x, pos.y+1);
                if (board.isMovable(newPos, block)) {
                    pos = newPos;
                } else {
                    // ブロックをボードに固定する
                    board.fixBlock(pos, block, imageNo);
                    // 固定されたらtrueを返す
                    return true;
                }
                break;
        }
        
        return false;
    }
    
    /**
     * ブロックを回転させる
     */
    public void turn() {
        int[][] turnedBlock = new int[MAX_Y][MAX_X];

        // 回転したブロック
        for (int y=0; y<MAX_Y; y++) {
            for (int x=0; x<MAX_X; x++) {
                turnedBlock[x][MAX_Y-1-y] = block[y][x];
            }
        }
        
        // 回転可能か調べる
        if (board.isMovable(pos, turnedBlock)) {
            block = turnedBlock;
        }
    }
}
