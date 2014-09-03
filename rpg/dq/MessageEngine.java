import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/*
 * Created on 2005/12/25
 *
 */

/**
 * @author mori
 *
 */
public class MessageEngine {
    // フォントの大きさ
    public static final int FONT_WIDTH = 16;
    public static final int FONT_HEIGHT = 22;
    
    // フォントの色
    public static final int WHITE = 0;
    public static final int RED = 160;
    public static final int GREEN = 320;
    public static final int BLUE = 480;
    
    // フォントイメージ
    private BufferedImage fontImage;
    // かな→座標のハッシュ
    private HashMap kana2Pos;
    
    // 色
    private int color;

    public MessageEngine() {
        // フォントイメージをロード
        try {
            fontImage = ImageIO.read(getClass().getResource("image/font.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        color = WHITE;
        
        // かな→イメージ座標へのハッシュを作成
        kana2Pos = new HashMap();
        createHash();
    }
    
    public void setColor(int c) {     
        this.color = c;
        
        // 変な値だったらWHITEにする
        if (color != WHITE && color != RED && color != GREEN && color != BLUE) {
            this.color = WHITE;
        }
    }
    
    /**
     * メッセージを描画する
     * @param x X座標
     * @param y Y座標
     * @param message メッセージ
     * @param g 描画オブジェクト
     */
    public void drawMessage(int x, int y, String message, Graphics g) {
        for (int i=0; i<message.length(); i++) {
            char c = message.charAt(i);
            int dx = x + FONT_WIDTH * i;
            drawCharacter(dx, y, c, g);
        }
    }
    
    /**
     * 文字を描画する
     * @param x X座標
     * @param y Y座標
     * @param c 文字
     * @param g 描画オブジェクト
     */
    public void drawCharacter(int x, int y, char c, Graphics g) {
        Point pos = (Point)kana2Pos.get(new Character(c));
        g.drawImage(fontImage, x, y, x + FONT_WIDTH, y + FONT_HEIGHT,
                pos.x + color, pos.y, pos.x + color + FONT_WIDTH, pos.y + FONT_HEIGHT, null);
    }
    
    /**
     * 文字から座標へのハッシュを作成する
     */
    private void createHash() {
        kana2Pos.put(new Character('あ'), new Point(0, 0));
        kana2Pos.put(new Character('い'), new Point(16, 0));
        kana2Pos.put(new Character('う'), new Point(32, 0));
        kana2Pos.put(new Character('え'), new Point(48, 0));
        kana2Pos.put(new Character('お'), new Point(64, 0));
        
        kana2Pos.put(new Character('か'), new Point(0, 22));
        kana2Pos.put(new Character('き'), new Point(16, 22));
        kana2Pos.put(new Character('く'), new Point(32, 22));
        kana2Pos.put(new Character('け'), new Point(48, 22));
        kana2Pos.put(new Character('こ'), new Point(64, 22));
        
        kana2Pos.put(new Character('さ'), new Point(0, 44));
        kana2Pos.put(new Character('し'), new Point(16, 44));
        kana2Pos.put(new Character('す'), new Point(32, 44));
        kana2Pos.put(new Character('せ'), new Point(48, 44));
        kana2Pos.put(new Character('そ'), new Point(64, 44));
        
        kana2Pos.put(new Character('た'), new Point(0, 66));
        kana2Pos.put(new Character('ち'), new Point(16, 66));
        kana2Pos.put(new Character('つ'), new Point(32, 66));
        kana2Pos.put(new Character('て'), new Point(48, 66));
        kana2Pos.put(new Character('と'), new Point(64, 66));
        
        kana2Pos.put(new Character('な'), new Point(0, 88));
        kana2Pos.put(new Character('に'), new Point(16, 88));
        kana2Pos.put(new Character('ぬ'), new Point(32, 88));
        kana2Pos.put(new Character('ね'), new Point(48, 88));
        kana2Pos.put(new Character('の'), new Point(64, 88));
        
        kana2Pos.put(new Character('は'), new Point(0, 110));
        kana2Pos.put(new Character('ひ'), new Point(16, 110));
        kana2Pos.put(new Character('ふ'), new Point(32, 110));
        kana2Pos.put(new Character('へ'), new Point(48, 110));
        kana2Pos.put(new Character('ほ'), new Point(64, 110));
        
        kana2Pos.put(new Character('ま'), new Point(0, 132));
        kana2Pos.put(new Character('み'), new Point(16, 132));
        kana2Pos.put(new Character('む'), new Point(32, 132));
        kana2Pos.put(new Character('め'), new Point(48, 132));
        kana2Pos.put(new Character('も'), new Point(64, 132));
        
        kana2Pos.put(new Character('や'), new Point(0, 154));
        kana2Pos.put(new Character('ゆ'), new Point(16, 154));
        kana2Pos.put(new Character('よ'), new Point(32, 154));
        kana2Pos.put(new Character('わ'), new Point(48, 154));
        kana2Pos.put(new Character('を'), new Point(64, 154));
        
        kana2Pos.put(new Character('ら'), new Point(0, 176));
        kana2Pos.put(new Character('り'), new Point(16, 176));
        kana2Pos.put(new Character('る'), new Point(32, 176));
        kana2Pos.put(new Character('れ'), new Point(48, 176));
        kana2Pos.put(new Character('ろ'), new Point(64, 176));
        
        kana2Pos.put(new Character('ん'), new Point(0, 198));
        kana2Pos.put(new Character('ぃ'), new Point(16, 198));
        kana2Pos.put(new Character('っ'), new Point(32, 198));
        kana2Pos.put(new Character('ぇ'), new Point(48, 198));
        kana2Pos.put(new Character('　'), new Point(64, 198));
        
        kana2Pos.put(new Character('ゃ'), new Point(0, 220));
        kana2Pos.put(new Character('ゅ'), new Point(16, 220));
        kana2Pos.put(new Character('ょ'), new Point(32, 220));
        kana2Pos.put(new Character('、'), new Point(48, 220));
        kana2Pos.put(new Character('。'), new Point(64, 220));
        
        kana2Pos.put(new Character('が'), new Point(0, 242));
        kana2Pos.put(new Character('ぎ'), new Point(16, 242));
        kana2Pos.put(new Character('ぐ'), new Point(32, 242));
        kana2Pos.put(new Character('げ'), new Point(48, 242));
        kana2Pos.put(new Character('ご'), new Point(64, 242));
        
        kana2Pos.put(new Character('ざ'), new Point(0, 264));
        kana2Pos.put(new Character('じ'), new Point(16, 264));
        kana2Pos.put(new Character('ず'), new Point(32, 264));
        kana2Pos.put(new Character('ぜ'), new Point(48, 264));
        kana2Pos.put(new Character('ぞ'), new Point(64, 264));
        
        kana2Pos.put(new Character('だ'), new Point(0, 286));
        kana2Pos.put(new Character('ぢ'), new Point(16, 286));
        kana2Pos.put(new Character('づ'), new Point(32, 286));
        kana2Pos.put(new Character('で'), new Point(48, 286));
        kana2Pos.put(new Character('ど'), new Point(64, 286));
        
        kana2Pos.put(new Character('ば'), new Point(0, 308));
        kana2Pos.put(new Character('び'), new Point(16, 308));
        kana2Pos.put(new Character('ぶ'), new Point(32, 308));
        kana2Pos.put(new Character('べ'), new Point(48, 308));
        kana2Pos.put(new Character('ぼ'), new Point(64, 308));
        
        kana2Pos.put(new Character('ぱ'), new Point(0, 330));
        kana2Pos.put(new Character('ぴ'), new Point(16, 330));
        kana2Pos.put(new Character('ぷ'), new Point(32, 330));
        kana2Pos.put(new Character('ぺ'), new Point(48, 330));
        kana2Pos.put(new Character('ぽ'), new Point(64, 330));
        
        kana2Pos.put(new Character('ア'), new Point(80, 0));
        kana2Pos.put(new Character('イ'), new Point(96, 0));
        kana2Pos.put(new Character('ウ'), new Point(112, 0));
        kana2Pos.put(new Character('エ'), new Point(128, 0));
        kana2Pos.put(new Character('オ'), new Point(144, 0));
        
        kana2Pos.put(new Character('カ'), new Point(80, 22));
        kana2Pos.put(new Character('キ'), new Point(96, 22));
        kana2Pos.put(new Character('ク'), new Point(112, 22));
        kana2Pos.put(new Character('ケ'), new Point(128, 22));
        kana2Pos.put(new Character('コ'), new Point(144, 22));

        kana2Pos.put(new Character('サ'), new Point(80, 44));
        kana2Pos.put(new Character('シ'), new Point(96, 44));
        kana2Pos.put(new Character('ス'), new Point(112, 44));
        kana2Pos.put(new Character('セ'), new Point(128, 44));
        kana2Pos.put(new Character('ソ'), new Point(144, 44));
        
        kana2Pos.put(new Character('タ'), new Point(80, 66));
        kana2Pos.put(new Character('チ'), new Point(96, 66));
        kana2Pos.put(new Character('ツ'), new Point(112, 66));
        kana2Pos.put(new Character('テ'), new Point(128, 66));
        kana2Pos.put(new Character('ト'), new Point(144, 66));
        
        kana2Pos.put(new Character('ナ'), new Point(80, 88));
        kana2Pos.put(new Character('ニ'), new Point(96, 88));
        kana2Pos.put(new Character('ヌ'), new Point(112, 88));
        kana2Pos.put(new Character('ネ'), new Point(128, 88));
        kana2Pos.put(new Character('ノ'), new Point(144, 88));
        
        kana2Pos.put(new Character('ハ'), new Point(80, 110));
        kana2Pos.put(new Character('ヒ'), new Point(96, 110));
        kana2Pos.put(new Character('フ'), new Point(112, 110));
        kana2Pos.put(new Character('ヘ'), new Point(128, 110));
        kana2Pos.put(new Character('ホ'), new Point(144, 110));
        
        kana2Pos.put(new Character('マ'), new Point(80, 132));
        kana2Pos.put(new Character('ミ'), new Point(96, 132));
        kana2Pos.put(new Character('ム'), new Point(112, 132));
        kana2Pos.put(new Character('メ'), new Point(128, 132));
        kana2Pos.put(new Character('モ'), new Point(144, 132));
        
        kana2Pos.put(new Character('ヤ'), new Point(80, 154));
        kana2Pos.put(new Character('ユ'), new Point(96, 154));
        kana2Pos.put(new Character('ヨ'), new Point(112, 154));
        kana2Pos.put(new Character('ワ'), new Point(128, 154));
        kana2Pos.put(new Character('ヲ'), new Point(144, 154));
        
        kana2Pos.put(new Character('ラ'), new Point(80, 176));
        kana2Pos.put(new Character('リ'), new Point(96, 176));
        kana2Pos.put(new Character('ル'), new Point(112, 176));
        kana2Pos.put(new Character('レ'), new Point(128, 176));
        kana2Pos.put(new Character('ロ'), new Point(144, 176));
        
        kana2Pos.put(new Character('ン'), new Point(80, 198));
        kana2Pos.put(new Character('ィ'), new Point(96, 198));
        kana2Pos.put(new Character('ッ'), new Point(112, 198));
        kana2Pos.put(new Character('ェ'), new Point(128, 198));
        kana2Pos.put(new Character('「'), new Point(144, 198));
        
        kana2Pos.put(new Character('ャ'), new Point(80, 220));
        kana2Pos.put(new Character('ュ'), new Point(96, 220));
        kana2Pos.put(new Character('ョ'), new Point(112, 220));
        kana2Pos.put(new Character('ー'), new Point(128, 220));
        kana2Pos.put(new Character('」'), new Point(144, 220));
        
        kana2Pos.put(new Character('ガ'), new Point(80, 242));
        kana2Pos.put(new Character('ギ'), new Point(96, 242));
        kana2Pos.put(new Character('グ'), new Point(112, 242));
        kana2Pos.put(new Character('ゲ'), new Point(128, 242));
        kana2Pos.put(new Character('ゴ'), new Point(144, 242));
        
        kana2Pos.put(new Character('ザ'), new Point(80, 264));
        kana2Pos.put(new Character('ジ'), new Point(96, 264));
        kana2Pos.put(new Character('ズ'), new Point(112, 264));
        kana2Pos.put(new Character('ゼ'), new Point(128, 264));
        kana2Pos.put(new Character('ゾ'), new Point(144, 264));
        
        kana2Pos.put(new Character('ダ'), new Point(80, 286));
        kana2Pos.put(new Character('ヂ'), new Point(96, 286));
        kana2Pos.put(new Character('ヅ'), new Point(112, 286));
        kana2Pos.put(new Character('デ'), new Point(128, 286));
        kana2Pos.put(new Character('ド'), new Point(144, 286));
        
        kana2Pos.put(new Character('バ'), new Point(80, 308));
        kana2Pos.put(new Character('ビ'), new Point(96, 308));
        kana2Pos.put(new Character('ブ'), new Point(112, 308));
        kana2Pos.put(new Character('ベ'), new Point(128, 308));
        kana2Pos.put(new Character('ボ'), new Point(144, 308));
        
        kana2Pos.put(new Character('パ'), new Point(80, 330));
        kana2Pos.put(new Character('ピ'), new Point(96, 330));
        kana2Pos.put(new Character('プ'), new Point(112, 330));
        kana2Pos.put(new Character('ペ'), new Point(128, 330));
        kana2Pos.put(new Character('ポ'), new Point(144, 330));
        
        kana2Pos.put(new Character('0'), new Point(0, 352));        
        kana2Pos.put(new Character('1'), new Point(16, 352));
        kana2Pos.put(new Character('2'), new Point(32, 352));
        kana2Pos.put(new Character('3'), new Point(48, 352));
        kana2Pos.put(new Character('4'), new Point(64, 352));
        kana2Pos.put(new Character('5'), new Point(80, 352));
        kana2Pos.put(new Character('6'), new Point(96, 352));
        kana2Pos.put(new Character('7'), new Point(112, 352));
        kana2Pos.put(new Character('8'), new Point(128, 352));
        kana2Pos.put(new Character('9'), new Point(144, 352));
        
        kana2Pos.put(new Character('Ａ'), new Point(0, 374));        
        kana2Pos.put(new Character('Ｂ'), new Point(16, 374));
        kana2Pos.put(new Character('Ｃ'), new Point(32, 374));
        kana2Pos.put(new Character('Ｄ'), new Point(48, 374));
        kana2Pos.put(new Character('Ｅ'), new Point(64, 374));
        kana2Pos.put(new Character('Ｆ'), new Point(80, 374));
        kana2Pos.put(new Character('Ｇ'), new Point(96, 374));
        kana2Pos.put(new Character('Ｈ'), new Point(112, 374));
        kana2Pos.put(new Character('Ｉ'), new Point(128, 374));
        kana2Pos.put(new Character('Ｊ'), new Point(144, 374));
        
        kana2Pos.put(new Character('Ｋ'), new Point(0, 396));        
        kana2Pos.put(new Character('Ｌ'), new Point(16, 396));
        kana2Pos.put(new Character('Ｍ'), new Point(32, 396));
        kana2Pos.put(new Character('Ｎ'), new Point(48, 396));
        kana2Pos.put(new Character('Ｏ'), new Point(64, 396));
        kana2Pos.put(new Character('Ｐ'), new Point(80, 396));
        kana2Pos.put(new Character('Ｑ'), new Point(96, 396));
        kana2Pos.put(new Character('Ｒ'), new Point(112, 396));
        kana2Pos.put(new Character('Ｓ'), new Point(128, 396));
        kana2Pos.put(new Character('Ｔ'), new Point(144, 396));
        
        kana2Pos.put(new Character('Ｕ'), new Point(0, 418));        
        kana2Pos.put(new Character('Ｖ'), new Point(16, 418));
        kana2Pos.put(new Character('Ｗ'), new Point(32, 418));
        kana2Pos.put(new Character('Ｘ'), new Point(48, 418));
        kana2Pos.put(new Character('Ｙ'), new Point(64, 418));
        kana2Pos.put(new Character('Ｚ'), new Point(80, 418));
        kana2Pos.put(new Character('！'), new Point(96, 418));
        kana2Pos.put(new Character('？'), new Point(112, 418));
    }
}
