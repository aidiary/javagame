/*
 * 作成日: 2004/12/18
 *
 */
import javax.swing.*;
/**
 * @author mori
 *  
 */
public class InfoPanel extends JPanel {
    private JLabel blackLabel;
    private JLabel whiteLabel;

    public InfoPanel() {
        add(new JLabel("BLACK:"));
        blackLabel = new JLabel("0");
        add(blackLabel);
        add(new JLabel("WHITE:"));
        whiteLabel = new JLabel("0");
        add(whiteLabel);
    }

    /**
     * BLACKラベルに値をセットする。
     * 
     * @param count セットする数字。
     *  
     */
    public void setBlackLabel(int count) {
        blackLabel.setText(count + "");
    }

    /**
     * WHITEラベルに値をセットする。
     * 
     * @param text セットする数字。
     *  
     */
    public void setWhiteLabel(int count) {
        whiteLabel.setText(count + "");
    }
}