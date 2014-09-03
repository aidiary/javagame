import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2007/05/04
 */

public class RemuoruTest extends JFrame {
    public RemuoruTest() {
        setTitle("ƒŒƒ€ƒIƒ‹I");
        setResizable(false);

        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new RemuoruTest();
    }
}
