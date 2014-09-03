package dqc;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

/*
 * DQCメインフレーム
 */

public class DQC extends JFrame implements KeyListener, Runnable {
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    // 方向定数
    public static final int DOWN = 0;
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    // フルスクリーンモードにするならtrue、ウィンドウモードならfalse
    private boolean isFullScreenMode = true;

    // 1フレームの持ち時間（ms）
    private static final int PERIOD = 10;

    // キャラクター
    private Hero hero;  // 主人公

    // マップ
    public static MapManager mapManager = new MapManager();
    
    // アクションキー
    private ActionKey downKey = new ActionKey();
    private ActionKey upKey = new ActionKey();
    private ActionKey leftKey = new ActionKey();
    private ActionKey rightKey = new ActionKey();
    private ActionKey anyKey = new ActionKey(ActionKey.DETECT_INITIAL_PRESS_ONLY);  // どのキーでもよい
    private ActionKey spaceKey = new ActionKey(ActionKey.DETECT_INITIAL_PRESS_ONLY);  // 便利キー
    private ActionKey talkKey = new ActionKey(ActionKey.DETECT_INITIAL_PRESS_ONLY);  // 話す
    private ActionKey searchKey = new ActionKey(ActionKey.DETECT_INITIAL_PRESS_ONLY);  // 調べる
    private ActionKey openKey = new ActionKey(ActionKey.DETECT_INITIAL_PRESS_ONLY);  // 開ける
    
    // ゲームループ
    private Thread gameLoop;
    private volatile boolean running = false;

    // フルスクリーン用
    private GraphicsDevice device;
    // ダブルバッファリング用
    private BufferStrategy bufferStrategy;

    // 乱数生成器
    private Random rand;

    // メッセージウィンドウ
    private MessageWindow msgWnd;

    // サウンドマネジャー
    public static SoundManager soundManager = new SoundManager();
    
    private Font font = new Font("HG創英角ｺﾞｼｯｸUB", Font.PLAIN, 16);

