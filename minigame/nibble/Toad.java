/*
 * 作成日: 2004/12/14
 *
 */
import java.awt.*;
import java.applet.*;
/**
 * 蛙を表すクラス
 * 
 * @author mori
 *  
 */
public abstract class Toad {
    // 蛙の座標
    protected Point pos;
    // 蛙のイメージ
    protected Image toadImage;
    // 蛙の鳴き声のサウンド
    protected AudioClip geko;
    // エネルギー（蛇が食べたときに身体が伸びる大きさ）
    protected int energy;

    /**
     * posの位置に蛙を作成する
     * 
     * @param energy 蛙のエネルギー
     * @param pos 蛙の座標
     * @param panel メインパネルへの参照
     */
    public Toad(int energy, Point pos, MainPanel panel) {
        this.energy = energy;
        this.pos = pos;
        loadImage(panel);
        geko = Applet.newAudioClip(getClass().getResource("geko.wav"));
    }

    /**
     * 蛙の座標を返す
     * 
     * @return 蛙の座標
     */
    public Point getPos() {
        return pos;
    }

    /**
     * 蛙を鳴かせる
     *  
     */
    public void croak() {
        geko.play();
    }

    /**
     * 蛙を描画する
     * 
     * @param g 描画オブジェクト
     *  
     */
    public void draw(Graphics g) {
        g.drawImage(toadImage,
                pos.x * MainPanel.GS,
                pos.y * MainPanel.GS, null);
    }

    /**
     * エネルギーの大きさを返す。
     * @return エネルギーの大きさ。
     */
    public int getEnergy() {
        return energy;
    }
    
    /**
     * 蛙を移動する
     *  
     */
    public abstract void move();
    
    /**
     * 蛙の画像をロードする
     * 
     * @param panel MainPanelへの参照
     */
    protected abstract void loadImage(MainPanel panel);
}