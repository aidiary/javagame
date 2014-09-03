import javax.swing.JFrame;

/*
 * Created on 2007/02/04
 */

public class CircularMotion extends JFrame {
    public CircularMotion() {
        setTitle("‰~‰^“®");
        setResizable(false);

        MainPanel panel = new MainPanel();
        getContentPane().add(panel);

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new CircularMotion();
    }
}
