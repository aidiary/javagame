import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2006/04/23
 */

public class Tetris extends JFrame {
    public Tetris() {
        // タイトルを設定
        setTitle("積み上げ");
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
