import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Created on 2006/07/16
 */

public class Tetris extends JFrame {
    public Tetris() {
        // タイトルを設定
        setTitle("次のブロック表示");
        // サイズ変更不可
        setResizable(false);

        Container contentPane = getContentPane();

        // 右側パネル
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // スコアパネル
        ScorePanel scorePanel = new ScorePanel();
        // 次のブロックパネル
        NextBlockPanel nextBlockPanel = new NextBlockPanel();

        rightPanel.add(scorePanel, BorderLayout.NORTH);
        rightPanel.add(nextBlockPanel, BorderLayout.CENTER);

        // メインパネルを作成してフレームに追加
        // メインパネルからスコア表示パネルを操作するためscorePanel, nextBlockPanelを渡す必要あり！
        MainPanel mainPanel = new MainPanel(scorePanel, nextBlockPanel);

        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(rightPanel, BorderLayout.EAST);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        Tetris frame = new Tetris();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
