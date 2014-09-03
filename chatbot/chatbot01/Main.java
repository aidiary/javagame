import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2007/01/08
 */

public class Main extends JFrame {
    public Main() {
        setTitle("êlçHñ≥î]1çÜ");

        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
