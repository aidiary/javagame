import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;

import javax.swing.JPanel;

/*
 * Created on 2006/07/16
 */

public class ScorePanel extends JPanel {
    // パネルサイズ
    public static final int WIDTH = 96;
    public static final int HEIGHT = 16;

    // スコア
    private int score;

    public ScorePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // スコアを描画
        g.setColor(Color.YELLOW);
        DecimalFormat formatter = new DecimalFormat("000000");
        // フォントを作成
        Font font = new Font("Ariel", Font.BOLD, 16);
        g.setFont(font);

        g.drawString(formatter.format(score), 16, 12);
    }

    /**
     * スコアをセットする
     * 
     * @param score スコア
     */
    public void setScore(int score) {
        this.score = score;

        repaint();
    }

    /**
     * スコアをプラスする
     * 
     * @param d プラス分
     */
    public void upScore(int d) {
        score += d;

        repaint();
    }
}
