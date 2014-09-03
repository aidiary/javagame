import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JPanel;

/*
 * Created on 2005/12/10
 *
 */

/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel implements Runnable, ActionListener {

    // WAVEファイル名
    private static final String[] waveNames = {"attack.wav", "spell.wav", "escape.wav"};

    private JButton s1Button, s2Button, s3Button;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(400, 40));
        // パネルがキー入力を受け付けるようにする
        setFocusable(true);

        // GUIを作成
        initGUI();

        // サウンドをロード
        loadSound();

        MidiEngine.play(0);

        // ゲームループ開始
        Thread gameLoop = new Thread(this);
        gameLoop.start();
    }

    public void run() {
        while (true) {
            // サウンドのレンダリング（WAVEのみ必要）
            WaveEngine.render();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();
        
        if (button == s1Button) {
            WaveEngine.play(0);
        } else if (button == s2Button) {
            WaveEngine.play(1);
        } else if (button == s3Button) {
            WaveEngine.play(2);
        }
    }

    /**
     * GUIを初期化
     */
    private void initGUI() {
        s1Button = new JButton("こうげき");
        s2Button = new JButton("じゅもん");
        s3Button = new JButton("にげる");
        
        add(s1Button);
        add(s2Button);
        add(s3Button);
        
        s1Button.addActionListener(this);
        s2Button.addActionListener(this);
        s3Button.addActionListener(this);
    }

    /**
     * サウンドをロード
     */
    private void loadSound() {
        // WAVEをロード
        for (int i=0; i<waveNames.length; i++) {
            try {
                WaveEngine.load("wave/" + waveNames[i]);
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
        
        // MIDIをロード
        try {
            MidiEngine.load("midi/lovesong.mid");
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}