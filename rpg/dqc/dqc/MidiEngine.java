package dqc;

import java.io.IOException;
import java.util.HashMap;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

public class MidiEngine implements MetaEventListener {
    // 再生終了メタイベント
    private static final int END_OF_TRACK = 47;

    // シーケンサー
    private Sequencer sequencer;
    // シンセサイザー
    private Synthesizer synthesizer;

    // 登録できるMIDIファイルの最大数
    private int maxSequences;
    // MIDIファイルデータ（名前->Sequence）
    private HashMap midiMap;
    // 登録されているMIDIファイル数
    private int counter = 0;

    // 現在再生中のMIDIシーケンス名
    String currentSequenceName = "";

    /**
     * コンストラクタ
     */
    public MidiEngine() {
        this(256);
    }

    /**
     * コンストラクタ
     * 
     * @param maxSequences
     *            登録できるMIDIファイルの最大数
     */
    public MidiEngine(int maxSequences) {
        this.maxSequences = maxSequences;
        midiMap = new HashMap(maxSequences);

        // シーケンサーとシンセサイザーを初期化
        initSequencer();
    }

    private void initSequencer() {
        try {
            // シーケンサを開く
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            // メタイベントリスナーを登録
            sequencer.addMetaEventListener(this);
            // シーケンサとシンセサイザーの接続
            if (!(sequencer instanceof Synthesizer)) { // J2SE 5.0用
                // シンセサイザーを開く
                synthesizer = MidiSystem.getSynthesizer();
                synthesizer.open();
                Receiver synthReceiver = synthesizer.getReceiver();
                Transmitter seqTransmitter = sequencer.getTransmitter();
                seqTransmitter.setReceiver(synthReceiver);
            } else { // J2SE 1.4.2以前
                // シーケンサとシンセサイザーは同じ
                synthesizer = (Synthesizer) sequencer;
            }
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * MIDIファイルをロード
     * 
     * @param name
     *            登録名
     * @param filename
     *            ファイル名
     */
    public void load(String name, String filename) {
        if (counter == maxSequences) {
            System.out.println("エラー: これ以上登録できません");
            return;
        }

        try {
            // MIDIファイルをロード
            Sequence seq = MidiSystem.getSequence(getClass().getClassLoader().getResourceAsStream(filename));
            // 登録
            midiMap.put(name, seq);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 再生
     * 
     * @param name
     *            登録名
     */
    public void play(String name) {
        // 現在再生中のシーケンスと同名なら何もしない
        if (currentSequenceName.equals(name)) {
            return;
        }

        // 現在再生中のシーケンスを停止する
        stop();

        // 名前に対応するMIDIを取得
        Sequence seq = (Sequence)midiMap.get(name);  // MIDIシーケンス
        if (sequencer != null && seq != null) {
            try {
                sequencer.setSequence(seq);  // シーケンサにセット
                sequencer.start();  // 再生開始！
                currentSequenceName = name;
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止
     */
    public void stop() {
        if (sequencer.isRunning()) {
            sequencer.stop();
        }
    }

    /**
     * 終了処理
     */
    public void close() {
        // 再生中のシーケンスを停止する
        stop();

        // シーケンサの終了処理
        sequencer.removeMetaEventListener(this);
        sequencer.close();
        sequencer = null;
    }

    public void meta(MetaMessage meta) {
        // 再生が終了した場合
        if (meta.getType() == END_OF_TRACK) {
            if (sequencer != null && sequencer.isOpen()) {
                // MIDIシーケンス再生位置に戻す
                sequencer.setMicrosecondPosition(0);
                // 最初から再生
                sequencer.start();
            }
        }
    }
}
