import java.awt.*;
import javax.swing.*;

public class DrawFigure extends JFrame {
    public DrawFigure() {
        // タイトルを設定
        setTitle("図形を描く");

        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        DrawFigure frame = new DrawFigure();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class MainPanel extends JPanel {
    // パネルサイズ
    private static final int WIDTH = 240;
    private static final int HEIGHT = 240;

    public MainPanel() {
        // パネルの推奨サイズを設定
        // pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 線を描く
        g.drawLine(10, 10, 100, 10);
        // 赤に変更
        g.setColor(Color.RED);
        // 四角形を描く
        g.drawRect(10, 20, 40, 40);
        g.fillRect(60, 20, 40, 40);
        // 青に変更
        g.setColor(Color.BLUE);
        // 円を描く
        g.drawOval(10, 70, 40, 40);
        g.fillOval(60, 70, 40, 40);
    }
}
