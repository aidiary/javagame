import java.applet.Applet;
import java.applet.AudioClip;

/*
 * Created on 2005/06/24
 *
 */

/**
 * @author mori
 *
 */
public class Coin extends Sprite {
    // コインをとったときの音
    private AudioClip sound;

    public Coin(double x, double y, String fileName, Map map) {
        super(x, y, fileName, map);
        
        // サウンドをロード
        sound = Applet.newAudioClip(getClass().getResource("se/coin03.wav"));
    }

    public void update() {
    }
    
    /**
     * サウンドを再生
     */
    public void play() {
        sound.play();
    }
}
