import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

/*
 * Created on 2006/04/22
 */

public class MainPanel extends JPanel implements KeyListener {
    // パネルサイズ
    public static final int WIDTH = 192;
    public static final int HEIGHT = 416;

    // フィールド
    private Field field;
    // ブロック
    private Block block;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // パネルがキー入力を受け付けるようにする
        setFocusable(true);

        field = new Field();
        block = new Block();

        addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // フィールドを描画
        field.draw(g);
        // ブロックを描画
        block.draw(g);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) { // ブロックを左へ移動
            block.move(Block.LEFT);
        } else if (key == KeyEvent.VK_RIGHT) { // ブロックを右へ移動
            block.move(Block.RIGHT);
        } else if (key == KeyEvent.VK_DOWN) { // ブロックを下へ移動
            block.move(Block.DOWN);
        } else if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) { // ブロックを回転
            block.turn();
        } else if (key == KeyEvent.VK_N) {  // バーブロックを表示
            block.createBarBlock();
        }

        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }
}
