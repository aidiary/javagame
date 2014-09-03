import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

/*
 * Created on 2005/05/13
 *
 */

/**
 * @author mori
 *
 */
public class MainPanel extends JPanel implements Runnable, KeyListener {
    // パネルサイズ
    private static final int WIDTH = 640;
    private static final int HEIGHT = 640;

    // グリッドサイズ
    public static final int GS = 32;

    // 行数、列数
    public static final int ROW = HEIGHT / GS;
    public static final int COL = WIDTH / GS;

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
        // 獲物
        prey = new Prey(10, 10, predator);

        thread = new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 格子を描く
        g.setColor(Color.BLACK);
        for (int i=1; i<COL; i++) {  // 縦線
            g.drawLine(i*GS, 0, i*GS, HEIGHT);
        }
        for (int i=1; i<ROW; i++) {  // 横線
            g.drawLine(0, i*GS, WIDTH, i*GS);
        }
        g.drawRect(0, 0, WIDTH, HEIGHT);  // 外枠
        
        // 追跡者・獲物を描画
        predator.draw(g);
        prey.draw(g);
    }

    public void run() {
        while (true) {
            // 逃げる
            prey.escape();
            
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
     * 012
     * 345
     * 678
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP :
                predator.move(1);
                break;
            case KeyEvent.VK_DOWN :
                predator.move(7);
                break;
            case KeyEvent.VK_LEFT :
                predator.move(3);
                break;
            case KeyEvent.VK_RIGHT :
                predator.move(5);
                break;
        }
        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }
}
