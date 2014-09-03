import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

/*
 * Created on 2007/05/04
 */

public class Wizard {
    private BufferedImage wizardImg;
    private AudioClip spellClip;

    // アルファ値（alpha=0.0で透明、alpha=1.0で不透明）
    private float alpha = 1.0f;
    
    private MainPanel panel;

    public Wizard(MainPanel panel) {
        this.panel = panel;

        // イメージをロード
        try {
            wizardImg = ImageIO.read(getClass().getResource("wizard.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // サウンドをロード
        spellClip = Applet.newAudioClip(getClass().getResource("spell.wav"));
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // アルファ値は0.0-1.0
        if (alpha < 0.0f)
            alpha = 0.0f;
        else if (alpha > 1.0f)
            alpha = 1.0f;

        // 現在のCompositeを保存
        Composite comp = g2d.getComposite();
        // アルファ値をセット
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.drawImage(wizardImg, 70, 70, null);
        // 元のCompositeに戻す
        g2d.setComposite(comp);
    }

    public void remuoru() {
        System.out.println("魔法使いはレムオルを唱えた！");

        alpha = 1.0f;
        spellClip.play();

        // alpha値を減らすタイマータスクを起動
        TimerTask task = new FadeOutTask();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 100);
    }

    private class FadeOutTask extends TimerTask {
        public void run() {
            alpha -= 0.05f;
            panel.repaint();

            // 完全に消えたらタスク終了
            if (alpha < 0) {
                cancel();
            }
        }
    }
}
