import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2006/04/22
 */

public class Tetris extends JFrame {
    public Tetris() {
        // タイトルを設定
        setTitle("移動と回転");
        // サイズ変更不可
        setResizable(false);

        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        Tetris frame = new Tetris();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
