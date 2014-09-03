import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Random;

/*
 * Created on 2005/04/24
 *
 */

/**
 * @author mori
 *  
 */
public class Map {
    // チップセットのサイズ（単位：ピクセル）
    private static final int CS = 16;

    // 行、列数（マス）
    private static final int ROW = 32;
    private static final int COL = 42;

    // 地形値
    private static final int GROUND = 0; // 地面
    private static final int WATER = 1; // 水
    private static final int BLACK_HOME = 2; // 黒蟻の巣
    private static final int RED_HOME = 3; // 赤蟻の巣
    private static final int POISON = 4; // 毒
    private static final int FOOD = 5; // 食べ物

    // 黒蟻の巣の位置
    public static final Point BLACK_HOME_POS = new Point(36, 5);
    // 赤蟻の巣の位置
    public static final Point RED_HOME_POS = new Point(5, 5);

    // 水の数
    private static final int MAX_WATER = 100;
    // 毒の数
    private static final int MAX_POISON = 5;
    // 食べ物の数
    private static final int MAX_FOOD = 100;

    // マップ
    private int[][] map = new int[ROW][COL];

    // マップセット
    private Image blackHomeImage;
    private Image redHomeImage;

    private Random rand = new Random();

    /**
     * コンストラクタ。
     */
    public Map() {
        init();
    }

    /**
     * マップを初期化する
     */
    public void init() {
        // 地面で初期化
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                map[i][j] = GROUND;
            }
        }

        // 水をばらまく
        for (int i = 0; i < MAX_WATER; i++) {
            map[rand.nextInt(ROW)][rand.nextInt(COL)] = WATER;
        }
        // 毒をばらまく（ごめん！蟻さんたち）
        for (int i = 0; i < MAX_POISON; i++) {
            map[rand.nextInt(ROW)][rand.nextInt(COL)] = POISON;
        }
        // 食べ物をばらまく
        for (int i = 0; i < MAX_FOOD; i++) {
            map[rand.nextInt(ROW)][rand.nextInt(COL)] = FOOD;
        }

        // 黒蟻の巣
        map[BLACK_HOME_POS.y][BLACK_HOME_POS.x] = BLACK_HOME;
        // 赤蟻の巣
        map[RED_HOME_POS.y][RED_HOME_POS.x] = RED_HOME;
    }

    /**
     * マップを描く。
     * 
     * @param g 指定されたGraphicsウィンドウ
     */
    public void draw(Graphics g) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                // mapの値に応じて画像を描く
                switch (map[i][j]) {
                    case 0 : // 地面
                        g.setColor(new Color(153, 255, 153));
                        g.fillRect(j * CS, i * CS, CS, CS);
                        break;
                    case 1 : // 水
                        g.setColor(Color.BLUE);
                        g.fillRect(j * CS, i * CS, CS, CS);
                        break;
                    case 2 : // 黒蟻の巣
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillOval(j * CS, i * CS, CS, CS);
                        break;
                    case 3 : // 赤蟻の巣
                        g.setColor(Color.PINK);
                        g.fillOval(j * CS, i * CS, CS, CS);
                        break;
                    case 4 : // 毒
                        g.setColor(new Color(204, 153, 204));
                        g.fillRect(j * CS, i * CS, CS, CS);
                        break;
                    case 5 : // 食べ物（砂糖のつもり）
                        g.setColor(Color.WHITE);
                        g.fillRect(j * CS, i * CS, CS, CS);
                        break;
                }
            }
        }
    }

    /**
     * (x,y)にぶつかるものがあるか調べる
     * 
     * @param x マップのx座標
     * @param y マップのy座標
     * @return (x,y)にぶつかるものがあったらtrueを返す
     */
    public boolean isHit(int x, int y) {
        // 画面の範囲外だったらぶつかる
        if (x < 0 || x > COL - 1 || y < 0 || y > ROW - 1) {
            return true;
        }

        return false;
    }

    /**
     * 食べ物があるか？
     * 
     * @param x x座標
     * @param y y座標
     * @return 食べ物があったらtrue
     */
    public boolean isFood(int x, int y) {
        if (map[y][x] == FOOD) {
            return true;
        }
        return false;
    }

    /**
     * 毒があるか？
     * 
     * @param x x座標
     * @param y y座標
     * @return 毒があったらtrue
     */
    public boolean isPoison(int x, int y) {
        if (map[y][x] == POISON) {
            return true;
        }
        return false;
    }

    /**
     * 水があるか？
     * 
     * @param x x座標
     * @param y y座標
     * @return 水があったらtrue
     */
    public boolean isWater(int x, int y) {
        if (map[y][x] == WATER) {
            return true;
        }
        return false;
    }

    /**
     * ただの地面にする
     * 
     * @param x x座標
     * @param y y座標
     */
    public void setGround(int x, int y) {
        map[y][x] = GROUND;
    }

    /**
     * 食べ物をランダムな場所に置く
     */
    public void scatterFood() {
        int x, y;
        // 地面の場所を探す
        do {
            x = rand.nextInt(COL - 1);
            y = rand.nextInt(ROW - 1);
        } while (map[y][x] != GROUND);
        map[y][x] = FOOD;
    }

    /**
     * 毒をランダムな場所に置く
     */
    public void scatterPoison() {
        int x, y;
        // 地面の場所を探す
        do {
            x = rand.nextInt(COL - 1);
            y = rand.nextInt(ROW - 1);
        } while (map[y][x] != GROUND);
        map[y][x] = POISON;
    }

    /**
     * 水をランダムな場所に置く
     */
    public void scatterWater() {
        int x, y;
        // 地面の場所を探す
        do {
            x = rand.nextInt(COL - 1);
            y = rand.nextInt(ROW - 1);
        } while (map[y][x] != GROUND);
        map[y][x] = WATER;
    }

    /**
     * マップをコンソールに表示。デバッグ用。
     */
    public void show() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
}