/*
 * Created on 2007/03/25
 */
package mapeditor;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanel extends JPanel {
    private JLabel posLabel;
    
    public InfoPanel() {
        add(new JLabel("ç¿ïW: "));
        posLabel = new JLabel("(0, 0)   (0, 0)");
        add(posLabel);
    }
    
    public void setPos(int x, int y, int mouseX, int mouseY) {
        posLabel.setText("(" + x + ", " + y + ")" + "   " + "(" + mouseX + ", " + mouseY + ")");
    }
}
