import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.JPanel;

/*
 * Created on 2007/04/26
 */

public class MainPanel extends JPanel implements Runnable {
    public static final int WIDTH = 360;
    public static final int HEIGHT = 360;

    // ボールの数
    private static final int NUM_BALLS = 50;

    // 期待するFPS（1秒間に描画するフレーム数）
    private static final int FPS = 50;  // ★値を変えてみよう

    // 1フレームで使える持ち時間
    private static final long PERIOD = (long) (1.0 / FPS * 1000000000); // 単位: ns

    // FPSを計算する間隔（1s = 10^9ns）
    private static long MAX_STATS_INTERVAL = 1000000000L; // 単位: ns

    // ボール
    private Ball[] ball;

    // ゲームループ用スレッド
    private volatile boolean running = false;
    private Thread gameLoop;

    // ダブルバッファリング用
    private Graphics dbg;
    private Image dbImage = null;

    private Random rand;

    // FPS計算用
    private long calcInterval = 0L; // in ns
    private long prevCalcTime;

    // フレーム数
    private long frameCount = 0;
    // 実際のFPS
    private double actualFPS = 0.0;

    private DecimalFormat df = new DecimalFormat("0.0");

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        rand = new Random(System.currentTimeMillis());

        ball = new Ball[NUM_BALLS];
        for (int i = 0; i < NUM_BALLS; i++) {
            // 位置・速度をランダムに設定
            int x = rand.nextInt(WIDTH);
            int y = rand.nextInt(HEIGHT);
            int vx = rand.nextInt(10);
            int vy = rand.nextInt(10);
            // ボールを作成
            ball[i] = new Ball(x, y, vx, vy);
        }
    }

    public void addNotify() {
        super.addNotify();
        // ゲームループの起動
        if (gameLoop == null || !running) {
            gameLoop = new Thread(this);
            gameLoop.start();
        }
    }

    public void run() {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;

        beforeTime = System.nanoTime();
        prevCalcTime = beforeTime;

        running = true;
        while (running) {
            gameUpdate();
            gameRender();
            paintScreen();

            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            // 前回のフレームの休止時間誤差も引いておく
            sleepTime = (PERIOD - timeDiff) - overSleepTime;

            if (sleepTime > 0) {
                // 休止時間がとれる場合
                try {
                    Thread.sleep(sleepTime / 1000000L); // nano->ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // sleep()の誤差
                overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
            } else {
                // 状態更新・レンダリングで時間を使い切ってしまい
                // 休止時間がとれない場合
                overSleepTime = 0L;
                // 休止なしが16回以上続いたら
                if (++noDelays >= 16) {
                    Thread.yield(); // 他のスレッドを強制実行
                    noDelays = 0;
                }
            }

            beforeTime = System.nanoTime();

            // FPSを計算
            calcFPS();
        }

        System.exit(0);
    }

    private void gameUpdate() {
        for (int i = 0; i < NUM_BALLS; i++) {
            ball[i].move();
        }
    }

    private void gameRender() {
        if (dbImage == null) {
            dbImage = createImage(WIDTH, HEIGHT);
            if (dbImage == null) {
                System.out.println("dbImageが作成できません。");
                return;
            } else {
                dbg = dbImage.getGraphics();
            }
        }

        // 背景の塗りつぶし
        dbg.setColor(Color.WHITE);
        dbg.fillRect(0, 0, WIDTH, HEIGHT);

        // FPSの描画
        dbg.setColor(Color.BLUE);
        dbg.drawString("FPS: " + df.format(actualFPS), 4, 16);

        // ボールの描画
        for (int i = 0; i < NUM_BALLS; i++) {
            ball[i].draw(dbg);
        }
    }

    private void paintScreen() {
        Graphics g;
        try {
            g = this.getGraphics();
            if ((g != null) && (dbImage != null)) {
                g.drawImage(dbImage, 0, 0, null);
            }
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * FPSの計算
     * 
     */
    private void calcFPS() {
        frameCount++;
        calcInterval += PERIOD;

        // 1秒おきにFPSを再計算する
        if (calcInterval >= MAX_STATS_INTERVAL) {
            long timeNow = System.nanoTime();
            // 実際の経過時間を測定
            long realElapsedTime = timeNow - prevCalcTime; // 単位: ns

            // FPSを計算
            // realElapsedTimeの単位はnsなのでsに変換する
            actualFPS = ((double) frameCount / realElapsedTime) * 1000000000L;
            System.out.println(df.format(actualFPS));

            frameCount = 0L;
            calcInterval = 0L;
            prevCalcTime = timeNow;
        }
    }
}
