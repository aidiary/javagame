import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2005/03/15
 *
 */

/**
 * @author mori
 *  
 */
public class LOS extends JFrame {
    public LOS() {
        // タイトルを設定
        setTitle("LOS追跡");

        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        LOS frame = new LOS();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}