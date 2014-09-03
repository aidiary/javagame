/*
 * メッセージに特化したウィンドウクラス
 * 
 */
package dqc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

public class MessageWindow extends Window {
    // 1行の最大文字数
    private static final int MAX_CHARS_PER_LINE = 30;
    // 1ページに表示できる最大行数
    private static final int MAX_LINES_PER_PAGE = 5;
    // 1ページに表示できる最大文字数
    private static final int MAX_CHARS_PER_PAGE = MAX_CHARS_PER_LINE * MAX_LINES_PER_PAGE;
    // 格納できる最大行数
    private static final int MAX_LINES = 128;
    
    // Fontに合わせて変えること
    // M2フォントの16.0fだとこのサイズ
    private static final int FONT_WIDTH = 16;
    private static final int FONT_HEIGHT = 18;
    
    // テキストを表示する領域
    private Rectangle textRect;
    
    // メッセージを格納する配列
    private char[] text = new char[MAX_LINES * MAX_CHARS_PER_LINE];
    // 現在表示しているページ
    private int curPage;
    // 現在のページで表示した文字数（最大値: MAX_CHARS_PER_LINE * MAX_LINES_PER_PAGE）
    private int curPos;
    // 次のページがあるか？
    private boolean nextFlag;
    // ウィンドウを隠せるか？（最後まで表示したらtrueになる）
    private boolean hideFlag;
    
    // テキストを流すTimerTask
    private Timer timer;
    private TimerTask task;
    
    public MessageWindow(Rectangle textRect) {
        super(textRect);
        
        this.textRect = textRect;
        timer = new Timer();
    }
    
    public void draw(Graphics2D g) {
        super.draw(g);

        if (isVisible == false) {
            return;
        }
        
        g.setColor(Color.WHITE);
        
        // 現在表示しているページのcurPosまで表示
        // curPosはDrawingTimerTaskで増えていくので流れて表示されるように見える
        for (int i=0; i<curPos; i++) {
            char c = text[curPage * MAX_CHARS_PER_PAGE + i];
            if (c == '/' || c == '%' || c == '!') continue;  // コントロール文字は表示しない
            int dx = textRect.x + FONT_WIDTH * (i % MAX_CHARS_PER_LINE);
            int dy = textRect.y + FONT_HEIGHT + FONT_HEIGHT * (i / MAX_CHARS_PER_LINE);
            g.drawString(c + "", dx, dy);
        }
        
        // 最後のページでない場合は▼を表示する
        if (nextFlag) {
            int dx = textRect.x + (MAX_CHARS_PER_LINE / 2) * FONT_WIDTH - 8;
            int dy = textRect.y + FONT_HEIGHT + (FONT_HEIGHT * 5);
            g.drawString("▼", dx, dy);
        }
    }
    
    /**
     * メッセージをセットする
     * 
     * @param message メッセージ文字列
     */
    public void setMessage(String message) {
        curPos = 0;
        curPage = 0;
        nextFlag = false;
        hideFlag = false;
        
        // 全角スペースで初期化
        for (int i=0; i<text.length; i++) {
            text[i] = '　';
        }
        
        int p = 0;  // 処理中の文字位置
        for (int i=0; i<message.length(); i++) {
            char c = message.charAt(i);
            if (c == '/') {  // 改行
                text[p] = '/';
                p += MAX_CHARS_PER_LINE;
                p = (p / MAX_CHARS_PER_LINE) * MAX_CHARS_PER_LINE;
            } else if (c == '%') {  // 改ページ
                text[p] = '%';
                p += MAX_CHARS_PER_PAGE;
                p = (p / MAX_CHARS_PER_PAGE) * MAX_CHARS_PER_PAGE;
            } else {
                text[p++] = c;
            }
        }
        text[p] = '!';  // 終端記号
        
        task = new DrawingMessageTask();
        timer.schedule(task, 0L, 20L);
    }
    
    /**
     * メッセージを先に進める
     * 
     * @return メッセージが終了したらtrueを返す
     */
    public boolean nextMessage() {
        // 現在のページが最後のページだったらメッセージを終了する
        if (hideFlag) {
            task.cancel();
            task = null;
            return true;
        }
        
        // ▼が表示されていなければ次のページへいけない
        if (nextFlag) {
            curPage++;
            curPos = 0;
            nextFlag = false;
            // TODO: ビープ音
        }
        
        return false;
    }
    
    /**
     * メッセージを1文字ずつ順に描画するタスク
     * 
     */
    private class DrawingMessageTask extends TimerTask {
        public void run() {
            if (!nextFlag) {
                curPos++;  // 1文字増やす
                // テキスト全体から見た現在位置
                int p = curPage * MAX_CHARS_PER_PAGE + curPos;
                if (text[p] == '/') {
                    curPos += MAX_CHARS_PER_LINE;
                    curPos = (curPos / MAX_CHARS_PER_LINE) * MAX_CHARS_PER_LINE;
                } else if (text[p] == '%') {
                    curPos += MAX_CHARS_PER_PAGE;
                    curPos = (curPos / MAX_CHARS_PER_PAGE) * MAX_CHARS_PER_PAGE;
                } else if (text[p] == '!') {
                    hideFlag = true;
                }
                
                // 1ページの文字数に達したら▼を表示
                if (curPos % MAX_CHARS_PER_PAGE == 0) {
                    nextFlag = true;
                }
            }
        }
    }
}
