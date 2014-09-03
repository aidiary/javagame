import java.awt.image.WritableRaster;

/*
 * メタボール
 * 『C言語逆引き大全500の極意』のp.331を参考
 * 
 * Created on 2006/04/30
 */

public class Metaball {
    private int x; // ボールの中心座標
    private int y;
    private int vx; // ボールの移動速度
    private int vy;
    private Pixel[] pixels; // ボールの各ピクセル情報
    private Palette palette; // パレット

    private static final int RADIUS = 70; // ボールの半径
    private static final int MAX_PIXELS = (2 * RADIUS) * (2 * RADIUS); // 円を囲む四角形のピクセル数

    /**
     * コンストラクタ
     * 
     * @param x メタボールの中心のX座標
     * @param y
     * @param vx
     * @param vy
     */
    public Metaball(int x, int y, int vx, int vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        pixels = new Pixel[MAX_PIXELS];
        for (int i = 0; i < MAX_PIXELS; i++) {
            pixels[i] = new Pixel();
        }
        palette = new Palette();

        init();
    }

    /**
     * メタボールを初期化
     */
    public void init() {
        int no;

        int c = 0;
        // 円の中身の各ピクセルについて処理
        // 円の中身の点(x,y)ははx^2+y^2 < r^2を満たすので
        // z = r^2 - x^2 - y^2 > 0なら円の中身と判定できる
        for (int i = -RADIUS; i < RADIUS; i++) {
            for (int j = -RADIUS; j < RADIUS; j++) {
                double z = RADIUS * RADIUS - i * i - j * j; // 円の内部の点か判定
                if (z < 0) { // 円の外側
                    no = 0;
                } else { // 円の中側
                    // 円内のピクセルの色（パレット番号）を決定している
                    z = Math.sqrt(z);
                    double t = z / RADIUS;
                    no = (int) (Palette.MAX_PAL * (t * t * t * t));
                    if (no > 255)
                        no = 255;
                    if (no < 0)
                        no = 0;
                }

                pixels[c].dx = i;
                pixels[c].dy = j;
                pixels[c].no = no;
                c++;
            }
        }
    }

    /**
     * メタボールを描画
     * 
     * @param raster
     */
    public void draw(WritableRaster raster) {
        for (int i = 0; i < MAX_PIXELS; i++) {
            int sx = x + pixels[i].dx;
            if (sx < 0 || sx > MainPanel.WIDTH)
                continue;
            int sy = y + pixels[i].dy;
            if (sy < 0 || sy > MainPanel.HEIGHT)
                continue;

            // ボールの色をイメージのRGBへ加算する
            int no = pixels[i].no; // パレット番号
            int[] color = palette.getColor(no); // パレット番号からRGBを取り出す

            // スクリーンの色を取り出す
            int[] p = new int[3];
            raster.getPixel(sx, sy, p);

            // スクリーンの色にボールの色を加算する
            for (int j = 0; j < 3; j++) {
                p[j] += color[j];
                if (p[j] > 255)
                    p[j] = 255;
            }

            // スクリーンに新しい色を書き込む
            raster.setPixel(sx, sy, p);
        }
    }

    /**
     * メタボールを移動する
     */
    public void move() {
        x += vx;
        y += vy;

        // 範囲内かチェック
        if (x < RADIUS) {
            x = RADIUS;
            vx = -vx;
        }
        if (y < RADIUS) {
            y = RADIUS;
            vy = -vy;
        }
        if (x > MainPanel.WIDTH - RADIUS) {
            x = MainPanel.WIDTH - RADIUS;
            vx = -vx;
        }
        if (y > MainPanel.HEIGHT - RADIUS) {
            y = MainPanel.HEIGHT - RADIUS;
            vy = -vy;
        }
    }

    /**
     * メタボール内のピクセル情報
     */
    class Pixel {
        public int dx; // ボールの中心からの偏差
        public int dy;
        public int no; // パレット番号
    }

    /**
     * パレットクラス 0～255の各番号に色をセットできる
     */
    class Palette {
        public static final int MAX_PAL = 256;
        private int[] red; // 赤成分
        private int[] green; // 緑成分
        private int[] blue; // 青成分

        public Palette() {
            red = new int[MAX_PAL];
            green = new int[MAX_PAL];
            blue = new int[MAX_PAL];

            // パレットを初期化
            init();
        }

        /**
         * パレットを初期化する
         */
        public void init() {
            // 各パレット番号にRGBを設定
            // 赤基調
            int r, g, b;
            for (int i = 0; i < MAX_PAL; i++) {
                r = g = b = 0;
                // ここのr,g,bの順番を変えると違う色のメタボールができる
                if (i >= 0)
                    r = 4 * i;
                if (i >= 2)
                    g = 4 * (i / 2);
                if (i >= 4)
                    b = 4 * (i / 4);

                if (r > 255)
                    r = 255;
                if (g > 255)
                    g = 255;
                if (b > 255)
                    b = 255;

                red[i] = r;
                green[i] = g;
                blue[i] = b;
            }
        }

        /**
         * パレット番号からRGB配列へ変換
         * 
         * @param no パレット番号
         * @return RGB配列
         */
        public int[] getColor(int no) {
            int[] color = new int[3];
            color[0] = red[no];
            color[1] = green[no];
            color[2] = blue[no];

            return color;
        }
    }
}
