import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2005/04/16
 *
 */

/**
 * @author mori
 *
 */
public class WallTracing extends JFrame {
    public WallTracing() {
        // タイトルを設定
        setTitle("ウォールトレーシング");

        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        WallTracing frame = new WallTracing();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
