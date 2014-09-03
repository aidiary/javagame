import java.applet.Applet;
import java.applet.AudioClip;

/*
 * Created on 2005/07/03
 *
 */

/**
 * 二段ジャンプアイテム
 * @author mori
 *
 */
public class JumperTwo extends Sprite {
    // アイテムをとったときの音
    private AudioClip sound;

    public JumperTwo(double x, double y, String fileName, Map map) {
        super(x, y, fileName, map);
        
        // サウンドをロード
        sound = Applet.newAudioClip(getClass().getResource("se/pyoro57_b.wav"));
    }

    public void update() {
    }
    
    /**
     * サウンドを再生
     */
    public void play() {
        sound.play();
    }
    
    /**
     * アイテムを使う
     */
    public void use(Player player) {
        // プレイヤーが二段ジャンプ可能に！
        player.setJumperTwo(true);
    }
}
