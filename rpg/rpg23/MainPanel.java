import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

/*
 * Created on 2006/5/5
 *
 */

/**
 * @author mori
 * 
 */
class MainPanel extends JPanel implements KeyListener, Runnable, Common {
    // パネルサイズ
    public static final int WIDTH = 480;
    public static final int HEIGHT = 480;

    // デバッグモード（trueだと座標などが表示される）
    private static final boolean DEBUG_MODE = true;

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

    // ゲームループ
    private Thread gameLoop;

    // 乱数生成器
    private Random rand = new Random();

    // ウィンドウ
    private MessageWindow messageWindow;
    // ウィンドウを表示する領域
    private static Rectangle WND_RECT = new Rectangle(62, 324, 356, 140);

    // BGM名（from TAM Music Factory: http://www.tam-music.com/）
    // BGM番号は0, 1, ・・・というように割り当てられる
    private static final String[] bgmNames = {"tamhe07.mid", "tamsu02.mid"};

    // サウンド名
    // サウンド番号は0, 1,・・・というように割り当てられる
    private static final String[] soundNames = {"treasure.wav", "door.wav",
            "step.wav"};

    public MainPanel() {
        // パネルの推奨サイズを設定
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // パネルがキー操作を受け付けるように登録する
        setFocusable(true);
        addKeyListener(this);

        // アクションキーを作成
        leftKey = new ActionKey();
        rightKey = new ActionKey();
        upKey = new ActionKey();
        downKey = new ActionKey();
        spaceKey = new ActionKey(ActionKey.DETECT_INITIAL_PRESS_ONLY);

        // マップを作成（マップで鳴らすBGM番号を渡す）
        maps = new Map[2];
        // 王の間
        maps[0] = new Map("map/king_room.map", "event/king_room.evt", 0, this);
        // フィールド
        maps[1] = new Map("map/field.map", "event/field.evt", 1, this);

        // 最初は王の間
        mapNo = 0;

        // 勇者を作成
        hero = new Chara(4, 4, 0, DOWN, 0, maps[mapNo]);

        // マップにキャラクターを登録
        // キャラクターはマップに属す
        maps[mapNo].addChara(hero);

        // ウィンドウを追加
        messageWindow = new MessageWindow(WND_RECT);

        // サウンドをロード
        loadSound();

        // マップに割り当てられたBGMを再生
        MidiEngine.play(maps[mapNo].getBgmNo());

        // ゲームループ開始
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // X方向のオフセットを計算
        int offsetX = MainPanel.WIDTH / 2 - hero.getPx();
        // マップの端ではスクロールしないようにする
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, MainPanel.WIDTH - maps[mapNo].getWidth());

        // Y方向のオフセットを計算
        int offsetY = MainPanel.HEIGHT / 2 - hero.getPy();
        // マップの端ではスクロールしないようにする
        offsetY = Math.min(offsetY, 0);
        offsetY = Math.max(offsetY, MainPanel.HEIGHT - maps[mapNo].getHeight());

        // マップを描く
        // キャラクターはマップが描いてくれる
        maps[mapNo].draw(g, offsetX, offsetY);

        // メッセージウィンドウを描画
        messageWindow.draw(g);
        
