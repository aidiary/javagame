import java.awt.Container;

/*
 * Created on 2005/01/16
 *
 */
import javax.swing.*;
/**
 * @author mori
 *  
 */
public class ChaseTest extends JFrame {
    public ChaseTest() {
        // タイトルを設定
        setTitle("追跡");

        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        ChaseTest frame = new ChaseTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}