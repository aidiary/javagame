import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2005/12/17
 *
 */

/**
 * @author mori
 *  
 */
public class MapEditor extends JFrame {
    public MapEditor() {
        setTitle("ひながた");
        setResizable(false);

        // パネルを作成
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        MapEditor frame = new MapEditor();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}