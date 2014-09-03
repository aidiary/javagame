import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2006/07/28
 */

public class Sample3D extends JFrame {
    public Sample3D() {
        super("Java3Dの雛形");
        setResizable(false);

        Container c = getContentPane();

        // メインパネルを作成
        MainPanel mainPanel = new MainPanel();
        c.add(mainPanel);

        pack();
    }

    public static void main(String[] args) {
        Sample3D frame = new Sample3D();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
