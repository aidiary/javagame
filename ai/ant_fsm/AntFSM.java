import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2005/04/24
 *
 */

/**
 * @author mori
 *
 */
public class AntFSM extends JFrame {
    public AntFSM() {
        // タイトルを設定
        setTitle("有限状態機械（蟻の例）");

        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        AntFSM frame = new AntFSM();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
