import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * Created on 2006/02/05
 */

/**
 * コマンドウィンドウ
 * @author mori
 */
public class CommandWindow extends Window {
    // コマンド番号
    public static final int TALK = 0;  // はなす
    public static final int STATUS = 1;  // つよさ
    public static final int EQUIPMENT = 2;  // そうび
    public static final int DOOR = 3;  // とびら
    public static final int SPELL = 4;  // じゅもん
    public static final int ITEM = 5;  // どうぐ
    public static final int TACTICS = 6;  // さくせん
    public static final int SEARCH = 7;  // しらべる
    
    // ウィンドウに表示するテキスト
    private String[] commands = {"はなす", "つよさ", "そうび", "とびら",
            "じゅもん", "どうぐ", "さくせん", "しらべる"};

    // 選択されているコマンド番号
    private int selectedCmdNo;
    // カーソルのイメージ
    private BufferedImage cursorImage;

    public CommandWindow(Rectangle rect) {
        super(rect);
        
        selectedCmdNo = TALK;

        try {
            cursorImage = ImageIO.read(getClass().getResource("image/cmd_cursor.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ウィンドウを描画
     */
    public void draw(Graphics g) {
        super.draw(g);
        
        if (isVisible == false) return;

        // はなす、つよさ、そうび、とびら
        for (int i=0; i<4; i++) {
            int dx = textRect.x + MessageEngine.FONT_WIDTH;
            int dy = textRect.y + (LINE_HEIGHT + MessageEngine.FONT_HEIGHT) * i;
            messageEngine.drawMessage(dx, dy, commands[i], g);
        }
        
        // じゅもん、どうぐ、さくせん、しらべる
        for (int i=4; i<8; i++) {
            int dx = textRect.x + 116;
            int dy = textRect.y + (LINE_HEIGHT + MessageEngine.FONT_HEIGHT) * (i%4);
            messageEngine.drawMessage(dx, dy, commands[i], g);
        }
        
        int dx = textRect.x +100 * (selectedCmdNo / 4);
        int dy = textRect.y + (LINE_HEIGHT + MessageEngine.FONT_HEIGHT) * (selectedCmdNo % 4);
        g.drawImage(cursorImage, dx, dy, null);
    }

    /**
     * カーソルを左に移動
     */
    public void leftCursor() {
        // はなす、つよさ、そうび、とびらのときはカーソルを移動できない
        if (selectedCmdNo <= 3) return;
        selectedCmdNo -= 4;
    }

    /**
     * カーソルを右に移動
     */
    public void rightCursor() {
        // じゅもん、どうぐ、さくせん、しらべるのときはカーソルを移動できない
        if (selectedCmdNo >= 4) return;
        selectedCmdNo += 4;
    }

    /**
     * カーソルを上に移動
     */
    public void upCursor() {
        // はなす、じゅもんのときはカーソルを移動できない
        if (selectedCmdNo == 0 || selectedCmdNo == 4) return;
        selectedCmdNo--;
    }
    
    /**
     * カーソルを下に移動
     */
    public void downCursor() {
        // とびら、しらべるのときはカーソルを移動できない
        if (selectedCmdNo == 3 || selectedCmdNo == 7) return;
        selectedCmdNo++;
    }

    /**
     * 選択されているコマンド番号を返す
     * @return コマンド番号
     */
    public int getSelectedCmdNo() {
        return selectedCmdNo;
    }
    
    // オーバーライド
    public void show() {
        WaveEngine.play(Sound.BEEP);
        isVisible = true;
        selectedCmdNo = TALK;  // カーソルははなすに初期化
    }
}
