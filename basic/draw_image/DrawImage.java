import java.awt.*;
import javax.swing.*;

public class DrawImage extends JFrame {
    public DrawImage() {
        // タイトルを設定
        setTitle("イメージを表示する");

        // パネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        DrawImage frame = new DrawImage();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}