import javax.swing.JFrame;

/*
 * Created on 2007/02/02
 */

public class TextFileIO extends JFrame {
    public TextFileIO() {
        setTitle("テキストファイル入出力");
        
        MainPanel panel = new MainPanel();
        getContentPane().add(panel);
        
        pack();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new TextFileIO();
    }
}
