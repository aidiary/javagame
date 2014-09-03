import javax.swing.JFrame;

/*
 * Created on 2006/06/10
 */

public class ArrowTest extends JFrame {
    public ArrowTest() {
        setTitle("‹|–î");
        setResizable(false);
        
        MainPanel panel = new MainPanel();
        getContentPane().add(panel);
        
        pack();
    }
    
    public static void main(String[] args) {
        ArrowTest frame = new ArrowTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
