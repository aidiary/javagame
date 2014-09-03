import java.applet.Applet;
import java.applet.AudioClip;

/*
 * Created on 2005/07/03
 *
 */

/**
 * 加速アイテム
 * @author mori
 *
 */
public class Accelerator extends Sprite {
    // アイテムをとったときの音
    private AudioClip sound;

    public Accelerator(double x, double y, String fileName, Map map) {
        super(x, y, fileName, map);
        
        // サウンドをロード
        sound = Applet.newAudioClip(getClass().getResource("se/chari13_c.wav"));
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
        // プレイヤーのスピードがアップ！
        player.setSpeed(player.getSpeed() * 2);
    }
}
