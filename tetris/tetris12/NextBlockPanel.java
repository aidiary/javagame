import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/*
 * Created on 2006/12/09
 */

public class NextBlockPanel extends JPanel {
    public static final int WIDTH = 96;
    public static final int HEIGHT = 400;

    private Block nextBlock;
    private Image blockImage;

    public NextBlockPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 次のブロックを描画
        if (nextBlock != null) {
            nextBlock.drawInPanel(g, blockImage);
        }
    }

    /**
     * 次のブロックをセット
     * 
     * @param nextBlock 次のブロック
     * @param blockImage ブロックイメージ
     */
    public void set(Block nextBlock, Image blockImage) {
        this.nextBlock = nextBlock;
        this.blockImage = blockImage;
        repaint();
    }
}
