import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

/*
 * Created on 2006/04/23
 */

public class MainPanel extends JPanel implements KeyListener, Runnable {
    // パネルサイズ
    public static final int WIDTH = 192;
    public static final int HEIGHT = 416;

    // フィールド
    private Field field;
    // ブロック
    private Block block;
    // 次のブロック
    private Block nextBlock;

    // ゲームループ用スレッド
    private Thread gameLoop;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // パネルがキー入力を受け付けるようにする
        setFocusable(true);

        field = new Field();
        block = new Block(field);

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
            // ブロックを下方向へ移動する
            boolean isFixed = block.move(Block.DOWN);
            if (isFixed) { // ブロックが固定されたら
                // 次のブロックをランダムに作成
                nextBlock = createBlock(field);
                block = nextBlock;
            }

            // ブロックがそろった行を消す
            field.deleteLine();

            repaint();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        }

        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }

    /**
     * ランダムに次のブロックを作成
     * 
     * @param field フィールドへの参照
     * @return ランダムに生成されたブロック
     */
    private Block createBlock(Field field) {
        // 乱数を用いてランダムにブロックを作る
        // 今は四角いブロックしか作れない
        return new Block(field);
    }
}
