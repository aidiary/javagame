import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

/*
 * Created on 2006/08/17
 */

public class MainPanel extends JPanel implements Runnable, MouseListener {
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;

	// 稲妻
	private Lightning lightning;

	// カウンタ
	private int cnt = 0;

	// ゲームループ
	private Thread gameLoop;

	// ダブルバッファリング（db）用
	private Graphics dbg;
	private Image dbImage = null;

	// サウンド
    private AudioClip thunderSound;

	public MainPanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		addMouseListener(this);

	    // サウンドをロード
	    thunderSound = Applet.newAudioClip(getClass().getResource("don09_a.wav"));

		// ゲームループ開始
		gameLoop = new Thread(this);
		gameLoop.start();
	}

	public void run() {
		while (true) {
			gameRender(); // バッファにレンダリング（ダブルバッファリング）
			paintScreen(); // バッファを画面に描画（repaint()を自分でする！）

			cnt++;

			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * レンダリング
	 */
	private void gameRender() {
		// 初回の呼び出し時にダブルバッファリング用オブジェクトを作成
		if (dbImage == null) {
			// バッファイメージ
			dbImage = createImage(WIDTH, HEIGHT);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			} else {
				// バッファイメージの描画オブジェクト
				dbg = dbImage.getGraphics();
			}
			return;
		}

		// バッファをクリアする
		dbg.setColor(Color.BLACK);
		dbg.fillRect(0, 0, WIDTH, HEIGHT);

		// cntが18を越えたら稲妻を描画しない
		if (cnt > 18)
			return;

		// cntが偶数のときのみ稲妻を描画
		// cntが奇数のときは描画されないので点滅して見える
		if (lightning != null) {
			if (cnt % 2 == 0) {
				lightning.drawTo(dbg);
			}
		}
	}

	/**
	 * バッファを画面に描画
	 */
	private void paintScreen() {
		try {
			Graphics g = getGraphics(); // グラフィックオブジェクトを取得
			if ((g != null) && (dbImage != null)) {
				g.drawImage(dbImage, 0, 0, null); // バッファイメージを画面に描画
			}
			Toolkit.getDefaultToolkit().sync();
			if (g != null) {
				g.dispose(); // グラフィックオブジェクトを破棄
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		// カウンタをクリア
		cnt = 0;
		// クリックした位置が稲妻の始点
		lightning = new Lightning(new Point(x, y), Color.BLUE);
		// ぴっしゃーん
		thunderSound.play();
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
