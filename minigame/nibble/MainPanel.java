/*
 * 作成日: 2004/12/14
 *
 */
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
/**
 * メインパネル
 * 
 * @author mori
 *  
 */
public class MainPanel extends JPanel implements Runnable, KeyListener {
    // フィールドの行数
    public static final int ROW = 30;
    // フィールドの列数
    public static final int COL = 40;
    // フィールドのグリッドサイズ
    public static final int GS = 16;
    // ゲーム状態：スタート画面
    public static final int START = 0;
    // ゲーム状態：プレイ画面
    public static final int PLAY = 1;
    // ゲーム状態：ゲームオーバー画面
    public static final int GAMEOVER = 2;

    // ゲーム状態
    private int gameState;
    // スコア
    private int score;
    // 残り時間
    private int limit;

    // 蛇
    private Snake snake;
    // 蛙の数
    private static final int NUM_TOAD = 50;
    // 蛙
    private Toad[] toad;

    // 乱数発生器
    private Random rand = new Random();
    // スレッド
    private Thread thread;
    // InfoPanelへの参照
    private InfoPanel infoPanel;
    
    // タイトル背景
    private Image titleImage;
    // ゲームオーバー背景
    private Image gameoverImage;
    
    /**
     * コンストラクタ
     *  
     */
    public MainPanel(InfoPanel infoPanel) {
        // パネルの大きさ（pack()するときに必要）
        setPreferredSize(new Dimension(640, 480));
        this.infoPanel = infoPanel;
        // イメージをロードする
        loadImage();
        // キーリスナーを登録
        addKeyListener(this);
        // ゲーム状態をSTARTに設定
        gameState = START;
    }

    /**
     * ゲームを初期化する
     *  
     */
    public void newGame() {
        snake = new Snake(10, 7, this);
        toad = new Toad[NUM_TOAD];
        for (int i = 0; i < NUM_TOAD; i++) {
            makeToad(i);
        }
        score = 0;
        limit = 600;
        thread = new Thread(this);
        thread.start();
        gameState = PLAY;
    }

    /**
     * ゲームループ
     */
    public void run() {
        while (true) {
            // PLAY状態以外は何もしない
            if (gameState != PLAY)
                break;
            // ターンごとに蛇のサイズ分だけ得点が加えられる
            score += snake.getSize();
            // 残り時間を減らす
            limit--;
            // 残り時間がなくなったらゲームオーバー
            if (limit < 0) {
                gameOver();
            }

            // 蛇の向いている方向へ移動
            // 蛇の向きはkeyPressed()でセットされる
            // キーを押さないとずっと同じ向きへ移動する
            snake.move();
            // 蛙を移動
            for (int i = 0; i < NUM_TOAD; i++) {
                toad[i].move();
            }
            // 蛇が画面の外へ出たらゲームオーバー
            if (snake.isOutOfField()) {
                gameOver();
                break;
            }
            // 蛇が自分の身体に触れたらゲームオーバー
            if (snake.touchOwnBody()) {
                gameOver();
                break;
            }
            // 蛇が蛙を食べたかどうかチェックする
            // 食べたら蛇の身体は伸びる
            for (int i = 0; i < NUM_TOAD; i++) {
                if (snake.eat(toad[i])) {
                    // ゲコ～
                    toad[i].croak();
                    // i番目は食べられちゃったので新しく作る
                    makeToad(i);
                }
            }
            // インフォメーションパネルに情報を表示する
            infoPanel.setLengthLabel(snake.getSize() + "");
            infoPanel.setScoreLabel(score + "");
            infoPanel.setTimeLabel(limit + "");

            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // ゲームオーバーになってbreakでwhileループをぬけたときにも
        // 再描画されるように
        repaint();
    }

    /**
     * JPanelがキーボードを受け付けるために必要
     *  
     */
    public boolean isFocusable() {
        // パネルがキーボードを受け付けるようにする
        return true;
    }

    /**
     * キー操作 蛇の向きをセットする
     */
    public void keyPressed(KeyEvent event) {
        // ゲーム状態に応じてキーを処理する
        if (gameState == START) {
            // START場面でスペースを押すとゲームが始まる
            if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                gameState = PLAY;
                // 新規ゲーム
                newGame();
            }
        } else if (gameState == PLAY) {
            // PLAY場面なら蛇の方向をセットする
            int key = event.getKeyCode();
            int dir = snake.getDir();
            if (key == KeyEvent.VK_LEFT && dir != Snake.RIGHT) {
                // 右向いているときに左に移動はできない（身体にぶつかってしまう）
                snake.setDir(Snake.LEFT);
            } else if (key == KeyEvent.VK_DOWN && dir != Snake.UP) {
                snake.setDir(Snake.DOWN);
            } else if (key == KeyEvent.VK_UP && dir != Snake.DOWN) {
                snake.setDir(Snake.UP);
            } else if (key == KeyEvent.VK_RIGHT && dir != Snake.LEFT) {
                snake.setDir(Snake.RIGHT);
            }
        } else if (gameState == GAMEOVER) {
            // GAMEOVER場面ならスペースで最初に戻る
            if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                gameState = START;
            }
        }

        repaint();
    }

    public void keyReleased(KeyEvent event) {
    }
    public void keyTyped(KeyEvent event) {
    }

    /**
     * 描画処理
     * 
     * @param g グラフィックハンドル
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ゲーム状態に応じて画面を変える
        if (gameState == START) {
            g.drawImage(titleImage, 0, 0, this);
        } else if (gameState == PLAY) {
            clear(g);
            snake.draw(g);
            for (int i = 0; i < NUM_TOAD; i++) {
                toad[i].draw(g);
            }
        } else if (gameState == GAMEOVER) {
            g.drawImage(gameoverImage, 0, 0, this);
            g.setFont(new Font("SansSerif", Font.BOLD, 28));
            FontMetrics fm = g.getFontMetrics();
            g.setColor(Color.WHITE);
            String s = "YOUR LENGTH: " + snake.getSize();
            g.drawString(s, 
                    (this.getWidth() - fm.stringWidth(s)) / 2,
                    this.getHeight() / 2 + fm.getDescent());
            s = "YOUR SCORE: " + score;
            g.drawString(s,
                    (this.getWidth() - fm.stringWidth(s)) / 2,
                    this.getHeight() / 2 + fm.getDescent() + fm.getHeight());
        }
    }

    /**
     * n番目の蛙を新しく作成する
     * 
     * @param n
     */
    private void makeToad(int n) {
        // ランダムな位置
        Point p = new Point(rand.nextInt(COL), rand.nextInt(ROW));
        // 金:青:緑=1:4:5の比率で出るようにする
        double x = rand.nextDouble();
        if (x < 0.1) {
            toad[n] = new GoldToad(p, this);
        } else if (x < 0.5) {
            toad[n] = new BlueToad(p, this);
        } else {
            toad[n] = new GreenToad(p, this);
        }
    }
    
    /**
     * ゲームオーバー処理。
     *
     */
    private void gameOver() {
        // 最後にスコアが足されてしまうのでひいておく
        score -= snake.getSize();
        // ゲームオーバー状態へ
        gameState = GAMEOVER;
    }
    
    /**
     * 画面をクリアする
     * 
     * @param g グラフィックハンドル
     */
    private void clear(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize().width, getSize().height);
    }
    
    /**
     * イメージをロードする。
     *
     */
    private void loadImage() {
        MediaTracker tracker = new MediaTracker(this);
        titleImage = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("title.jpg"));
        gameoverImage = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("gameover.jpg"));
        tracker.addImage(titleImage, 0);
        tracker.addImage(gameoverImage, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}