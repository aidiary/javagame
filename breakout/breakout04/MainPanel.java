import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/*
 * Created on 2007/05/05
 * 
 * ブロック崩しのゲーム画面用パネル
 */

public class MainPanel extends JPanel implements Runnable, MouseMotionListener {
    public static final int WIDTH = 360;
    public static final int HEIGHT = 480;

    // ブロックの行数
    private static final int NUM_BLOCK_ROW = 10;
    // ブロックの列数
    private static final int NUM_BLOCK_COL = 7;
    // ブロック数
    private static final int NUM_BLOCK = NUM_BLOCK_ROW * NUM_BLOCK_COL;

    private Racket racket; // ラケット
    private Ball ball; // ボール
    private Block[] block; // ブロック

    private Thread gameLoop; // ゲームループ

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addMouseMotionListener(this);

        racket = new Racket();
        ball = new Ball();
        block = new Block[NUM_BLOCK];

        // ブロックを並べる
        for (int i = 0; i < NUM_BLOCK_ROW; i++) {
            for (int j = 0; j < NUM_BLOCK_COL; j++) {
                int x = j * Block.WIDTH + Block.WIDTH;
                int y = i * Block.HEIGHT + Block.HEIGHT;
                block[i * NUM_BLOCK_COL + j] = new Block(x, y);
            }
        }

        gameLoop = new Thread(this);
        gameLoop.start();
    }

    /**
     * ゲームループ
     * 
     */
    public void run() {
        while (true) {
            // ボールの移動
            ball.move();

            // もしラケットと衝突したらボールをバウンド
            if (racket.collideWith(ball)) {
                ball.boundY();
            }

            // ブロックとボールの衝突処理
            for (int i = 0; i < NUM_BLOCK; i++) {
                // すでに消えているブロックは無視
                if (block[i].isDeleted())
                    continue;
                // ブロックの当たった位置を計算
                int collidePos = block[i].collideWith(ball);
                if (collidePos != Block.NO_COLLISION) { // ブロックに当たっていたら
                    block[i].delete();
                    // ボールの当たった位置からボールの反射方向を計算
                    switch (collidePos) {
                        case Block.DOWN :
                        case Block.UP :
                            ball.boundY();
                            break;
                        case Block.LEFT :
                        case Block.RIGHT :
                            ball.boundX();
                            break;
                        case Block.UP_LEFT :
                        case Block.UP_RIGHT :
                        case Block.DOWN_LEFT :
                        case Block.DOWN_RIGHT :
                            ball.boundXY();
                            break;
                    }
                    break; // 1回に壊せるブロックは1つ
                }
            }

            repaint();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 背景
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        racket.draw(g); // ラケット
        ball.draw(g); // ボール

        // ブロック
        for (int i = 0; i < NUM_BLOCK; i++) {
            if (!block[i].isDeleted()) {
                block[i].draw(g);
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        int x = e.getX(); // マウスのX座標
        racket.move(x); // ラケットを移動
        repaint();
    }

    public void mouseDragged(MouseEvent e) {
    }
}
