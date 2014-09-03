/*
 * 作成日: 2004/12/07
 *
 */
import java.awt.*;
import javax.swing.*;
/**
 * アプリケーションクラス。
 * 
 * @author mori
 *  
 */
public class PongLearning extends JFrame {
    public PongLearning() {
        // タイトルを設定
        setTitle("Pongの学習");

        Container contentPane = getContentPane();

        // インフォメーションパネルを作成
        InfoPanel infoPanel = new InfoPanel();
        contentPane.add(infoPanel, BorderLayout.NORTH);

        // メインパネルを作成
        MainPanel mainPanel = new MainPanel();
        contentPane.add(mainPanel, BorderLayout.CENTER);
        // メインパネルにインフォメーションパネルを渡す
        mainPanel.setInfoPanel(infoPanel);

        // コントロールパネルを作成
        ControlPanel controlPanel = new ControlPanel(mainPanel);
        contentPane.add(controlPanel, BorderLayout.SOUTH);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        PongLearning frame = new PongLearning();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

