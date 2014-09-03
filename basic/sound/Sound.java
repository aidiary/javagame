import java.awt.*;
import javax.swing.*;

public class Sound extends JFrame {
    public Sound() {
        // タイトルを設定
        setTitle("効果音を鳴らす");

        // パネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        Sound frame = new Sound();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}