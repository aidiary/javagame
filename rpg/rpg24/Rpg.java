import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2006/5/5
 *
 */

/**
 * @author mori
 *  
 */
public class Rpg extends JFrame {
    public Rpg() {
        // タイトルを設定
        setTitle("ここまでの整理");

        // パネルを作成
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        setResizable(false);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        Rpg frame = new Rpg();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}