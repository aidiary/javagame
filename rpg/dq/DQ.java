import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

/*
 * Created on 2006/2/5
 *
 */

/**
 * @author mori
 *  
 */
public class DQ extends JFrame implements KeyListener, Runnable {
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private static final int PERIOD = 20;  // 50FPSなので1フレーム20ms

    // デバッグモード（trueだと座標などが表示される）
    private static final boolean DEBUG_MODE = true;

    // ゲーム状態
    private int gameState = GameState.TITLE;

    // マップ
    private Map[] maps;
    // 現在のマップ番号
    private int mapNo;

    // 勇者
    private Chara hero;

    // アクションキー
    private ActionKey leftKey;
    private ActionKey rightKey;
    private ActionKey upKey;
    private ActionKey downKey;
    private ActionKey spaceKey;
    private ActionKey cmdLeftKey;
    private ActionKey cmdRightKey;
    private ActionKey cmdUpKey;
    private ActionKey cmdDownKey;

    // サブウィンドウ
    private MessageWindow messageWindow;
    private CommandWindow commandWindow;

    // ゲームループ
    private Thread gameLoop;
    private volatile boolean running = false;

    // 乱数生成器
    private Random rand = new Random();

    // フルスクリーン用
    private GraphicsDevice device;
    private Graphics gScr;
    private BufferStrategy bufferStrategy;

    // BGM名
    private static final String[] bgmNames = {"castle", "overworld", "town", "cave", "village", "shrine", "battle"};
    // 効果音名
    private static final String[] seNames = {"barrier", "beep", "door", "stairs", "treasure", "warp", "spell", "winbattle"};


    // 戦闘画面テスト用スライム
    private BufferedImage enemyImage;