        if (DEBUG_MODE) {
            Font font = new Font("SansSerif", Font.BOLD, 16);
            g.setFont(font);
            g.setColor(Color.YELLOW);
            g.drawString(maps[mapNo].getMapName() + " (" + maps[mapNo].getCol() + "," + maps[mapNo].getRow() + ")", 4, 16);
            g.drawString("(" + hero.getX() + "," + hero.getY() + ") ", 4, 32);
            g.drawString("(" + hero.getPx() + "," + hero.getPy() + ")", 4, 48);
        }
    }

    public void run() {
        while (true) {
            // キー入力をチェックする
            if (messageWindow.isVisible()) { // メッセージウィンドウ表示中
                messageWindowCheckInput();
            } else { // メイン画面
                mainWindowCheckInput();
            }

            if (!messageWindow.isVisible()) {
                // 勇者の移動処理
                heroMove();
                // キャラクターの移動処理
                charaMove();
            }

            WaveEngine.render();

            repaint();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * メインウィンドウでのキー入力をチェックする
     */
    private void mainWindowCheckInput() {
        if (leftKey.isPressed()) { // 左
            if (!hero.isMoving()) { // 移動中でなければ
                hero.setDirection(LEFT); // 方向をセットして
                hero.setMoving(true); // 移動（スクロール）開始
            }
        }
        if (rightKey.isPressed()) { // 右
            if (!hero.isMoving()) {
                hero.setDirection(RIGHT);
                hero.setMoving(true);
            }
        }
        if (upKey.isPressed()) { // 上
            if (!hero.isMoving()) {
                hero.setDirection(UP);
                hero.setMoving(true);
            }
        }
        if (downKey.isPressed()) { // 下
            if (!hero.isMoving()) {
                hero.setDirection(DOWN);
                hero.setMoving(true);
            }
        }
        if (spaceKey.isPressed()) { // スペース
            // 移動中は表示できない
            if (hero.isMoving())
                return;

            // しらべる
            TreasureEvent treasure = hero.search();
            if (treasure != null) {
                // かちゃ
                WaveEngine.play(0);
                // メッセージをセットする
                messageWindow.setMessage(treasure.getItemName() + "を　てにいれた。");
                // メッセージウィンドウを表示
                messageWindow.show();
                // TODO: ここにアイテム入手処理を入れる
                // 宝箱を削除
                maps[mapNo].removeEvent(treasure);
                return; // しらべた場合ははなさない
            }

            // とびら
            DoorEvent door = hero.open();
            if (door != null) {
                // ぎー
                WaveEngine.play(1);
                // ドアを削除
                maps[mapNo].removeEvent(door);

                return;
            }

            // はなす
            if (!messageWindow.isVisible()) { // メッセージウィンドウを表示
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
            }
        }
    }

    /**
     * メッセージウィンドウでのキー入力をチェックする
     */
    private void messageWindowCheckInput() {
        if (spaceKey.isPressed()) {
            if (messageWindow.nextMessage()) { // 次のメッセージへ
                messageWindow.hide(); // 終了したら隠す
            }
        }
    }

    /**
     * 勇者の移動処理
     */
    private void heroMove() {
        // 移動（スクロール）中なら移動する
        if (hero.isMoving()) {
            if (hero.move()) { // 移動（スクロール）
                // 移動が完了した後の処理はここに書く

                // 移動イベント
                // イベントがあるかチェック
                Event event = maps[mapNo].eventCheck(hero.getX(), hero.getY());
                if (event instanceof MoveEvent) { // 移動イベントなら
                    MoveEvent m = (MoveEvent) event;
                    // ざっざっざっ
                    WaveEngine.play(2);
                    // 移動元マップの勇者を消去
                    maps[mapNo].removeChara(hero);
                    // 現在のマップ番号に移動先のマップ番号を設定
                    mapNo = m.destMapNo;
                    // 移動先マップでの座標を取得して勇者を作り直す
                    hero = new Chara(m.destX, m.destY, 0, DOWN, 0, maps[mapNo]);
                    // 移動先マップに勇者を登録
                    maps[mapNo].addChara(hero);
                    // 移動先マップのBGMを鳴らす
                    MidiEngine.play(maps[mapNo].getBgmNo());
                }
            }
        }
    }

    /**
     * 勇者以外のキャラクターの移動処理
     */
    private void charaMove() {
        // マップにいるキャラクターを取得
        Vector charas = maps[mapNo].getCharas();
        for (int i = 0; i < charas.size(); i++) {
            Chara chara = (Chara) charas.get(i);
            // キャラクターの移動タイプを調べる
            if (chara.getMoveType() == 1) { // 移動するタイプなら
                if (chara.isMoving()) { // 移動中なら
                    chara.move(); // 移動する
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
     * キーが押されたらキーの状態を「押された」に変える
     * 
     * @param e キーイベント
     */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            leftKey.press();
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            rightKey.press();
        }
        if (keyCode == KeyEvent.VK_UP) {
            upKey.press();
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            downKey.press();
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
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            rightKey.release();
        }
        if (keyCode == KeyEvent.VK_UP) {
            upKey.release();
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            downKey.release();
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            spaceKey.release();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
     * サウンドをロードする
     */
    private void loadSound() {
        // BGMをロード
        for (int i = 0; i < bgmNames.length; i++) {
            try {
                MidiEngine.load("bgm/" + bgmNames[i]);
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // サウンドをロード
        for (int i = 0; i < soundNames.length; i++) {
            try {
                WaveEngine.load("sound/" + soundNames[i]);
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }
}
