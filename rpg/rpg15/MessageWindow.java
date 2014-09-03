import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
/*
 * Created on 2006/02/12
 *
 */

/**
 * @author mori
 *
 */
public class MessageWindow {
    // 白枠の幅
    private static final int EDGE_WIDTH = 2;

    // 一番外側の枠
    private Rectangle rect;
    // 一つ内側の枠（白い枠線ができるように）
    private Rectangle innerRect;

    // メッセージウィンドウを表示中か
    private boolean isVisible = false;

    public MessageWindow(Rectangle rect) {
        this.rect = rect;

        innerRect = new Rectangle(
                rect.x + EDGE_WIDTH,
                rect.y + EDGE_WIDTH,
                rect.width - EDGE_WIDTH * 2,
                rect.height - EDGE_WIDTH * 2);
    }

    public void draw(Graphics g) {
        if (isVisible == false) return;
        
        // 枠を描く
        g.setColor(Color.WHITE);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);

        // 内側の枠を描く
        g.setColor(Color.BLACK);
        g.fillRect(innerRect.x, innerRect.y, innerRect.width, innerRect.height);
    }

    /**
     * ウィンドウを表示
     */
    public void show() {
        isVisible = true;
    }

    /**
     * ウィンドウを隠す
     */
    public void hide() {
        isVisible = false;
    }
    
    /**
     * ウィンドウを表示中か
     */
    public boolean isVisible() {
        return isVisible;
    }
}
