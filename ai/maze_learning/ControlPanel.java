import javax.swing.*;
import java.awt.event.*;

public class ControlPanel extends JPanel implements ActionListener {
    private MainPanel panel;

    private JButton skipButton;
    private JButton initButton;

    public ControlPanel(MainPanel panel) {
        this.panel = panel;

        initButton = new JButton("Init");
        skipButton = new JButton("Skip");

        initButton.addActionListener(this);
        skipButton.addActionListener(this);

        add(initButton);
        add(skipButton);
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
        }
    }
}
