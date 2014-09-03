import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/*
 * Created on 2006/11/05
 */

public class MidiEngineTest extends JFrame {
    private static final String[] midiNames = {"春の吹く場所で", "夏の恋", "秋の野", "冬の星"};
    private MidiEngine midiEngine = new MidiEngine();

    public MidiEngineTest() {
        setTitle("MIDIエンジンテスト");
        setResizable(false);

        // コントロールパネルを追加
        ControlPanel controlPanel = new ControlPanel(midiEngine);
        Container container = getContentPane();
        container.add(controlPanel);

        // MIDIファイルをロード
        loadMidi();

        pack();

        // ウィンドウを閉じたとき
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                midiEngine.close();  // シーケンサーを閉じる
                System.exit(0);
            }
        });
    }

    /**
     * MIDIファイルをロード
     */
    private void loadMidi() {
        // WAVEをロード
        for (int i = 0; i < midiNames.length; i++) {
            midiEngine.load(midiNames[i], "midi/" + midiNames[i] + ".mid");
        }
    }

    public static void main(String[] args) {
        MidiEngineTest frame = new MidiEngineTest();
        frame.setVisible(true);
    }
}
