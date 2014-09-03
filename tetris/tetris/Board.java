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
public class Board {
    // フィールドのサイズ
    public static final int MAX_X = 12;
    public static final int MAX_Y = 26;

    // マスのサイズ
    public static final int TILE_SIZE = 16;
    
    // ボード
    private int[][] board;
    // ボードのイメージ
    private int[][] boardImage;

    public Board() {
        board = new int[MAX_Y][MAX_X];
        boardImage = new int[MAX_Y][MAX_X];
        init();
    }

    /**
     * フィールドを初期化する
     */
    public void init() {
        for (int y=0; y<MAX_Y; y++) {
            for (int x=0; x<MAX_X; x++) {
                // 壁をつくる
                if (x == 0 || x == MAX_X - 1) {
                    board[y][x] = 1;
                    boardImage[y][x] = Block.WALL;
                } else if (y == MAX_Y - 1) {
                    board[y][x] = 1;
                    boardImage[y][x] = Block.WALL;
                } else {
                    board[y][x] = 0;
                }
            }
        }
    }
    
    /**
     * ボード（固定ブロックを含む）の描画
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g, Image blockImage) {
        for (int y=0; y<MAX_Y; y++) {
            for (int x=0; x<MAX_X; x++) {
                if (board[y][x] == 1) {
                    g.drawImage(blockImage, x * TILE_SIZE, y * TILE_SIZE, x*TILE_SIZE+TILE_SIZE, y*TILE_SIZE+TILE_SIZE,
                            boardImage[y][x] * TILE_SIZE, 0, boardImage[y][x] * TILE_SIZE + TILE_SIZE, TILE_SIZE, null);
                }
            }
        }
    }

    /**
     * ブロックを移動できるか調べる
     * @param newPos ブロックの移動先座標
     * @param block ブロック
     * @return 移動できたらtrue
     */
    public boolean isMovable(Point newPos, int[][] block) {
        for (int y=0; y<Block.MAX_Y; y++) {
            for (int x=0; x<Block.MAX_X; x++) {
                if (block[y][x] == 1) {  // 4×4内でブロックのあるマスのみ調べる
                    if (newPos.y + y < 0) {  // そのマスが画面の上端外のとき
                        // ブロックのあるマスが壁のある0列目以下または
                        // MAX_X-1列目以上に移動しようとしてる場合は移動できない
                        if (newPos.x + x <= 0 || newPos.x + x >= MAX_X - 1) {
                            return false;
                        }
                    } else if (board[newPos.y+y][newPos.x+x] == 1) {  // そのマスが画面内のとき
                        // 移動先にすでにブロックがある場合は移動できない
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * 落ちきったブロックをボードに固定する
     * @param pos ブロックの位置
     * @param block ブロック
     * @param color ブロックの色
     */
    public void fixBlock(Point pos, int[][] block, int imageNo) {
        for (int y=0; y<Block.MAX_Y; y++) {
            for (int x=0; x<Block.MAX_X; x++) {
                if (block[y][x] == 1) {
                    if (pos.y + y < 0) continue;
                    board[pos.y+y][pos.x+x] = 1;
                    boardImage[pos.y+y][pos.x+x] = imageNo;
                }
            }
        }
    }
    
    /**
     * そろった行を削除
     */
    public void deleteLine() {
        for (int y=0; y<MAX_Y-1; y++) {
            int count = 0;
            for (int x=1; x<MAX_X-1; x++) {
                // ブロックがある列を数える
                if (board[y][x] == 1) count++;
            }
            // そろった行が見つかった
            if (count == Board.MAX_X - 2) {
                // その行を消去
                for (int x=1; x<MAX_X-1; x++) {
                    board[y][x] = 0;
                }
                // それより上の行を落とす
                for (int ty=y; ty>0; ty--) {
                    for (int tx=1; tx<MAX_X-1; tx++) {
                        board[ty][tx] = board[ty-1][tx];
                        boardImage[ty][tx] = boardImage[ty-1][tx];
                    }
                }
            }
        }
    }
    
    /**
     * ブロックが積み上がってるか
     * @return 最上行まで積み上がってたらtrue
     */
    public boolean isStacked() {
        for (int x=1; x<MAX_X-1; x++) {
            if (board[0][x] == 1) {
                return true;
            }
        }
        
        return false;
    }
}
