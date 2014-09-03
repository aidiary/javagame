package dqc;

import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WaveEngine implements LineListener {
    // 登録できるWAVEファイルの最大数
    private int maxClips;
    // WAVEファイルデータ（名前->データ本体）
    private HashMap clipMap;
    // 登録されたWAVEファイル数
    private int counter = 0;

    /**
     * コンストラクタ
     */
    public WaveEngine() {
        this(256);
    }

    /**
     * コンストラクタ
     * 
     * @param maxClips
     *            登録できるWAVEファイルの最大数
     */
    public WaveEngine(int maxClips) {
        this.maxClips = maxClips;
        clipMap = new HashMap(maxClips);
    }

    /**
     * WAVEファイルをロード
     * @param name 登録名
     * @param filename ファイル名
     */
    public void load(String name, String filename) {
        if (counter == maxClips) {
            System.out.println("エラー: これ以上登録できません");
            return;
        }

        try {
            // オーディオストリームを開く
            AudioInputStream stream = AudioSystem
                    .getAudioInputStream(getClass().getClassLoader().getResource(filename));

            // オーディオ形式を取得
            AudioFormat format = stream.getFormat();
            // ULAW/ALAW形式の場合はPCM形式に変更
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW)
                    || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat newFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        format.getSampleRate(),
                        format.getSampleSizeInBits() * 2, format.getChannels(),
                        format.getFrameSize() * 2, format.getFrameRate(), true);
                stream = AudioSystem.getAudioInputStream(newFormat, stream);
                format = newFormat;
            }

            // ライン情報を取得
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            // サポートされてる形式かチェック
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("エラー: " + filename + "はサポートされていない形式です");
                System.exit(0);
            }

            // 空のクリップを作成
            Clip clip = (Clip) AudioSystem.getLine(info);
            // クリップのイベントを監視
            clip.addLineListener(this);
            // オーディオストリームをクリップとして開く
            clip.open(stream);
            // クリップを登録
            clipMap.put(name, clip);
            // ストリームを閉じる
            stream.close();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * 再生
     * @param name 登録名
     */
    public void play(String name) {
        // 名前に対応するクリップを取得
        Clip clip = (Clip)clipMap.get(name);
        if (clip != null) {
            clip.start();
        }
    }

    public void update(LineEvent event) {
        // ストップか最後まで再生された場合
        if (event.getType() == LineEvent.Type.STOP) {
            Clip clip = (Clip) event.getSource();
            clip.stop();
            clip.setFramePosition(0); // 再生位置を最初に戻す
        }
    }
}
