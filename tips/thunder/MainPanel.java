import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

import javax.swing.JPanel;

/*
 * Created on 2006/02/04
 */

/**
 * @author mori
 */
public class MainPanel extends JPanel
        implements
            Runnable,
            MouseListener,
            MouseMotionListener {
    // パネルサイズ
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private static final int MAX_THUNDERS = 16;

    private static Random rand = new Random(System.currentTimeMillis());

    // 火球が膨らむスピード
    private static final double SWELLING_SPEED = 0.2;
    // 火球の最大サイズ
    private static final double MAX_SIZE = 15.0;

    private Thunder[] thunder;

    // 火球の大きさ
    private double size;
    // マウスが押されているか
    private boolean isPressed;

    // マウスの位置
    private int mouseX;
    private int mouseY;

    private Thread thread;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        thunder = new Thunder[MAX_THUNDERS];
        for (int i = 0; i < MAX_THUNDERS; i++) {
            thunder[i] = new Thunder();
        }

        size = 0.0;
        isPressed = false;

        addMouseListener(this);
        addMouseMotionListener(this);

        thread = new Thread(this);
        thread.start();
    }

    /**
     * ゲームループ
     */
    public void run() {
        while (true) {
            // ボタンが押されている間火球を膨らませる
            if (isPressed && size < MAX_SIZE) {
                size += SWELLING_SPEED;
            }

            for (int i = 0; i < MAX_THUNDERS; i++) {
                if (thunder[i].isUsed()) {
                    thunder[i].move();
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

        // 背景を黒で塗りつぶす
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 火球を描画
        if (isPressed) {
            g.setColor(Color.WHITE);
            int tx = (int) (mouseX - size / 2);
            int ty = (int) (mouseY - size / 2);
            g.fillOval(tx, ty, (int) size, (int) size);
        }

        for (int i = 0; i < MAX_THUNDERS; i++) {
            if (thunder[i].isUsed()) {
                thunder[i].draw(g);
            }
        }
    }

    /*
     * ランダムな位置にいっせいに落とす
     */
    public void burst() {
        for (int i = 0; i < MAX_THUNDERS; i++) {
            int x = rand.nextInt(WIDTH);
            thunder[i].lightning(x, 0, 1.0);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        isPressed = true;
    }

    public void mouseReleased(MouseEvent e) {
        if (size > 14) {
            burst(); // バースト！
        } else {
            for (int i = 0; i < MAX_THUNDERS; i++) {
                if (!thunder[i].isUsed()) { // 使われていないオブジェクトを検索
                    thunder[i].lightning(mouseX, mouseY, size); // サンダー！
                    break;
                }
            }
        }

        mouseX = 0;
        mouseY = 0;
        size = 0.0;
        isPressed = false;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
    }
}