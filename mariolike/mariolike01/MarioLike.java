import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2005/06/06
 *
 */

/**
 * @author mori
 *
 */
public class MarioLike extends JFrame {
    public MarioLike() {
        // タイトルを設定
        setTitle("左右移動");
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
        MarioLike frame = new MarioLike();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