    public DQ() {
        // タイトルを設定
        setTitle("ドラクエクローン");

        initFullScreen();

        addKeyListener(this);

        // アクションキーを作成
        leftKey = new ActionKey();
        rightKey = new ActionKey();
        upKey = new ActionKey();
        downKey = new ActionKey();
        spaceKey = new ActionKey(ActionKey.DETECT_INITIAL_PRESS_ONLY);
        cmdLeftKey = new ActionKey(ActionKey.DETECT_INITIAL_PRESS_ONLY);
        cmdRightKey = new ActionKey(ActionKey.DETECT_INITIAL_PRESS_ONLY);
        cmdUpKey = new ActionKey(ActionKey.DETECT_INITIAL_PRESS_ONLY);
        cmdDownKey = new ActionKey(ActionKey.DETECT_INITIAL_PRESS_ONLY);

        // マップを作成
        maps = new Map[7];
        // 王の間
        maps[0] = new Map("king_room", Sound.CASTLE);
        // ラダトーム城
        maps[1] = new Map("castle", Sound.CASTLE);
        // フィールド
        maps[2] = new Map("overworld", Sound.OVERWORLD);
        // ラダトームの街
        maps[3] = new Map("town", Sound.TOWN);
        // ロトの洞窟
        maps[4] = new Map("cave", Sound.CAVE);
        // ガライの街
        maps[5] = new Map("town2", Sound.VILLAGE);
        // ほこら
        maps[6] = new Map("shrine", Sound.SHRINE);

        // 最初は王の間
        mapNo = 0;

        // 勇者を作成
        hero = new Chara(6, 4, 0, Direction.DOWN, 0, maps[mapNo]);

        // マップにキャラクターを登録
        // キャラクターはマップに属す
        maps[mapNo].addChara(hero);

        // サブウィンドウ
        messageWindow = new MessageWindow(new Rectangle(142, 324, 356, 140));  // TODO: WND_RECT定数に
        commandWindow = new CommandWindow(new Rectangle(30, 10, 232, 156));

        // サウンドをロード
        loadSound();

        // BGMを再生
        MidiEngine.play(maps[mapNo].getBgmNo());

        // テストイメージをロード
        try {
            enemyImage = ImageIO.read(getClass().getResource("image/suraimu.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        startGame();
    }

    private void initFullScreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = env.getDefaultScreenDevice();
        
        setUndecorated(true);  // タイトルバーを表示しない
        setIgnoreRepaint(true);  // アクティブレンダリングするのでpaintイベントを無効
        setResizable(false);
        
        if (!device.isFullScreenSupported()) {
            System.out.println("full-screen mode not supported");
            System.exit(0);
        }
        
        device.setFullScreenWindow(this);  // フルスクリーン！
        
        DisplayMode mode = new DisplayMode(640, 480, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
        device.setDisplayMode(mode);
        
        // BufferStrategyを使用
        createBufferStrategy(2);
        bufferStrategy = getBufferStrategy();
    }

    /**
     * ゲームループ
     */
    public void run() {
        long beforeTime, timeDiff, sleepTime;
        
        beforeTime = System.currentTimeMillis();

        running = true;
        while (running) {
            gameUpdate();  // ゲーム状態の更新
            screenUpdate();  // スクリーンに描画
            WaveEngine.render();  // サウンドのレンダリング

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleepTime = PERIOD - timeDiff;  // このフレームの残り時間
            
            if (sleepTime <= 0) {
                sleepTime = 5;
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            beforeTime = System.currentTimeMillis();
        }
        MidiEngine.stop();  // 音楽を止める
        System.exit(0);  // ゲームループをぬけたら終了
    }
    
    /*
     * ゲームループを起動
     */
    private void startGame() {
        if (gameLoop == null || !running) {
            gameLoop = new Thread(this);
            gameLoop.start();
        }
    }

    /*
     * ゲームを停止
     */
    public void stopGame() {
        running = false;
    }

    /**
     * ゲーム状態の更新
     */
    private void gameUpdate() {
        switch (gameState) {
            case GameState.TITLE:
                // タイトル画面のキー入力を処理する
                titleProcessInput();
                break;
            case GameState.MAIN:
                // メイン画面のキー入力を処理する
                mainProcessInput();
                // サブウィンドウが表示されていないとき移動可能
                if (!messageWindow.isVisible() && !commandWindow.isVisible()) {
                    // 勇者の移動処理
                    heroMove();
                    // キャラクターの移動処理
                    charaMove();
                }
                break;
            case GameState.BATTLE:
                MidiEngine.play(Sound.BATTLE);
                // 戦闘画面のキー入力を処理する
                battleProcessInput();
                break;
            case GameState.GAMEOVER:
                break;
        }
    }

    /**
     * スクリーンに描画
     */
    private void screenUpdate() {
        try {
            gScr = bufferStrategy.getDrawGraphics();
            gameRender(gScr);  // レンダリング
            gScr.dispose();
            if (!bufferStrategy.contentsLost()) {
                bufferStrategy.show();
            } else {
                System.out.println("Contents Lost");
            }
            Toolkit.getDefaultToolkit().sync();
        } catch (Exception e) {
            e.printStackTrace();
            running = false;
        }
    }

    /**
     * 画像のレンダリング
     */
    private void gameRender(Graphics gScr) {
        switch (gameState) {
            case GameState.TITLE:
                titleRender();
                break;
            case GameState.MAIN:
                mainRender();
                break;
            case GameState.BATTLE:
                battleRender();
                break;
            case GameState.GAMEOVER:
                gameOverRender();
                break;
        }
    }

    private void titleRender() {
        gScr.setColor(Color.BLACK);
        gScr.fillRect(0, 0, WIDTH, HEIGHT);
        
        gScr.setColor(Color.WHITE);
        gScr.drawString("タイトル画面（スペースキーを押してね）", 30, 30);
    }

    private void mainRender() {
        // スクリーンのグラフィックオブジェクトgScrを使って描画する
        gScr.setColor(Color.WHITE);
        gScr.fillRect(0, 0, WIDTH, HEIGHT);

        // X方向のオフセットを計算
        int offsetX = WIDTH / 2 - hero.getPx();
        // マップの端ではスクロールしないようにする
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, WIDTH - maps[mapNo].getWidth());
        
        // Y方向のオフセットを計算
        int offsetY = HEIGHT / 2 - hero.getPy();
        // マップの端ではスクロールしないようにする
        offsetY = Math.min(offsetY, 0);
        offsetY = Math.max(offsetY, HEIGHT - maps[mapNo].getHeight());

        // マップを描く
        // キャラクターはマップが描いてくれる
        maps[mapNo].draw(gScr, offsetX, offsetY);
        
        // サブウィンドウを描画
        // isVisibleがtrueのときだけ描画される
        messageWindow.draw(gScr);
        commandWindow.draw(gScr);

        if (DEBUG_MODE) {
            gScr.setColor(Color.WHITE);
            gScr.drawString("スクリーンサイズ: " + WIDTH + " " + HEIGHT, 450, 32);
            gScr.drawString("マップサイズ: " + maps[mapNo].getCol() + " " + maps[mapNo].getRow(), 450, 48);
            gScr.drawString("キャラクター座標: " + hero.getX() + " " + hero.getY(), 450, 64);
        }
    }

    private void battleRender() {
        // TODO: バトルクラスのdraw()を呼び出すように書き換える
        gScr.setColor(Color.BLACK);
        gScr.fillRect(0, 0, WIDTH, HEIGHT);
        
        gScr.setColor(Color.WHITE);
        gScr.drawString("戦闘画面になる予定（スペースを押してね）", 30, 30);
        
        gScr.drawImage(enemyImage, 314, 150, null);
    }
    
    private void gameOverRender() {
        
    }

    /**
     * 勇者の移動処理
     */
    private void heroMove() {
        // 移動（スクロール）中なら移動する
        if (hero.isMoving()) {
            if (hero.move()) {  // 移動（スクロール）
                // 移動が完了した後の処理はここに書く
                // 足元にバリアーがあれば音を鳴らす
                if (maps[mapNo].getMapChip(hero.getX(), hero.getY()) == Chipset.BARRIER) {
                    WaveEngine.play(Sound.BARRIER);
                    // TODO:画面フラッシュ
                }

                // 移動イベント
                // イベントがあるかチェック
                Event event = maps[mapNo].eventCheck(hero.getX(), hero.getY());
                if (event instanceof MoveEvent) {  // 移動イベントなら
                    if (event.chipNo == Chipset.DOWNSTAIRS || event.chipNo == Chipset.UPSTAIRS) {  // 階段
                        WaveEngine.play(Sound.STAIRS);
                    } else if (event.chipNo == Chipset.WARP) {  // 旅のとびら
                        WaveEngine.play(Sound.WARP);
                        
                        // TODO:旅のとびらのエフェクト

                    }
                    MoveEvent m = (MoveEvent)event;
                    // 移動元マップの勇者を消去
                    maps[mapNo].removeChara(hero);
                    // 現在のマップ番号に移動先のマップ番号を設定
                    mapNo = m.destMapNo;
                    // 移動先マップでの座標を取得して勇者を作り直す
                    hero = new Chara(m.destX, m.destY, 0, Direction.DOWN, 0, maps[mapNo]);
                    // 移動先マップに勇者を登録
                    maps[mapNo].addChara(hero);
                    // BGM
                    MidiEngine.play(maps[mapNo].getBgmNo());
                }
            }
        }
    }
    
    /**
     * タイトル画面のキー入力を処理する
     */
    private void titleProcessInput() {
        if (spaceKey.isPressed()) {
            gameState = GameState.MAIN;  // メイン画面へ
        }
    }

    /**
     * メイン画面のキー入力を処理する
     */
    private void mainProcessInput() {
        // 一番上にある表示されているウィンドウのキー処理が優先される
        if (messageWindow.isVisible()) {  // メッセージウィンドウ表示中
            messageWindowProcessInput();
        } else if (commandWindow.isVisible()) {  // コマンドウィンドウを表示中
            commandWindowProcessInput();
        } else {  // メイン画面
            mainWindowProcessInput();
        }
    }

    /**
     * メッセージウィンドウが表示されているときのキー処理
     */
    private void messageWindowProcessInput() {
        if (spaceKey.isPressed()) {  // スペースキー
            // 次のメッセージへ
            if (messageWindow.nextMessage()) {
                // メッセージが終了したらメッセージウィンドウを隠す
                messageWindow.hide();
            }
        }
        // キーリセット
        // これがないとウィンドウを閉じたときに移動してしまう
        leftKey.reset();
        rightKey.reset();
        upKey.reset();
        downKey.reset();
    }

    /**
     * コマンドウィンドウが表示されているときのキー処理
     */
    private void commandWindowProcessInput() {
        if (cmdLeftKey.isPressed()) {
            commandWindow.leftCursor();
        }
        if (cmdRightKey.isPressed()) {
            commandWindow.rightCursor();
        }
        if (cmdUpKey.isPressed()) {
            commandWindow.upCursor();
        }
        if (cmdDownKey.isPressed()) {
            commandWindow.downCursor();
        }
        if (spaceKey.isPressed()) {  // スペースキー
            commandWindow.hide();
            WaveEngine.play(Sound.BEEP);
            switch (commandWindow.getSelectedCmdNo()) {
                case CommandWindow.TALK:  // はなす
                    Chara chara = hero.talkWith();
                    if (chara != null) {
                        // メッセージをセットする
                        messageWindow.setMessage(chara.getMessage());
                        // メッセージウィンドウを表示
                        messageWindow.show();
                    } else {
                        messageWindow.setMessage("そのほうこうには　だれもいない。");
                        messageWindow.show();
                    }
                    break;
                case CommandWindow.STATUS:  // つよさ
                    break;
                case CommandWindow.EQUIPMENT:  // そうび
                    break;
                case CommandWindow.DOOR:  // とびら
                    DoorEvent door = hero.open();
                    if (door != null) {
                        WaveEngine.play(Sound.DOOR);    
                        // ドアを削除
                        maps[mapNo].removeEvent(door);
                    }
                    break;
                case CommandWindow.SPELL:  // じゅもん
                    WaveEngine.play(Sound.SPELL);
                    break;
                case CommandWindow.ITEM:  // どうぐ
                    break;
                case CommandWindow.TACTICS:  // さくせん
                    // 戦闘画面テスト
                    gameState = GameState.BATTLE;
                    break;
                case CommandWindow.SEARCH:  // しらべる
                    Event event = hero.search();
                    if (event instanceof TreasureEvent) {  // 宝箱
                        TreasureEvent treasure = (TreasureEvent)event; 
                        WaveEngine.play(Sound.TREASURE);
                        // メッセージをセットする
                        messageWindow.setMessage(treasure.getItemName() + "を　てにいれた。");

                        // TODO:ここにアイテム入手処理を入れる

                        // メッセージウィンドウを表示
                        messageWindow.show();
                        // 宝箱を削除
                        maps[mapNo].removeEvent(treasure);
                        break;
                    } else if (event instanceof MessageEvent) {  // メッセージ
                        MessageEvent me = (MessageEvent)event;
                        // メッセージをセットする
                        messageWindow.setMessage(me.getMessage());
                        // メッセージウィンドウを表示
                        messageWindow.show();
                        // メッセージイベントは消去しない
                    } else if (event instanceof MessageBoardEvent) {  // 立て札
                        MessageBoardEvent board = (MessageBoardEvent)event;

                        // TODO: ここで書き込みなどできたらいいなぁ

                        // メッセージをセットする
                        messageWindow.setMessage(board.getMessage());
                        // メッセージウィンドウを表示
                        messageWindow.show();
                    } else {
                        messageWindow.setMessage("しかし　なにも　みつからなかった。");
                        messageWindow.show();
                    }
            }
        }
    }

    /**
     * メイン画面でのキー処理
     */
    private void mainWindowProcessInput() {
        if (leftKey.isPressed()) { // 左
            if (!hero.isMoving()) {       // 移動中でなければ
                hero.setDirection(Direction.LEFT);  // 方向をセットして
                hero.setMoving(true);     // 移動（スクロール）開始
            }
        }
        if (rightKey.isPressed()) { // 右
            if (!hero.isMoving()) {
                hero.setDirection(Direction.RIGHT);
                hero.setMoving(true);
            }
        }
        if (upKey.isPressed()) { // 上
            if (!hero.isMoving()) {
                hero.setDirection(Direction.UP);
                hero.setMoving(true);
            }
        }
        if (downKey.isPressed()) { // 下
            if (!hero.isMoving()) {
                hero.setDirection(Direction.DOWN);
                hero.setMoving(true);
            }
        }
        if (spaceKey.isPressed()) {  // スペース
            commandWindow.show();
        }
    }

    /**
     * 戦闘画面の入力を処理する
     */
    private void battleProcessInput() {
        if (spaceKey.isPressed()) {
            WaveEngine.play(Sound.WINBATTLE);
            MidiEngine.play(maps[mapNo].getBgmNo());
            gameState = GameState.MAIN;
        }
    }

    /**
     * 勇者以外のキャラクターの移動処理
     */
    private void charaMove() {
        // マップにいるキャラクターを取得
        Vector charas = maps[mapNo].getCharas();
        for (int i=0; i<charas.size(); i++) {
            Chara chara = (Chara)charas.get(i);
            // キャラクターの移動タイプを調べる
            if (chara.getMoveType() == 1) {  // 移動するタイプなら
                if (chara.isMoving()) {  // 移動中なら
                    chara.move();  // 移動する
                } else if (rand.nextDouble() < Chara.PROB_MOVE) {
                    // 移動してない場合はChara.PROB_MOVEの確率で再移動する
                    // 方向はランダムに決める
                    chara.setDirection(rand.nextInt(4));
                    chara.setMoving(true);
                }
            }
        }
    }

    /**
     * サウンドをロード
     */
    private void loadSound() {
        // BGMをロード
        for (int i = 0; i < bgmNames.length; i++) {
            try {
                MidiEngine.load("bgm/" + bgmNames[i] + ".mid");
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 効果音をロード
        for (int i = 0; i < seNames.length; i++) {
            try {
                WaveEngine.load("se/" + seNames[i] + ".wav");
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * キーが押されたらキーの状態を「押された」に変える
     * 
     * @param e キーイベント
     */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // 終了判定
        if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_Q ||
                keyCode == KeyEvent.VK_END ||
                (keyCode == KeyEvent.VK_C && e.isControlDown())) {
            running = false;
        }

        if (keyCode == KeyEvent.VK_LEFT) {
            if (commandWindow.isVisible()) {
                cmdLeftKey.press();
            } else {
                leftKey.press();
            }
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            if (commandWindow.isVisible()) {
                cmdRightKey.press();
            } else {
                rightKey.press();
            }
        }
        if (keyCode == KeyEvent.VK_UP) {
            if (commandWindow.isVisible()) {
                cmdUpKey.press();
            } else {
                upKey.press();
            }
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            if (commandWindow.isVisible()) {
                cmdDownKey.press();
            } else {
                downKey.press();
            }
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            spaceKey.press();
        }
    }

    /**
     * キーが離されたらキーの状態を「離された」に変える
     * 
     * @param e キーイベント
     */
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            leftKey.release();
            cmdLeftKey.release();
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            rightKey.release();
            cmdRightKey.release();
        }
        if (keyCode == KeyEvent.VK_UP) {
            upKey.release();
            cmdUpKey.release();
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            downKey.release();
            cmdDownKey.release();
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            spaceKey.release();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        new DQ();
    }
}