/*
 * 作成日: 2004/12/06
 *
 */
import javax.swing.*;
/**
 * エピソード数やバウンド数を表示するパネル。
 * @author mori
 *  
 */
public class InfoPanel extends JPanel {
    private JLabel episodeLabel;
    private JLabel boundLabel;

    public InfoPanel() {
        add(new JLabel("Episode: "));
        episodeLabel = new JLabel("0");
        add(episodeLabel);
        boundLabel = new JLabel("0");
        add(new JLabel("Bound: "));
        add(boundLabel);
    }

    /**
     * エピソードラベルに値をセットする。
     * 
     * @param text セットするテキスト。
     */
    public void setEpisodeLabel(String text) {
        episodeLabel.setText(text);
    }

    /**
     * バウンドラベルに値をセットする。
     * 
     * @param text セットするテキスト。
     */
    public void setBoundLabel(String text) {
        boundLabel.setText(text);
    }
}