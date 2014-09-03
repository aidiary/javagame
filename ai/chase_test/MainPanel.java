/*
 * Created on 2005/01/16
 *
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel implements Runnable, KeyListener {
    // パネルサイズ
    private static final int WIDTH = 640;
    private static final int HEIGHT = 640;
    // グリッドサイズ
    private static final int GS = 8;
    // 行数、列数
    public static final int ROW = HEIGHT / GS;
    public static final int COL = WIDTH / GS;
    // 方向定数
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;

    // 追跡者
    private Predator predator;
    // 獲物
    private Prey prey;

    // スレッド
    private Thread thread;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        // 追跡者
        predator = new Predator(0, 0);
        // 獲物、あなたですよ（笑）
        prey = new Prey(50, 70);

        thread = new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 追跡者・獲物を描画
        predator.draw(g);
        prey.draw(g);
    }

    public void run() {
        while (true) {
            // 追跡者は獲物を追いかける
            predator.chase(prey);

            repaint();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
     * キー操作で獲物を動かす
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP :
                prey.move(UP);
                break;
            case KeyEvent.VK_DOWN :
                prey.move(DOWN);
                break;
            case KeyEvent.VK_LEFT :
                prey.move(LEFT);
                break;
            case KeyEvent.VK_RIGHT :
                prey.move(RIGHT);
                break;
        }
        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }
}