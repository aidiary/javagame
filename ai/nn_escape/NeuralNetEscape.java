import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2005/05/13
 *
 */

/**
 * ニューラルネットを使った逃避行動の学習
 * @author mori
 *
 */
public class NeuralNetEscape extends JFrame {
    public NeuralNetEscape() {
        // タイトルを設定
        setTitle("NNによる逃避行動の学習");
        setResizable(false);

        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        NeuralNetEscape frame = new NeuralNetEscape();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
