import javax.swing.*;

public class InfoPanel extends JPanel {
    private JLabel episodeLabel;
    private JLabel stepLabel;

    public InfoPanel() {
        add(new JLabel("Episode: "));
        episodeLabel = new JLabel("0");
        add(episodeLabel);
        stepLabel = new JLabel("0");
        add(new JLabel("Step: "));
        add(stepLabel);
    }

    /**
     * エピソードラベルに値をセットする。
     * @param text セットするテキスト。
     */
    public void setEpisodeLabel(String text) {
        episodeLabel.setText(text);
    }

    /**
     * ステップラベルに値をセットする。
     * @param text セットするテキスト。
     */
    public void setStepLabel(String text) {
        stepLabel.setText(text);
    }
}
