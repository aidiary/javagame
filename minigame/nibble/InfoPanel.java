/*
 * 作成日: 2004/12/14
 *
 */
import javax.swing.*;
/**
 * 蛇の長さ、得点、残り時間を表示するパネル
 * @author mori
 *
 */
public class InfoPanel extends JPanel {
    private JLabel lengthLabel;
    private JLabel scoreLabel;
    private JLabel timeLabel;

    public InfoPanel() {
        add(new JLabel("LENGTH: "));
        lengthLabel = new JLabel("0");
        add(lengthLabel);
        
        add(new JLabel("SCORE: "));
        scoreLabel = new JLabel("0");
        add(scoreLabel);
        
        add(new JLabel("TIME: "));
        timeLabel = new JLabel("0");
        add(timeLabel);
    }

    /**
     * LENGTHラベルに値をセットする。
     * 
     * @param text セットするテキスト。
     */
    public void setLengthLabel(String text) {
        lengthLabel.setText(text);
    }

    /**
     * SCOREラベルに値をセットする。
     * 
     * @param text セットするテキスト。
     */
    public void setScoreLabel(String text) {
        scoreLabel.setText(text);
    }
    
    /**
     * TIMEラベルに値をセットする
     * 
     * @param text
     */
    public void setTimeLabel(String text) {
        timeLabel.setText(text);
    }
}
