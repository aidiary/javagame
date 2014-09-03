import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.PixelGrabber;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/*
 * Created on 2005/12/03
 *
 */

/**
 * @author mori
 *  
 */
public class MainPanel extends JPanel implements ActionListener {
    // パネルサイズ
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;

    // 破砕する画像
    private Image image;

    // 画像の幅と高さ
    private int width;
    private int height;

    // 画像の各破片（ピクセル）
    private Fraction[] fractions;

    // アニメーション用タイマー
    private Timer timer;

    // ボタン
    private JButton meganteButton;
    private JButton revivalButton;

    // 呪文の音
    private AudioClip spellClip;

    private Random rand = new Random();

    public MainPanel() {
        // パネルの推奨サイズを設定
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // 自由レイアウト
        setLayout(null);

        meganteButton = new JButton("メガンテ");
        meganteButton.addActionListener(this);
        add(meganteButton);
        meganteButton.setBounds(10, 10, 100, 30);

        revivalButton = new JButton("ザオリク");
        revivalButton.addActionListener(this);
        add(revivalButton);
        revivalButton.setBounds(120, 10, 100, 30);
        revivalButton.setEnabled(false);

        // イメージをロードする
        loadImage("suraimu.gif");

        // サウンドをロード
        spellClip = Applet.newAudioClip(getClass().getResource("spell.wav"));

        // イメージから破片を作成
        init(image);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == meganteButton) {
            spellClip.play();
            megante();
            meganteButton.setEnabled(false);
            revivalButton.setEnabled(true);
        } else if (e.getSource() == revivalButton) {
            spellClip.play();
            init(image);
            repaint();
            meganteButton.setEnabled(true);
            revivalButton.setEnabled(false);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 背景を塗りつぶす
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 破片の1つ1つを描画
        for (int n = 0; n < width * height; n++) {
            int x = (int) fractions[n].x;
            int y = (int) fractions[n].y;
            // ピクセル値から色を取り出す
            // 各ピクセルはARGBの順に並んでいる（各8ビット）
            int red = (fractions[n].color >> 16) & 0xff;
            int green = (fractions[n].color >> 8) & 0xff;
            int blue = (fractions[n].color) & 0xff;
            // 色を設定する
            g.setColor(new Color(red, green, blue));
            // 点を打つ
            g.drawLine(x, y, x, y);
        }
    }

    private void loadImage(String filename) {
        // スライムの画像をロードする
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        image = icon.getImage();
        width = image.getWidth(this);
        height = image.getHeight(this);
    }

    /**
     * 破片を初期化
     * 
     * @param image
     */
    private void init(Image image) {
        if (timer != null) {
            timer.cancel();
        }

        fractions = new Fraction[width * height];

        // イメージからPixelGrabberを生成
        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, true);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ピクセル列を取得して各ピクセルからFractionを作る
        int pixel[] = (int[]) pg.getPixels();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int n = y * width + x;
                fractions[n] = new Fraction();
                // 飛び散る方向をランダムに決める
                double angle = rand.nextInt(360);
                // 飛び散るスピードをランダムに決める
                double speed = 10.0 / rand.nextInt(30);
                // fractionに値をセット
                fractions[n].x = 184 + x;
                fractions[n].y = 184 + y;
                fractions[n].vx = Math.cos(angle * Math.PI / 180) * speed;
                fractions[n].vy = Math.sin(angle * Math.PI / 180) * speed;
                fractions[n].color = pixel[n];
                fractions[n].countToCrush = x / 6 + rand.nextInt(10);
            }
        }
    }

    /**
     * メガンテ！飛び散るアニメーションを開始
     */
    private void megante() {
        TimerTask task = new CrashTask();
        timer = new Timer();
        timer.schedule(task, 0, 20L);
    }

    class CrashTask extends TimerTask {
        public void run() {
            for (int n = 0; n < width * height; n++) {
                if (fractions[n].countToCrush <= 0) { // カウント0の破片
                    // 破片を移動する
                    fractions[n].x += fractions[n].vx;
                    fractions[n].y += fractions[n].vy;
                    fractions[n].vy += 0.1; // 下向きに加速度をかける
                } else {
                    // 破片が飛び散るまでのカウントダウン！
                    fractions[n].countToCrush--;
                }
            }

            repaint();
        }
    }
}