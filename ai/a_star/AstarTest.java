import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2005/04/17
 *
 */

/**
 * A*経路探索のサンプルプログラム
 * @author mori
 */
public class AstarTest extends JFrame {
    public AstarTest() {
        // タイトルを設定
        setTitle("A*経路探索");

        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        AstarTest frame = new AstarTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
