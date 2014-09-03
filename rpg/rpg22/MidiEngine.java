import java.io.IOException;
import java.net.URL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/*
 * Created on 2005/08/15
 *
 */

/**
 * @author mori
 *
 */
public class MidiEngine {
    // 登録できるMIDIファイルの最大数
    private static final int MAX_SEQUENCE = 256;
    // MIDIメタイベント
    private static final int END_OF_TRACK_MESSAGE = 47;

    // MIDIシーケンス
    private static Sequence[] sequences = new Sequence[MAX_SEQUENCE];
    // MIDIシーケンサ
    private static Sequencer sequencer;

    // 登録されたMIDIファイルの数
    private static int counter = 0;

    // 再生中のMIDIシーケンスの登録番号
    private static int playSequenceNo = -1;

    // MIDIシーケンスの開始地点
    private static long startTick = 0;

    /**
     * MIDIファイルをロード
     * @param url MIDIファイルのURL
     */
    public static void load(URL url) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        if (sequencer == null) {
            // シーケンサを取得
            sequencer = MidiSystem.getSequencer();
            // シーケンサを開く
            sequencer.open();
            // メタイベントリスナーを登録
            sequencer.addMetaEventListener(new MyMetaEventListener());
        }

        // MIDIシーケンスを登録
        sequences[counter] = MidiSystem.getSequence(url);

        counter++;
    }
    
    /**
     * MIDIファイルをロード
     * @param filename MIDIファイル名
     */
    public static void load(String filename) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        URL url = MidiEngine.class.getResource(filename);
        load(url);
    }

    /**
     * 再生開始
     * @param no 登録番号
     */
    public static void play(int no) {
        // 登録されてなければ何もしない
        if (sequences[no] == null) {
            return;
        }
        
        // 現在再生中のMIDIファイルと同じ場合は何もしない
        if (playSequenceNo == no) {
            return;
        }

        // 別のMIDIシーケンスを再生する場合は
        // 現在再生中のシーケンスを停止する
        stop();

        try {
            // シーケンサにMIDIシーケンスをセット
            sequencer.setSequence(sequences[no]);
            // 登録番号を記憶
            playSequenceNo = no;
            // MIDIシーケンサのスタート地点を記録（ループできるように）
            startTick = sequencer.getMicrosecondPosition();
            // 再生開始
            sequencer.start();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 停止
     */
    public static void stop() {
        if (sequencer.isRunning()) {
            sequencer.stop();
        }
    }

    // メタイベントリスナー（ループ再生のため）
    private static class MyMetaEventListener implements MetaEventListener {
        public void meta(MetaMessage meta) {
            if (meta.getType() == END_OF_TRACK_MESSAGE) {
                if (sequencer != null && sequencer.isOpen()) {
                    // MIDIシーケンス再生位置を最初に戻す
                    sequencer.setMicrosecondPosition(startTick);
                    // 最初から再生
                    sequencer.start();
                }
            }
        } 
    }
}