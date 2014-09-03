import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2005/04/23
 *
 */

/**
 * A*経路探索のサンプルプログラム
 * @author mori
 */
public class AstarTest3 extends JFrame {
    public AstarTest3() {
        // タイトルを設定
        setTitle("A*経路探索3");

        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        AstarTest3 frame = new AstarTest3();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
