import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2006/07/28
 */

public class FloorTest extends JFrame {
    public FloorTest() {
        super("°");
        setResizable(false);

        Container c = getContentPane();

        // ƒƒCƒ“ƒpƒlƒ‹‚ğì¬
        MainPanel mainPanel = new MainPanel();
        c.add(mainPanel);

        pack();
    }

    public static void main(String[] args) {
        FloorTest frame = new FloorTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
