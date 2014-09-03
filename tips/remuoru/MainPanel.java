import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/*
 * Created on 2007/05/04
 */

public class MainPanel extends JPanel implements ActionListener {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;

    private JButton remuoruButton;
    private Wizard wizard;

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        wizard = new Wizard(this);

        remuoruButton = new JButton("ÉåÉÄÉIÉãÅI");
        remuoruButton.addActionListener(this);
        add(remuoruButton);
        remuoruButton.setBounds(10, 10, 100, 30);
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        wizard.draw(g);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == remuoruButton) {
            wizard.remuoru();
        }
    }
}
