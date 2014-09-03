import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

/*
 * Created on 2006/08/17
 */

public class Lightning {
	private int x, y;  // 稲妻の始点
	private Color color;  // 稲妻の色
	private Vector points = new Vector();  // 稲妻の主線の折れ曲がり点
	private Vector lights = new Vector();  // 折れ曲がり点で分岐した稲妻
	private Random rand = new Random(System.currentTimeMillis());
	
	private int move; // 大きいとぎざぎざしやすい
	private int gap;  // 小さいほど折れ曲がりが多くなる
	private int div;  // 小さいほど折れ曲がり点で分岐しやすくなる

	public Lightning(Point begin, Color color) {
		this.x = begin.x;
		this.y = begin.y;
		this.color = color;
		
		// この値だとけっこうきれいな稲妻になる
		move = 30; 
		gap = 20;  
		div = 20;

		initPoints();
	}
	
	/**
	 * 稲妻を描画
	 * @param g 描画オブジェクト
	 */
	public void drawTo(Graphics g) {
		drawPoints(g);  // 稲妻の主線を描く
		// 分岐した稲妻を描画
		Enumeration e = lights.elements();
		while (e.hasMoreElements()) {
			Lightning lightning = (Lightning)e.nextElement();
			lightning.drawTo(g);
		}
	}

	/**
	 * 稲妻の主線を描く
	 * @param g 描画オブジェクト
	 */
	public void drawPoints(Graphics g) {
		Enumeration e = points.elements();
		Point before = new Point(x, y);  // この稲妻の始点
		g.setColor(color);
		while (e.hasMoreElements()) {
			Point p = (Point)e.nextElement();  // 次の点
			drawLightLine(g, before.x, before.y, p.x, p.y);
			before = p;
		}
	}

	/**
	 * 稲妻の折れ曲がり点を設定
	 */
	private void initPoints() {
		Point before = new Point(x, y);
		while (before.y < MainPanel.HEIGHT) {  // 下に消えるまで
			before = new Point(before.x + rand.nextInt() % move,
					before.y + gap);
			points.addElement(before);  // 折れ曲がり点を追加
			if ((int)(rand.nextDouble() * div) == 0) {  // 再帰的に新しい稲妻を接続する
				// 現在のbeforeの位置から新しい稲妻を開始
				lights.addElement(new Lightning(before, color));
			}
		}
	}
	
	/**
	 * 稲妻の折れ曲がり点をつなぐ線を描画
	 * @param g 描画オブジェクト
	 * @param x1 始点X
	 * @param y1 始点Y
	 * @param x2 終点X
	 * @param y2 終点Y
	 */
	private void drawLightLine(Graphics g, int x1, int y1, int x2, int y2) {
		Color c = g.getColor();
		// 微妙にずらしながら線を4本描画
		g.drawLine(x1-1, y1, x2-1, y2);
		g.drawLine(x1, y1+1, x2, y2+1);

		// 白い線でアクセントを入れる
		g.setColor(Color.WHITE);
		g.drawLine(x1, y1, x2, y2);
		
		g.setColor(c);  // 元の色に戻しておく
	}
}
