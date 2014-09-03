import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/*
 * Created on 2005/06/16
 *
 */

/**
 * @author mori
 *  
 */
public class Map {
    // タイルサイズ
    private static final int TILE_SIZE = 32;
    // 行数
    private static final int ROW = 15;
    // 列数
    private static final int COL = 20;
    // 重力
    public static final double GRAVITY = 1.0;

    // マップ
    private int[][] map = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    public Map() {
    }

    /**
     * マップを描画する
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                // mapの値に応じて画像を描く
                switch (map[i][j]) {
                    case 1 : // ブロック
                        g.fillRect(tilesToPixels(j), tilesToPixels(i), TILE_SIZE, TILE_SIZE);
                        break;
                }
            }
        }
    }
    
    /**
     * (newX, newY)で衝突するブロックの座標を返す
     * @param player プレイヤーへの参照
     * @param newX X座標
     * @param newY Y座標
     * @return 衝突するブロックの座標
     */
    public Point getTileCollision(Player player, double newX, double newY) {
        // 小数点以下切り上げ
        // 浮動小数点の関係で切り上げしないと衝突してないと判定される場合がある
        newX = Math.ceil(newX);
        newY = Math.ceil(newY);

        double fromX = Math.min(player.getX(), newX);
        double fromY = Math.min(player.getY(), newY);
        double toX = Math.max(player.getX(), newX);
        double toY = Math.max(player.getY(), newY);
        
        int fromTileX = pixelsToTiles(fromX);
        int fromTileY = pixelsToTiles(fromY);
        int toTileX = pixelsToTiles(toX + Player.WIDTH - 1);
        int toTileY = pixelsToTiles(toY + Player.HEIGHT - 1);

        // 衝突しているか調べる
        for (int x = fromTileX; x <= toTileX; x++) {
            for (int y = fromTileY; y <= toTileY; y++) {
                // 画面外は衝突
                if (x < 0 || x >= COL) {
                    return new Point(x, y);
                }
                if (y < 0 || y >= ROW) {
                    return new Point(x, y);
                }
                // ブロックがあったら衝突
                if (map[y][x] == 1) {
                    return new Point(x, y);
                }
            }
        }
        
        return null;
    }
    
    /**
     * ピクセル単位をタイル単位に変更する
     * @param pixels ピクセル単位
     * @return タイル単位
     */
    public static int pixelsToTiles(double pixels) {
        return (int)Math.floor(pixels / TILE_SIZE);
    }
    
    /**
     * タイル単位をピクセル単位に変更する
     * @param tiles タイル単位
     * @return ピクセル単位
     */
    public static int tilesToPixels(int tiles) {
        return tiles * TILE_SIZE;
    }
}