import java.awt.*;
import javax.swing.*;

public class ManyBalls extends JFrame {
    public ManyBalls() {
        // タイトルを設定
        setTitle("ボールをたくさん作る");
        // サイズ変更禁止
        setResizable(false);

        // パネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        ManyBalls frame = new ManyBalls();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}