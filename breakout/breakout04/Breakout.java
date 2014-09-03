import javax.swing.JFrame;

/*
 * Created on 2007/05/05
 * 
 * ブロック崩しのメインクラス
 */

public class Breakout extends JFrame {
    public Breakout() {
        setTitle("ブロックとボールの当たり判定");
        setResizable(false);

        MainPanel panel = new MainPanel();
        getContentPane().add(panel);

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Breakout();
    }
}
