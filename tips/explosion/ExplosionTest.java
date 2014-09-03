import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2006/01/29
 */

/**
 * @author mori
 */
public class ExplosionTest extends JFrame {
    public ExplosionTest() {
        // タイトルを設定
        setTitle("爆発エフェクト");
        setResizable(false);

        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        ExplosionTest frame = new ExplosionTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}