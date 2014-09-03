import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2006/12/03
 */

public class TransparentTest extends JFrame {
    public TransparentTest() {
        setTitle("”¼“§–¾•`‰æ");

        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new TransparentTest();
    }
}
