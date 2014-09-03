import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

/*
 * Created on 2005/10/09
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

    // マップ
    private Map map;
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
    private static Rectangle WND_RECT =
        new Rectangle(62, 324, 356, 140);

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

        // マップを作成
        map = new Map("map/map.dat", "event/event.dat", this);

        // 勇者を作成
        hero = new Chara(4, 4, 0, DOWN, 0, map);

        // マップにキャラクターを登録
        // キャラクターはマップに属す
        map.addChara(hero);

        // ウィンドウを追加
        messageWindow = new MessageWindow(WND_RECT);

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
        offsetX = Math.max(offsetX, MainPanel.WIDTH - map.getWidth());
        
        // Y方向のオフセットを計算
        int offsetY = MainPanel.HEIGHT / 2 - hero.getPy();
        // マップの端ではスクロールしないようにする
        offsetY = Math.min(offsetY, 0);
        offsetY = Math.max(offsetY, MainPanel.HEIGHT - map.getHeight());

        // マップを描く
        // キャラクターはマップが描いてくれる
        map.draw(g, offsetX, offsetY);

        // メッセージウィンドウを描画
        messageWindow.draw(g);
    }

    public void run() {
        while (true) {
            // キー入力をチェックする
            if (messageWindow.isVisible()) {  // メッセージウィンドウ表示中
                messageWindowCheckInput();
            } else {  // メイン画面
                mainWindowCheckInput();
            }

            if (!messageWindow.isVisible()) {
                // 勇者の移動処理
                heroMove();
                // キャラクターの移動処理
                charaMove();
            }

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
            if (!hero.isMoving()) {       // 移動中でなければ
                hero.setDirection(LEFT);  // 方向をセットして
                hero.setMoving(true);     // 移動（スクロール）開始
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
        if (spaceKey.isPressed()) {  // スペース
            // 移動中は表示できない
            if (hero.isMoving()) return;

            // しらべる
            TreasureEvent treasure = hero.search();
            if (treasure != null) {
                // メッセージをセットする
                messageWindow.setMessage(treasure.getItemName() + "を　てにいれた。");
                // メッセージウィンドウを表示
                messageWindow.show();
                // TODO: ここにアイテム入手処理を入れる
                // 宝箱を削除
                map.removeEvent(treasure);
                return;  // しらべた場合ははなさない
            }

            // とびら
            DoorEvent door = hero.open();
            if (door != null) {
                // ドアを削除
                map.removeEvent(door);
                
                return;
            }

            // はなす
            if (!messageWindow.isVisible()) {  // メッセージウィンドウを表示
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
            if (messageWindow.nextMessage()) {  // 次のメッセージへ
                messageWindow.hide();  // 終了したら隠す
            }
        }
    }

    /**
     * 勇者の移動処理
     */
    private void heroMove() {
        // 移動（スクロール）中なら移動する
        if (hero.isMoving()) {
            if (hero.move()) {  // 移動（スクロール）
                // 移動が完了した後の処理はここに書く
            }
        }
    }

    /**
     * 勇者以外のキャラクターの移動処理
     */
    private void charaMove() {
        // マップにいるキャラクターを取得
        Vector charas = map.getCharas();
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
}
