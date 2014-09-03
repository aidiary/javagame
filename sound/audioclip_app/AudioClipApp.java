import java.applet.Applet;
import java.applet.AudioClip;

import javax.swing.JFrame;

/*
 * Created on 2006/11/03
 */

public class AudioClipApp extends JFrame {
    public AudioClipApp(String filename) {
        setTitle("AudioClipÇÃçƒê∂");

        AudioClip clip = Applet.newAudioClip(getClass().getResource(filename));
        clip.play();
    }

    public static void main(String[] args) {
        AudioClipApp app = new AudioClipApp("elise.mid");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }
}
