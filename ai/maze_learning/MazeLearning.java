import java.awt.*;
import javax.swing.*;

public class MazeLearning extends JFrame {
    public MazeLearning() {
        // タイトルを設定
        setTitle("迷路学習");

        // メインパネルを作成
        MainPanel mainPanel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(mainPanel, BorderLayout.CENTER);

        // コントロールパネルを作成
        ControlPanel controlPanel = new ControlPanel(mainPanel);
        contentPane.add(controlPanel, BorderLayout.SOUTH);

        // インフォメーションパネルを作成
        InfoPanel infoPanel = new InfoPanel();
        contentPane.add(infoPanel, BorderLayout.NORTH);
        // メインパネルにインフォメーションパネルを渡す
        mainPanel.setInfoPanel(infoPanel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        MazeLearning frame = new MazeLearning();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}