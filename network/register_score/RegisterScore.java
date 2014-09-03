import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/*
 * Created on 2005/07/30
 *
 */

/**
 * @author mori
 *
 */
public class RegisterScore {
    public static void main(String[] args) {
        // ななしの得点が9999だったというデータ
        String data = "name=ななし&score=9999";
        try {
            // CGIを表すURLオブジェクト
            URL cgiURL = new URL("http://javagame.main.jp/cgi-bin/score/reg_score.cgi");
            // 接続
            URLConnection uc = cgiURL.openConnection();
            uc.setDoOutput(true);
            uc.setUseCaches(false);
            // CGIへの書き込み用ストリームを開く
            PrintWriter pw = new PrintWriter(uc.getOutputStream());
            // CGIにデータを送信する
            pw.print(data);
            // ストリームを閉じる
            pw.close();
            
            // CGIからの読み込み用ストリームを開く
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            // CGIの出力を読み込んでコンソール画面に表示
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            // ストリームを閉じる
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
