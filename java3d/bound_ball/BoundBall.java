import java.awt.*;
import javax.swing.*;

public class BoundBall extends JFrame {
    public BoundBall() {
        // タイトルを設定
        setTitle("ボールが跳ね返る処理");

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
        BoundBall frame = new BoundBall();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}