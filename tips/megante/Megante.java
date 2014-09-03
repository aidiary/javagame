import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2005/12/03
 *
 */

/**
 * @author mori
 *  
 */
public class Megante extends JFrame {
    public Megante() {
        // タイトルを設定
        setTitle("メガンテ！");
        // サイズ変更不可
        setResizable(false);

        // パネルを作成
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        Megante frame = new Megante();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}