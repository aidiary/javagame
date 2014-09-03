import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * Created on 2005/05/04
 *
 */

/**
 * @author mori
 *
 */
public class InfoPanel extends JPanel {
    private JTextArea infoArea;
    
    public InfoPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(320, 240));
        
        setLayout(new BorderLayout());
        infoArea = new JTextArea();
        infoArea.setEditable(true);
        infoArea.setLineWrap(true);

        add(new JScrollPane(infoArea), BorderLayout.CENTER);
    }
    
    public void print(String str) {
        infoArea.append(str + "\n");
    }
}
