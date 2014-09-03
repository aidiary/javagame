import java.awt.Color;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.JFrame;

/*
 * Created on 2007/05/02
 */

public class FullScreenTest extends JFrame implements Runnable {
    // ボールの数
    private static final int NUM_BALLS = 256;
    private Ball[] ball;
    private Random rand;

    // ゲームループ
    private Thread gameLoop;
    private volatile boolean running;

    // フルスクリーン用
    private static final int NUM_BUFFERS = 2; // BufferStrategyのバッファの数
    private boolean isFullScreenMode = true; // フルスクリーンモードにするか？
    private GraphicsDevice device;
    private BufferStrategy bufferStrategy;

    // デフォルトフレームサイズ
    private int width = 640;
    private int height = 480;

    // FPS計算用
    private static final int FPS = 60; // 期待するFPS
    private static final long PERIOD = (long) (1.0 / FPS * 1000000000); // 1フレームの時間
    // 単位:
    // ns
    private static long MAX_STATS_INTERVAL = 1000000000L; // FPS計算間隔 単位: ns
    private long calcInterval = 0L;
    private long prevCalcTime;
    private long frameCount = 0; // フレームカウンター
    private double actualFPS = 0.0; // 実際のFPS
    private DecimalFormat df = new DecimalFormat("0.0");

    public FullScreenTest() {
        setTitle("フルスクリーン");
        setBounds(0, 0, width, height);
        setResizable(false);
        setIgnoreRepaint(true); // paintイベントを無効化

        // フルスクリーンモードの初期化
        if (isFullScreenMode) {
            initFullScreen();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // BufferStrategyの設定
        createBufferStrategy(NUM_BUFFERS);
        bufferStrategy = getBufferStrategy();

        rand = new Random(System.currentTimeMillis());
        ball = new Ball[NUM_BALLS];
        for (int i = 0; i < NUM_BALLS; i++) {
            // 位置・速度をランダムに設定
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            int vx = rand.nextInt(10);
            int vy = rand.nextInt(10);
            // ボールを作成
            ball[i] = new Ball(x, y, vx, vy, width, height);
        }

        // 終了キーの定義（ESCキーで終了）
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    running = false;
                }
            }
        });

        // ゲームループ開始
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    /**
     * ゲームループ
     * 
     */
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
            screenUpdate();

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

        // ゲームループをぬけたら終了
        restoreScreen();
        System.exit(0);
    }

    /**
     * フルスクリーンモードの後処理
     * 
     */
    private void restoreScreen() {
        Window w = device.getFullScreenWindow();
        if (w != null) {
            w.dispose();
        }
        device.setFullScreenWindow(null);
    }

    /**
     * ゲーム状態の更新
     * 
     */
    private void gameUpdate() {
        for (int i = 0; i < NUM_BALLS; i++) {
            ball[i].move();
        }
    }

    /**
     * レンダリング
     * 
     */
    private void gameRender() {
        Graphics dbg = bufferStrategy.getDrawGraphics();

        // 背景
        dbg.setColor(Color.BLACK);
        dbg.fillRect(0, 0, width, height);

        // ボールの描画
        for (int i = 0; i < NUM_BALLS; i++) {
            ball[i].draw(dbg);
        }

        // FPSの描画
        dbg.setColor(Color.YELLOW);
        dbg.drawString("FPS: " + df.format(actualFPS), 4, 16);

        dbg.dispose();
    }

    /**
     * スクリーンの更新（BufferStrategyを使用）
     * 
     */
    private void screenUpdate() {
        if (!bufferStrategy.contentsLost()) {
            bufferStrategy.show();
        } else {
            System.out.println("Contents Lost");
        }
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * フルスクリーンモードの初期化
     * 
     */
    private void initFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        device = ge.getDefaultScreenDevice();

        setUndecorated(true); // タイトルバー・ボーダー非表示

        // 必要ならマウスカーソルを消す
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(), "");
        setCursor(cursor);

        if (!device.isFullScreenSupported()) {
            System.out.println("フルスクリーンモードはサポートされていません。");
            System.exit(0);
        }

        // フルスクリーン化！
        device.setFullScreenWindow(this);
        // 変更可能なディスプレイモードを表示
        showDisplayModes();
        // ディスプレイモードの変更はフルスクリーン化後
        // 変更可能なディスプレイモードしか使えない
        // 640x480,800x600,1024x768の32bitあたりが妥当
        setDisplayMode(1024, 768, 32);
        showCurrentMode();

        width = getBounds().width;
        height = getBounds().height;
    }

    private void showDisplayModes() {
        System.out.println("変更可能なディスプレイモード");
        DisplayMode[] modes = device.getDisplayModes();
        for (int i = 0; i < modes.length; i++) {
            System.out.print("(" + modes[i].getWidth() + ","
                    + modes[i].getHeight() + "," + modes[i].getBitDepth() + ","
                    + modes[i].getRefreshRate() + ") ");
            if ((i + 1) % 4 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    /**
     * ディスプレイモードを設定
     * 
     * @param width
     * @param height
     * @param bitDepth
     */
    private void setDisplayMode(int width, int height, int bitDepth) {
        if (!device.isDisplayChangeSupported()) {
            System.out.println("ディスプレイモードの変更はサポートされていません。");
            return;
        }

        DisplayMode dm = new DisplayMode(width, height, bitDepth,
                DisplayMode.REFRESH_RATE_UNKNOWN);
        device.setDisplayMode(dm);
    }

    /**
     * 現在のディスプレイモードを表示
     * 
     */
    private void showCurrentMode() {
        DisplayMode dm = device.getDisplayMode();
        System.out.println("現在のディスプレイモード");
        System.out.println("Width: " + dm.getWidth());
        System.out.println("Height: " + dm.getHeight());
        System.out.println("Bit Depth: " + dm.getBitDepth());
        System.out.println("Refresh Rate: " + dm.getRefreshRate());
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
            // System.out.println(df.format(actualFPS));

            frameCount = 0L;
            calcInterval = 0L;
            prevCalcTime = timeNow;
        }
    }

    public static void main(String[] args) {
        new FullScreenTest();
    }
}
