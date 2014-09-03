/*
 * 話す（Talk）ことができるイベント
 * 
 */
package dqc;

import java.awt.Graphics;

public abstract class TalkEvent extends Event {
    // TALKで表示されるメッセージ
    protected String message;
    
    public TalkEvent(int x, int y, int imageNo, String message) {
        super(x, y, imageNo, false);  // 話せるイベントは移動不可能
        this.message = message;
    }

    public abstract void draw(Graphics g, int offsetX, int offsetY);
    public abstract void start(Hero hero, Map map, MessageWindow msgWnd);
    
    public String toString() {
        return super.toString() + "," + message;
    }
}
