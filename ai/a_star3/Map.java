import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

/*
 * Created on 2005/04/23
 *
 */

/**
 * @author mori
 *  
 */
public class Map {
    // チップセットのサイズ（単位：ピクセル）
    private static final int CS = 16;

    // マップ
    private int[][] map;
    // 行、列数（マス）
    private int row;
    private int col;

    // マップセット
    private Image floorImage;
    private Image wallImage;
    private Image barrierImage;
    
    /**
     * コンストラクタ。
     * 
     * @param filename マップデータのファイル名
     */
    public Map(String filename) {
        // マップを読み込む
        load(filename);
        // show();

        // 床のイメージを読み込む
        ImageIcon icon = new ImageIcon(getClass().getResource("floor.gif"));
        floorImage = icon.getImage();

        // 壁のイメージを読み込む
        icon = new ImageIcon(getClass().getResource("wall.gif"));
        wallImage = icon.getImage();
    }

    /**
     * マップを描く。
     * 
     * @param g 指定されたGraphicsウィンドウ
     */
    public void draw(Graphics g) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                // mapの値に応じて画像を描く
                switch (map[i][j]) {
                    case 0 : // 床
                        g.drawImage(floorImage, j * CS, i * CS, null);
                        break;
                    case 1 : // 壁
                        g.drawImage(wallImage, j * CS, i * CS, null);
                        break;
                    case 2:  // バリア
                        g.drawImage(barrierImage, j*CS, i*CS, null);
                        break;
                }
            }
        }
    }

    /**
     * (x,y)にぶつかるものがあるか調べる。
     * 
     * @param x マップのx座標
     * @param y マップのy座標
     * @return (x,y)にぶつかるものがあったらtrueを返す。
     */
    public boolean isHit(int x, int y) {
        // (x,y)に壁があったらぶつかる
        if (map[y][x] == 1) {
            return true;
        }

        // なければぶつからない
        return false;
    }

    /**
     * 行数を返す
     * 
     * @return 行数
     */
    public int getRow() {
        return row;
    }

    /**
     * 列数を返す
     * 
     * @return 列数
     */
    public int getCol() {
        return col;
    }

    /**
     * ファイルからマップを読み込む
     * 
     * @param filename 読み込むマップデータのファイル名
     */
    private void load(String filename) {
        try {
            // map.datファイルを開く
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    getClass().getResourceAsStream(filename)));
            // 1行読み込む
            String line = br.readLine();
            // マップファイルの最初の1行は
            // 10 10
            // のように行数と列数が入っているとする
            // 空白で分割
            StringTokenizer st = new StringTokenizer(line);
            // rowとcolを読む
            row = Integer.parseInt(st.nextToken());
            col = Integer.parseInt(st.nextToken());
            // マップを作成
            map = new int[row][col];
            for (int i = 0; i < row; i++) {
                line = br.readLine();
                for (int j = 0; j < col; j++) {
                    map[i][j] = Integer.parseInt(line.charAt(j) + "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * マップをコンソールに表示。デバッグ用。
     */
    public void show() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * 地形コストを返す
     * @param pos 座標
     * @return 地形コスト
     */
    public int getCost(Point pos) {
        return 1;
    }
}