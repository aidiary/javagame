import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/*
 * Created on 2006/02/05
 */

/**
 * @author mori
 */
public abstract class Window {
    // 白枠の幅
    protected static final int EDGE_WIDTH = 2;
    // 行間の大きさ
    protected static final int LINE_HEIGHT = 8;

    // 一番外側の枠
    protected Rectangle rect;
    // 一つ内側の枠（白い枠線ができるように）
    protected Rectangle innerRect;
    // 実際にテキストを描画する枠
    protected Rectangle textRect;
    
    // メッセージウィンドウを表示中か
    protected boolean isVisible = false;
    
    // メッセージエンジン
    protected MessageEngine messageEngine;
    
    public Window(Rectangle rect) {
        this.rect = rect;
        
        innerRect = new Rectangle(
                rect.x + EDGE_WIDTH,
                rect.y + EDGE_WIDTH,
                rect.width - EDGE_WIDTH * 2,
                rect.height - EDGE_WIDTH * 2);

        textRect = new Rectangle(
                innerRect.x + 16,
                innerRect.y + 16,
                320,  // TODO:
                120);
        
        // メッセージエンジンを作成
        messageEngine = new MessageEngine();
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
    
    public void show() {
        isVisible = true;
    }

    public void hide() {
        isVisible = false;
    }
    
    public boolean isVisible() {
        return isVisible;
    }
}
