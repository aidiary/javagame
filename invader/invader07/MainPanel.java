import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JPanel;

/*
 * Created on 2005/02/09
 *
 */

/**
 * メインパネル
 * 
 * @author mori
 *  
 */
public class MainPanel extends JPanel implements Runnable, KeyListener {
    // パネルサイズ
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    // 方向定数
    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    // 連続発射できる弾の数
    private static final int NUM_SHOT = 5;
    // 発射できる間隔（弾の充填時間）
    private static final int FIRE_INTERVAL = 300;

    // エイリアンの数
    private static final int NUM_ALIEN = 50;
    // ビームの数
    private static final int NUM_BEAM = 20;

    // プレイヤー
    private Player player;
    // 弾
    private Shot[] shots;
    // 最後に発射した時間
    private long lastFire = 0;
    // エイリアン
    private Alien[] aliens;
    // ビーム
    private Beam[] beams;

    // キーの状態（このキー状態を使ってプレイヤーを移動する）
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean firePressed = false;

    // ゲームループ用スレッド
    private Thread gameLoop;

    // 乱数発生器
    private Random rand;

    // サウンド
    private AudioClip fireSound;
    private AudioClip crySound;
    private AudioClip bombSound;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // パネルがキー入力を受け付けるようにする
        setFocusable(true);

        // ゲームの初期化
        initGame();

        rand = new Random();

        // サウンドをロード
        fireSound = Applet.newAudioClip(getClass().getResource("se/pi02.wav"));
        crySound = Applet.newAudioClip(getClass().getResource("se/pi00.wav"));
        bombSound = Applet.newAudioClip(getClass().getResource("se/bom28_a.wav"));

        // キーイベントリスナーを登録
        addKeyListener(this);

        // ゲームループ開始
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    /**
     * ゲームループ
     */
    public void run() {

        while (true) {
            move();

            // 発射ボタンが押されたら弾を発射
            if (firePressed) {
                tryToFire();
            }

            // エイリアンの攻撃
            alienAttack();

            // 衝突判定
            collisionDetection();

            // 再描画
            repaint();

            // 休止
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ゲームの初期化
     */
    private void initGame() {
        // プレイヤーを作成
        player = new Player(0, HEIGHT - 20, this);

        // 弾を作成
        shots = new Shot[NUM_SHOT];
        for (int i = 0; i < NUM_SHOT; i++) {
            shots[i] = new Shot(this);
        }

        // エイリアンを作成
        aliens = new Alien[NUM_ALIEN];
        for (int i = 0; i < NUM_ALIEN; i++) {
            aliens[i] = new Alien(20 + (i % 10) * 40, 20 + (i / 10) * 40, 3, this);
        }

        // ビームを作成
        beams = new Beam[NUM_BEAM];
        for (int i = 0; i < NUM_BEAM; i++) {
            beams[i] = new Beam(this);
        }
    }

    /**
     * 移動処理
     */
    private void move() {
        // プレイヤーを移動する
        // 何も押されていないときは移動しない
        if (leftPressed) {
            player.move(LEFT);
        } else if (rightPressed) {
            player.move(RIGHT);
        }

        // エイリアンを移動する
        for (int i = 0; i < NUM_ALIEN; i++) {
            aliens[i].move();
        }

        // 弾を移動する
        for (int i = 0; i < NUM_SHOT; i++) {
            shots[i].move();
        }

        // ビームを移動する
        for (int i = 0; i < NUM_BEAM; i++) {
            beams[i].move();
        }
    }

    /**
     * 弾を発射する
     */
    private void tryToFire() {
        // 前との発射間隔がFIRE_INTERVAL以下だったら発射できない
        if (System.currentTimeMillis() - lastFire < FIRE_INTERVAL) {
            return;
        }

        lastFire = System.currentTimeMillis();
        // 発射されていない弾を見つける
        for (int i = 0; i < NUM_SHOT; i++) {
            if (shots[i].isInStorage()) {
                // 弾が保管庫にあれば発射できる
                // 弾の座標をプレイヤーの座標にすれば発射される
                Point pos = player.getPos();
                shots[i].setPos(pos.x + player.getWidth() / 2, pos.y);
                // 発射音
                fireSound.play();
                // 1つ見つけたら発射してbreakでループをぬける
                break;
            }
        }
    }

    /**
     * エイリアンの攻撃
     */
    private void alienAttack() {
        // 1ターンでNUM_BEAMだけ発射する
        // つまりエイリアン1人になってもそいつがNUM_BEAM発射する
        for (int i = 0; i < NUM_BEAM; i++) {
            // エイリアンの攻撃
            // ランダムにエイリアンを選ぶ
            int n = rand.nextInt(NUM_ALIEN);
            // そのエイリアンが生きていればビーム発射
            if (aliens[n].isAlive()) {
                // 発射されていないビームを見つける
                // 1つ見つけたら発射してbreakでループをぬける
                for (int j = 0; j < NUM_BEAM; j++) {
                    if (beams[j].isInStorage()) {
                        // ビームが保管庫にあれば発射できる
                        // ビームの座標をエイリアンの座標にセットすれば発射される
                        Point pos = aliens[n].getPos();
                        beams[j].setPos(pos.x + aliens[n].getWidth() / 2, pos.y);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 衝突検出
     *  
     */
    private void collisionDetection() {
        // エイリアンと弾の衝突検出
        for (int i = 0; i < NUM_ALIEN; i++) {
            for (int j = 0; j < NUM_SHOT; j++) {
                if (aliens[i].collideWith(shots[j])) {
                    // i番目のエイリアンとj番目の弾が衝突
                    // エイリアンは死ぬ
                    aliens[i].die();
                    // 断末魔
                    crySound.play();
                    // 弾は保管庫へ（保管庫へ送らなければ貫通弾になる）
                    shots[j].store();
                    // エイリアンが死んだらもうループまわす必要なし
                    break;
                }
            }
        }
        
        // プレーヤーとビームの衝突検出
        for (int i = 0; i < NUM_BEAM; i++) {
            if (player.collideWith(beams[i])) {
                // 爆発音
                bombSound.play();
                // プレーヤーとi番目のビームが衝突
                // ビームは保管庫へ
                beams[i].store();
                // ゲームオーバー
                initGame();
            }
        }
    }

    /**
     * 描画処理
     * 
     * @param 描画オブジェクト
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 背景を黒で塗りつぶす
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // プレイヤーを描画
        player.draw(g);

        // エイリアンを描画
        for (int i = 0; i < NUM_ALIEN; i++) {
            aliens[i].draw(g);
        }

        // 弾を描画
        for (int i = 0; i < NUM_SHOT; i++) {
            shots[i].draw(g);
        }

        // ビームを描画
        for (int i = 0; i < NUM_BEAM; i++) {
            beams[i].draw(g);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
     * キーが押されたらキーの状態を「押された」に変える
     * 
     * @param e キーイベント
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            firePressed = true;
        }
    }

    /**
     * キーが離されたらキーの状態を「離された」に変える
     * 
     * @param e キーイベント
     */
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if (key == KeyEvent.VK_SPACE) {
            firePressed = false;
        }
    }
}