import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2006/12/03
 */

public class SpotlightTest extends JFrame {
    public SpotlightTest() {
        setTitle("スポットライトテスト");
        setResizable(false);

        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SpotlightTest();
    }
}
