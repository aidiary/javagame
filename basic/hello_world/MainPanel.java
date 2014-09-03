import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/*
 * Created on 2006/02/24
 */

public class MainPanel extends JPanel {
    // パネルサイズ
    private static final int WIDTH = 240;
    private static final int HEIGHT = 240;
    // 画面に表示する文字列
    private String str;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // 変数の初期化
        str = "Hello World";
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // (20,50)の位置にHello Worldを描画する
        g.drawString(str, 20, 50);
    }
}
