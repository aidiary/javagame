import java.applet.*;

public class SoundBall extends Ball {
    // サウンド
    private AudioClip pong;

    public SoundBall(int x, int y, int vx, int vy) {
        super(x, y, vx, vy);
        // サウンドをロード
        pong = Applet.newAudioClip(getClass().getResource("pong.wav"));
    }

    // move()をオーバーライド
    public void move() {
        // ボールを速度分だけ移動させる
        x += vx;
        y += vy;

        // 左または右に当たったらx方向速度の符号を反転させる
        if (x < 0 || x > MainPanel.WIDTH - SIZE) {
            // 壁に当たったら音を鳴らす
            pong.play();
            vx = -vx;
        }

        // 上または下に当たったらy方向速度の符号を反転させる
        if (y < 0 || y > MainPanel.HEIGHT - SIZE) {
            // 壁に当たったら音を鳴らす
            pong.play();
            vy = -vy;
        }
    }
}
