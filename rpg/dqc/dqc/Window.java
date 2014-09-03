/*
 * ドラクエ風の白枠ウィンドウの共通クラス
 * 
 */
package dqc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class Window {
    // 外枠とテキスト領域の間隔
    protected static final int FRAME_TEXT = 16;
    // 外枠の幅
    protected static final int EDGE_WIDTH = 2;
    
    // テキスト領域（実際にテキストを描画する部分）
    protected Rectangle textRect;
    // 外枠（一番外側）
    protected Rectangle frameRect;
    // 枠線を描くための内枠（EDGE_WIDTHだけ内側）
    protected Rectangle innerRect;
    
    // ウィンドウを表示中か
    protected boolean isVisible = false;
    
    public Window(Rectangle textRect) {
        this.textRect = textRect;
        
        // textRectよりFRAME_TEXTだけ外側
        frameRect = new Rectangle(textRect.x-FRAME_TEXT, textRect.y-FRAME_TEXT,
                textRect.width+FRAME_TEXT*2, textRect.height+FRAME_TEXT*2);

        // frameRectよりEDGE_WIDTHだけ内側
        innerRect = new Rectangle(frameRect.x+EDGE_WIDTH, frameRect.y+EDGE_WIDTH,
                frameRect.width-EDGE_WIDTH*2, frameRect.height-EDGE_WIDTH*2);
    }
    
    public void draw(Graphics2D g) {
        if (isVisible == false) {
            return;
        }

        // テキストのアンチエイリアシング
        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        
        g.setColor(Color.WHITE);

        // 外枠を白色で描く
        g.setColor(Color.WHITE);
        g.fillRect(frameRect.x, frameRect.y, frameRect.width, frameRect.height);
        
        // 内枠を黒色で描く=>外枠と合わせると白い枠に見える
        g.setColor(Color.BLACK);
        g.fillRect(innerRect.x, innerRect.y, innerRect.width, innerRect.height);
    }
    
    /**
     * ウィンドウを表示
     *
     */
    public void show() {
        isVisible = true;
    }
    
    /**
     * ウィンドウを隠す
     *
     */
    public void hide() {
        isVisible = false;
    }
    
    /**
     * 表示中か？
     * 
     * @return 表示中ならtrue
     */
    public boolean isVisible() {
        return isVisible;
    }
}
