import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/*
 * Created on 2006/01/29
 */

/**
 * @author mori
 */
public class MainPanel extends JPanel
        implements
            Runnable,
            MouseListener,
            MouseMotionListener,
            KeyListener {
    // パネルサイズ
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private Point mousePos = new Point();
    private Thread thread;

    private Wizard wizard;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        addMouseListener(this);
        addMouseMotionListener(this);
        
        // パネルがキー入力を受け付けるようにする
        setFocusable(true);
        addKeyListener(this);

        wizard = new Wizard(WIDTH/2, HEIGHT/2, this);
        
        thread = new Thread(this);
        thread.start();
    }

    /**
     * ゲームループ
     */
    public void run() {
        while (true) {
            wizard.update();

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

        // Wizardを描画
        wizard.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        // マウスポインタがある場所へファイア
        wizard.fire(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        // マウスポインタの場所を記録
        mousePos.x = e.getX();
        mousePos.y = e.getY();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        // マウスポインタがある場所へファイア
        wizard.fire(e.getX(), e.getY());
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_T) {
            if (!wizard.isInTeleport()) {
                // マウスポインタがある場所へテレポート
                wizard.teleport(mousePos.x, mousePos.y);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}