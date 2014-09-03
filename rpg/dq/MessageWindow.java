import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

/*
 * Created on 2005/12/25
 *
 */

/**
 * @author mori
 *
 */
public class MessageWindow extends Window {
    // 1行の最大文字数
    private static final int MAX_CHAR_IN_LINE = 20;
    // 1ページに表示できる最大行数
    private static final int MAX_LINES = 3;
    // 1ページに表示できる最大文字数
    private static final int MAX_CHAR_IN_PAGE = MAX_CHAR_IN_LINE * MAX_LINES;

    // カーソルのアニメーションGIF
    private BufferedImage cursorImage;

    // メッセージを格納した配列
    private char[] text = new char[128 * MAX_CHAR_IN_LINE];
    // 最大ページ
    private int maxPage;
    // 現在表示しているページ
    private int curPage = 0;
    // 表示した文字数
    private int curPos;
    // 次のページへいけるか（▼が表示されてればtrue）
    private boolean nextFlag = false;

    // テキストを流すタイマータスク
    private Timer timer;
    private TimerTask task;

    public MessageWindow(Rectangle rect) {
        super(rect);

        // カーソルイメージをロード
        try {
            cursorImage = ImageIO.read(getClass().getResource("image/cursor.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer = new Timer();
    }

    public void draw(Graphics g) {
        super.draw(g);

        if (isVisible == false) return;

        // 現在のページ（curPage）の1ページ分の内容を表示
        for (int i=0; i<curPos; i++) {
            char c = text[curPage * MAX_CHAR_IN_PAGE + i];
            int dx = textRect.x + MessageEngine.FONT_WIDTH * (i % MAX_CHAR_IN_LINE);
            int dy = textRect.y + (LINE_HEIGHT + MessageEngine.FONT_HEIGHT) * (i / MAX_CHAR_IN_LINE);
            messageEngine.drawCharacter(dx, dy, c, g);
        }

        // 最後のページでない場合は矢印を表示する
        if (curPage < maxPage && nextFlag) {
            int dx = textRect.x + (MAX_CHAR_IN_LINE / 2) * MessageEngine.FONT_WIDTH - 8;
            int dy = textRect.y + (LINE_HEIGHT + MessageEngine.FONT_HEIGHT) * 3;
            g.drawImage(cursorImage, dx, dy, null);
        }
    }

    /**
     * メッセージをセットする
     * @param msg メッセージ
     */
    public void setMessage(String msg) {
        curPos = 0;
        curPage = 0;
        nextFlag = false;

        // 全角スペースで初期化
        for (int i=0; i<text.length; i++) {
            text[i] = '　';
        }

        int p = 0;  // 処理中の文字位置
        for (int i=0; i<msg.length(); i++) {
            char c = msg.charAt(i);
            if (c == '\\') {
                i++;
                if (msg.charAt(i) == 'n') {  // 改行
                    p += MAX_CHAR_IN_LINE;
                    p = (p / MAX_CHAR_IN_LINE) * MAX_CHAR_IN_LINE;
                } else if (msg.charAt(i) == 'f') {  // 改ページ
                    p += MAX_CHAR_IN_PAGE;
                    p = (p / MAX_CHAR_IN_PAGE) * MAX_CHAR_IN_PAGE;
                }
            } else {
                text[p++] = c;
            }
        }
        
        maxPage = p / MAX_CHAR_IN_PAGE;
        
        task = new DrawingMessageTask();
        timer.schedule(task, 0L, 20L);
    }
    
    /**
     * メッセージを先に進める
     * @return メッセージが終了したらtrueを返す
     */
    public boolean nextMessage() {
        // 現在ページが最後のページだったらメッセージを終了する
        if (curPage == maxPage) {
            task.cancel();
            task = null;
            return true;
        }
        // ▼が表示されてなければ次ページへいけない
        if (nextFlag) {
            curPage++;
            curPos = 0;
            nextFlag = false;
            WaveEngine.play(Sound.BEEP);
        }
        return false;
    }

    // メッセージを1文字ずつ順に描画するタスク
    class DrawingMessageTask extends TimerTask {
        public void run() {
            if (!nextFlag) {
                curPos++;  // 1文字増やす
                // 1ページの文字数になったら▼を表示
                if (curPos % MAX_CHAR_IN_PAGE == 0) {
                    nextFlag = true;
                }
            }
        }
    }
}