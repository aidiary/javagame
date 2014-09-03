import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2005/12/10
 *
 */

/**
 * @author mori
 *
 */
public class SoundEngineTest extends JFrame {
    public SoundEngineTest() {
        // タイトルを設定
        setTitle("サウンドエンジンテスト");
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
        SoundEngineTest frame = new SoundEngineTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
