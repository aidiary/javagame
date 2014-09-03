import java.awt.*;
import javax.swing.*;

public class MouseClick extends JFrame {
    public MouseClick() {
        // タイトルを設定
        setTitle("マウスクリック");

        // パネルを作成
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        MouseClick frame = new MouseClick();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