    public DQC() {
        setTitle("DQC");
        setBounds(0, 0, WIDTH, HEIGHT);
        setResizable(false);
        setIgnoreRepaint(true); // paintイベントを無効化

        // フルスクリーンモードの初期化
        if (isFullScreenMode) {
            initFullScreen(); // setVisible()の前でないとダメ！
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // BufferStrategyを使用
        // setVisible()の後でないとダメ！
        createBufferStrategy(2);
        bufferStrategy = getBufferStrategy();

        addKeyListener(this);

        rand = new Random(System.currentTimeMillis());

        Map curMap = mapManager.getMap("king_room");
        mapManager.setCurMap(curMap);
        soundManager.playBGM(curMap.getBGM());
        
        hero = new Hero(8, 6, 0, Chara.DOWN, Chara.STOP, "", curMap);
        curMap.addEvent(hero);
        
        msgWnd = new MessageWindow(new Rectangle(80, 352, 480, 108));
        
        // ゲームループ開始
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    /**
     * ゲームループ
     * 
     */
    public void run() {
        long beforeTime, timeDiff, sleepTime;
        beforeTime = System.currentTimeMillis();
        running = true;
        while (running) {
            gameUpdate(); // ゲーム状態の更新
            gameRender(); // バッファにレンダリング
            paintScreen(); // バッファを画面に描画

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleepTime = PERIOD - timeDiff; // このフレームの残り時間

            if (sleepTime <= 0) {
                sleepTime = 5; // 最低5msは休む
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            beforeTime = System.currentTimeMillis();
        }

        // ゲームループをぬけたら終了
        System.exit(0);
    }

    /**
     * ゲーム状態を更新する
     * 
     */
    private void gameUpdate() {
        // キー入力をチェック
        checkInput();

        // メッセージウィンドウ表示中は移動しない
        if (msgWnd.isVisible()) {
            return;
        }
        
        // 主人公の移動処理
        heroMove();
        
        // その他のキャラクターの移動処理
        charaMove();
    }

    /**
     * バッファにレンダリング
     * 
     */
    private void gameRender() {
        // バッファの描画デバイスを取得
        Graphics2D dbg = (Graphics2D)bufferStrategy.getDrawGraphics();
        
        dbg.setColor(Color.BLACK);
        dbg.fillRect(0, 0, WIDTH, HEIGHT);

        dbg.setFont(font);
        
        Map curMap = mapManager.getCurMap();
        
        // オフセットを計算
        int offsetX = hero.getPx() - WIDTH / 2;
        int offsetY = hero.getPy() - HEIGHT / 2;
        // マップの端ではスクロールしないようにする
        offsetX = Math.max(offsetX, 0);
        offsetX = Math.min(offsetX, curMap.getWidth() - WIDTH);

        offsetY = Math.max(offsetY, 0);
        offsetY = Math.min(offsetY, curMap.getHeight() - HEIGHT);

        curMap.draw(dbg, offsetX, offsetY);
        
        msgWnd.draw(dbg);

        dbg.dispose();
    }

    /**
     * バッファを画面に描画
     * 
     */
    private void paintScreen() {
        if (!bufferStrategy.contentsLost()) {
            bufferStrategy.show();
        }
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * キー入力をチェック
     * 
     */
    private void checkInput() {
        // メッセージウィンドウ表示中
        if (msgWnd.isVisible()) {
            msgWndCheckInput();
        } else {
            mainCheckInput();
        }
    }
    
    /**
     * メインウィンドウのキー処理
     *
     */
    private void mainCheckInput() {
        // TODO: 便利キー（はなす・しらべるなど状況に応じて）の処理追加
        if (downKey.isPressed()) {
            // 移動中でなければ移動開始
            if (!hero.isMoving()) {
                hero.setDirection(DOWN);
                hero.setMoving(true);
            }
        } else if (upKey.isPressed()) {
            if (!hero.isMoving()) {
                hero.setDirection(UP);
                hero.setMoving(true);
            }
        } else if (leftKey.isPressed()) {
            if (!hero.isMoving()) {
                hero.setDirection(LEFT);
                hero.setMoving(true);
            }
        } else if (rightKey.isPressed()) {
            if (!hero.isMoving()) {
                hero.setDirection(RIGHT);
                hero.setMoving(true);
            }
        } else if (talkKey.isPressed()) {
            if (!hero.isMoving()) {  // はなす
                hero.talk(msgWnd);
            }
        } else if (searchKey.isPressed()) {  // 足元をしらべる
            if (!hero.isMoving()) {
                hero.search(msgWnd);

            }
        } else if (openKey.isPressed()) {  // 開ける
            if (!hero.isMoving()) {
                hero.open(msgWnd);
            }
        }
    }
    
    /**
     * メッセージウィンドウ表示中のキー処理
     *
     */
    private void msgWndCheckInput() {
        if (anyKey.isPressed()) {
            // 次のメッセージへ
            if (msgWnd.nextMessage()) {
                // メッセージが終了したらウィンドウを隠す
                msgWnd.hide();
            }
        }
        
        downKey.reset();
        upKey.reset();
        leftKey.reset();
        rightKey.reset();
        talkKey.reset();
        searchKey.reset();
        openKey.reset();
        spaceKey.reset();
    }
    
    /**
     * 主人公の移動処理
     *
     */
    private void heroMove() {
        // スクロール中なら移動を続ける
        if (hero.isMoving()) {
            hero.move();
        }
    }
    
    /**
     * キャラクターの移動処理
     * 
     */
    private void charaMove() {
        // マップにいるイベントを取得
        ArrayList eventList = mapManager.getCurMap().getEventList();

        for (int i = 0; i < eventList.size(); i++) {
            Event evt = (Event) eventList.get(i);
            if (evt instanceof Chara) {
                Chara chara = (Chara) evt;
                // キャラクターの移動タイプを調べる
                if (chara.getMoveType() == Chara.MOVE_AROUND) {
                    if (chara.isMoving()) { // 移動中
                        chara.move(); // 移動を続ける
                    } else if (rand.nextDouble() < Chara.PROB_MOVE) { // 停止中
                        // 移動してない場合はPROB_MOVEの確率で再移動する
                        // 方向はランダム
                        chara.setDirection(rand.nextInt(4));
                        chara.setMoving(true);
                    }
                }
            }
        }
    }

    /**
     * フルスクリーンモードの初期化
     * 
     */
    private void initFullScreen() {
        GraphicsEnvironment env = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        device = env.getDefaultScreenDevice();

        // タイトルバーを消す
        setUndecorated(true);
        // マウスカーソルを消す
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(), "");
        setCursor(cursor);

        if (!device.isFullScreenSupported()) {
            System.out.println("フルスクリーンモードはサポートされていません。");
            System.exit(0);
        }

        device.setFullScreenWindow(this); // フルスクリーン化！

        DisplayMode mode = new DisplayMode(640, 480, 16,
                DisplayMode.REFRESH_RATE_UNKNOWN);
        device.setDisplayMode(mode);
    }
    
    /**
     * キーが押されたときの処理
     * 
     */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        anyKey.press();
        
        switch (keyCode) {
            case KeyEvent.VK_DOWN :
                downKey.press();
                break;
            case KeyEvent.VK_UP :
                upKey.press();
                break;
            case KeyEvent.VK_LEFT :
                leftKey.press();
                break;
            case KeyEvent.VK_RIGHT :
                rightKey.press();
                break;
            case KeyEvent.VK_SPACE:
                spaceKey.press();
                break;
            case KeyEvent.VK_T:
                talkKey.press();
                break;
            case KeyEvent.VK_S:
                searchKey.press();
                break;
            case KeyEvent.VK_O:
                openKey.press();
                break;
            case KeyEvent.VK_ESCAPE :
                System.exit(0);
        }
    }

    /**
     * キーが離されたときの処理
     * 
     */
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        anyKey.release();
        
        switch (keyCode) {
            case KeyEvent.VK_DOWN :
                downKey.release();
                break;
            case KeyEvent.VK_UP :
                upKey.release();
                break;
            case KeyEvent.VK_LEFT :
                leftKey.release();
                break;
            case KeyEvent.VK_RIGHT :
                rightKey.release();
                break;
            case KeyEvent.VK_SPACE:
                spaceKey.release();
                break;
            case KeyEvent.VK_T:
                talkKey.release();
                break;
            case KeyEvent.VK_S:
                searchKey.release();
                break;
            case KeyEvent.VK_O:
                openKey.release();
                break;
            case KeyEvent.VK_ESCAPE :
                System.exit(0);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        new DQC();
    }
}
