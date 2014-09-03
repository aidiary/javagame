import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2005/03/30
 *
 */

/**
 * ブレゼンハムアルゴリズムで直線を描画する
 * 
 * @author mori
 *
 */
public class Bresenham extends JFrame {
    public Bresenham() {
        // タイトルを設定
        setTitle("ブレゼンハムアルゴリズム");

        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        Bresenham frame = new Bresenham();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
