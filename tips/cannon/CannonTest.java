import javax.swing.JFrame;

/*
 * Created on 2006/06/02
 */

public class CannonTest extends JFrame {
    public CannonTest() {
        setTitle("‘å–C");
        setResizable(false);
        
        MainPanel panel = new MainPanel();
        getContentPane().add(panel);
        
        pack();
    }
    
    public static void main(String[] args) {
        CannonTest frame = new CannonTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
