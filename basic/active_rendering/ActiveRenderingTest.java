import javax.swing.JFrame;

/*
 * Created on 2006/05/07
 */

public class ActiveRenderingTest extends JFrame {
    public ActiveRenderingTest() {
        setTitle("アクティブレンダリング");

        // メインパネルを追加
        MainPanel panel = new MainPanel();
        getContentPane().add(panel);

        pack();
    }

    public static void main(String[] args) {
        ActiveRenderingTest frame = new ActiveRenderingTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
