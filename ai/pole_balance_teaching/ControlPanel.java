/*
 * 作成日: 2004/12/06
 *
 */
import javax.swing.*;
import java.awt.event.*;

/**
 * アプリケーションを操作するボタンを配置したパネル。
 * @author mori
 *  
 */
public class ControlPanel extends JPanel implements ActionListener {
    private MainPanel panel;

    private JButton skipButton;
    private JButton initButton;
    private JButton modeButton;

    public ControlPanel(MainPanel panel) {
        this.panel = panel;

        initButton = new JButton("Init");
        skipButton = new JButton("Skip");
        modeButton = new JButton("Mode");

        initButton.setFocusable(false);
        skipButton.setFocusable(false);
        modeButton.setFocusable(false);

        initButton.addActionListener(this);
        skipButton.addActionListener(this);
        modeButton.addActionListener(this);

        add(initButton);
        add(skipButton);
        add(modeButton);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == initButton) {
            panel.init();
        } else if (e.getSource() == skipButton) {
            if (skipButton.getText() == "Skip") {
                skipButton.setText("No Skip");
                initButton.setEnabled(false);
                panel.skip(true);
                panel.repaint();
            } else if (skipButton.getText() == "No Skip") {
                skipButton.setText("Skip");
                initButton.setEnabled(true);
                panel.skip(false);
            }
        } else if (e.getSource() == modeButton) {
            panel.changeMode();
        }
    }
}