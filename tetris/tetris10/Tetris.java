import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2006/07/16
 */

public class Tetris extends JFrame {
	public Tetris() {
		// タイトルを設定
		setTitle("スコア表示");
		// サイズ変更不可
		setResizable(false);

		Container contentPane = getContentPane();

		// スコア表示パネル
		ScorePanel scorePanel = new ScorePanel();
		contentPane.add(scorePanel, BorderLayout.NORTH);

		// メインパネルを作成してフレームに追加
		// メインパネルからスコア表示パネルを操作するためscorePanelを渡す必要あり！
		MainPanel mainPanel = new MainPanel(scorePanel);
		contentPane.add(mainPanel, BorderLayout.CENTER);

		// パネルサイズに合わせてフレームサイズを自動設定
		pack();
	}

	public static void main(String[] args) {
		Tetris frame = new Tetris();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
