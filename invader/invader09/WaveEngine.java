import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/*
 * Created on 2005/08/15
 *
 */

/**
 * @author mori
 *
 */
public class WaveEngine {
    // 登録できるWAVEファイルの最大数
    private static final int MAX_CLIPS = 256;

    // WAVEファイルデータ
    private static DataClip[] clips = new DataClip[MAX_CLIPS];
    // ライン（オーディオデータを再生する経路）
    private static SourceDataLine[] lines = new SourceDataLine[MAX_CLIPS];
    // 登録されたWAVEファイル数
    private static int counter = 0;
    
    private static long last;

    /**
     * WAVEファイルをロード
     * @param url WAVEファイルのURL
     */
    public static void load(URL url) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // オーディオストリームを開く
        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
        // WAVEファイルのフォーマットを取得
        AudioFormat format = ais.getFormat();
        // ラインを取得
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format, AudioSystem.NOT_SPECIFIED);

        // WAVEデータを取得
        DataClip clip = new DataClip(ais);
        
        // WAVEデータを登録
        clips[counter] = clip;
        lines[counter] = (SourceDataLine)AudioSystem.getLine(info);
        
        // ラインを開く
        lines[counter].open(format);

        counter++;
    }
    
    /**
     * WAVEファイルをロード
     * @param filename WAVEファイル名
     */
    public static void load(String filename) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        URL url = WaveEngine.class.getResource(filename);
        load(url);
    }
    
    /**
     * 再生開始、鳴らすにはゲームループでrender()が必要
     * @param no 再生するDataClipの番号
     */
    public static void play(int no) {
        if (clips[no] == null) {
            return;
        }
        
        clips[no].index = 0;
        clips[no].running = true;
    }
    
    /**
     * 停止
     * @param no 停止するDataClipの番号
     */
    public static void stop(int no) {
        if (clips[no] == null) {
            return;
        }
        
        clips[no].running = false;
    }
    
    /**
     * 再生
     */
    public static void render() {
        long current = System.currentTimeMillis();
        // 前回の呼び出しからの経過時刻
        int difference = (int)(current - last);
        
        for (int i=0; i<counter; i++) {
            // 再生が終わったのにラインが開いてるなら閉じる
            if (!clips[i].running && lines[i].isRunning()) {
                lines[i].stop();
            }
  
            // 再生中でないDataClipは飛ばす
            if (!clips[i].running) {
                continue;
            }

            // サンプルレートを計算する
            clips[i].calculateSampleRate(difference);
            
            // 1フレームで送信するバイト数を計算する
            // 残りバイト数の方が小さい場合はそっちを選ぶ
            int bytes = Math.min(clips[i].sampleRate, clips[i].data.length - clips[i].index);

            if (bytes > 0) {
                // ラインの再生バッファにbytesだけデータを書き込む
                // データを書き込むと再生される
                lines[i].write(clips[i].data, clips[i].index, bytes);
                // 再生したバイト分だけindexをすすめる
                clips[i].index += bytes;
            }
            
            // DataClipを全部再生したら停止
            if (clips[i].index >= clips[i].data.length) {
                clips[i].running = false;
            }

            // ラインを開始する
            if (!lines[i].isRunning()) {
                lines[i].start();
            }
        }
        
        last = current;
    }
}
