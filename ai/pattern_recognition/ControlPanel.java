import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/*
 * Created on 2005/05/04
 *
 */

/**
 * @author mori
 *
 */
public class ControlPanel extends JPanel implements ActionListener {
    private MainPanel mainPanel;
    
    private JButton addButton;
    private JButton clearButton;
    private JButton learnButton;
    private JButton recognizeButton;
    
    public ControlPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        
        addButton = new JButton("í«â¡");
        clearButton = new JButton("è¡ãé");
        learnButton = new JButton("äwèK");
        recognizeButton = new JButton("îFéØ");
        
        addButton.addActionListener(this);
        clearButton.addActionListener(this);
        learnButton.addActionListener(this);
        recognizeButton.addActionListener(this);
        
        learnButton.setEnabled(false);
        recognizeButton.setEnabled(false);
        
        add(addButton);
        add(clearButton);
        add(learnButton);
        add(recognizeButton);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            mainPanel.addPattern();
            learnButton.setEnabled(true);
        } else if (e.getSource() == clearButton) {
            mainPanel.clearPattern();
        } else if (e.getSource() == learnButton) {
            mainPanel.learnPattern();
            addButton.setEnabled(false);
            learnButton.setEnabled(false);
            recognizeButton.setEnabled(true);
        } else if (e.getSource() == recognizeButton) {
            mainPanel.recognizePattern();
        }
    }
}
