import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Created on 2005/05/04
 *
 */

/**
 * 3層パーセプトロンによる文字認識のサンプル
 * @author mori
 *
 */
public class PatternRecognition extends JFrame {
    public PatternRecognition() {
        // タイトルを設定
        setTitle("NNによるパターン認識");
        setResizable(false);
        
        InfoPanel infoPanel = new InfoPanel();

        // メインパネルを作成してフレームに追加
        MainPanel mainPanel = new MainPanel(infoPanel);
        Container contentPane = getContentPane();
        contentPane.add(mainPanel, BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        panel.add(infoPanel, BorderLayout.CENTER);
        
        ControlPanel controlPanel = new ControlPanel(mainPanel);
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        contentPane.add(panel, BorderLayout.EAST);
        
        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        PatternRecognition frame = new PatternRecognition();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
