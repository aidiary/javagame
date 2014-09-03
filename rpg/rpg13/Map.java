import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.ImageIcon;

/*
 * Created on 2005/10/10
 *
 */

/**
 * @author mori
 *
 */
public class Map implements Common {
    // マップ
    private int[][] map;
    
    // マップの行数・列数（単位：マス）
    private int row;
    private int col;
    
    // マップ全体の大きさ（単位：ピクセル）
    private int width;
    private int height;

    private static Image chipImage;

    // このマップにいるキャラクターたち
    private Vector charas = new Vector();
    
    // メインパネルへの参照
    private MainPanel panel;

    public Map(String filename, MainPanel panel) {
        // マップをロード
        load(filename);
        
        // 初回の呼び出しのみイメージをロード
        if (chipImage == null) {
            loadImage();
        }
    }

    public void draw(Graphics g, int offsetX, int offsetY) {
        // オフセットを元に描画範囲を求める
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(MainPanel.WIDTH) + 1;
        // 描画範囲がマップの大きさより大きくならないように調整
        lastTileX = Math.min(lastTileX, col);
        
        int firstTileY = pixelsToTiles(-offsetY);
        int lastTileY = firstTileY + pixelsToTiles(MainPanel.HEIGHT) + 1;
        // 描画範囲がマップの大きさより大きくならないように調整
        lastTileY = Math.min(lastTileY, row);

        for (int i = firstTileY; i < lastTileY; i++) {
            for (int j = firstTileX; j < lastTileX; j++) {
                int mapChipNo = map[i][j];
                // イメージ上の位置を求める
                // マップチップイメージは8x8を想定
                int cx = (mapChipNo % 8) * CS;
                int cy = (mapChipNo / 8) * CS;
                g.drawImage(chipImage, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY,
                        tilesToPixels(j) + offsetX + CS, tilesToPixels(i) + offsetY + CS,
                        cx, cy, cx + CS, cy + CS, panel);
            }
        }
        
        // このマップにいるキャラクターを描画
        for (int n=0; n<charas.size(); n++) {
            Chara chara = (Chara)charas.get(n);
            chara.draw(g, offsetX, offsetY);
        }
    }

    /**
     * (x,y)にぶつかるものがあるか調べる。
     * @param x マップのx座標
     * @param y マップのy座標
     * @return (x,y)にぶつかるものがあったらtrueを返す。
     */
    public boolean isHit(int x, int y) {
        // (x,y)に壁か玉座があったらぶつかる
        if (map[y][x] == 1 || map[y][x] == 2) {
            return true;
        }

        // 他のキャラクターがいたらぶつかる
        for (int i = 0; i < charas.size(); i++) {
            Chara chara = (Chara) charas.get(i);
            if (chara.getX() == x && chara.getY() == y) {
                return true;
            }
        }

        // なければぶつからない
        return false;
    }

    /**
     * キャラクターをこのマップに追加する
     * @param chara キャラクター
     */
    public void addChara(Chara chara) {
        charas.add(chara);
    }

    /**
     * ピクセル単位をマス単位に変更する
     * @param pixels ピクセル単位
     * @return マス単位
     */
    public static int pixelsToTiles(double pixels) {
        return (int)Math.floor(pixels / CS);
    }
    
    /**
     * マス単位をピクセル単位に変更する
     * @param tiles マス単位
     * @return ピクセル単位
     */
    public static int tilesToPixels(int tiles) {
        return tiles * CS;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * ファイルからマップを読み込む
     * @param filename 読み込むマップデータのファイル名
     */
    private void load(String filename) {
        try {
            BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(filename)));
            // rowを読み込む
            String line = br.readLine();
            row = Integer.parseInt(line);
            // colを読む
            line = br.readLine();
            col = Integer.parseInt(line);
            // マップサイズを設定
            width = col * CS;
            height = row * CS;
            // マップを作成
            map = new int[row][col];
            for (int i=0; i<row; i++) {
                line = br.readLine();
                for (int j=0; j<col; j++) {
                    map[i][j] = Integer.parseInt(line.charAt(j) + "");
                }
            }
//            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadImage() {
        // マップチップのイメージをロード
        ImageIcon icon = new ImageIcon(getClass().getResource("image/mapchip.gif"));
        chipImage = icon.getImage();
    }

    /**
     * マップをコンソールに表示。デバッグ用。
     */
    public void show() {
        for (int i=0; i<row; i++) {
            for (int j=0; j<col; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
}
