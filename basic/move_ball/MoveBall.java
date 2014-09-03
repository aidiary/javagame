import java.awt.*;
import javax.swing.*;

public class MoveBall extends JFrame {
    public MoveBall() {
        // タイトルを設定
        setTitle("ボールを動かす");

        // サイズ変更禁止
        setResizable(false);

        // パネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        MoveBall frame = new MoveBall();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class MainPanel extends JPanel implements Runnable {
    // パネルサイズ
    private static final int WIDTH = 240;
    private static final int HEIGHT = 240;
    // ボールの大きさ
    private static final int SIZE = 10;
    // ボールの位置 (x, y)、円の中心の座標
    private int x;
    private int y;
    // ボールの速度 (vx, vy)
    private int vx;
    private int vy;
    // アニメーション用スレッド
    private Thread thread;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // 変数を初期化
        // ボールは画面の左上に配置
        x = 100;
        y = 100;
        vx = 1;
        vy = 1;

        // スレッドを起動
        thread = new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 青いボールを描く
        g.setColor(Color.BLUE);
        g.fillOval(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
    }

    // メインループ
    public void run() {
        // プログラムが終了するまでフレーム処理を繰り返す
        while (true) {
            // ボールを速度分だけ移動させる
            x += vx;
            y += vy;

            // ボールを再描画
            repaint();

            // 20ミリ秒だけ休止
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
