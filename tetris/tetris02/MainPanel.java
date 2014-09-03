import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/*
 * Created on 2006/03/19
 */

public class MainPanel extends JPanel {
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
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // フィールドを描画
        field.draw(g);
        // ブロックを描画
        block.draw(g);
    }
}
