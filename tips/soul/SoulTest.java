import javax.swing.JFrame;

/*
 * Created on 2007/02/04
 */

public class SoulTest extends JFrame {
    public SoulTest() {
        setTitle("ソウルブレイダー？");
        setResizable(false);

        MainPanel panel = new MainPanel();
        getContentPane().add(panel);

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SoulTest();
    }
}
