import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2006/08/17
 */

public class LightningTest extends JFrame {
	public LightningTest() {
		setTitle("àÓç»");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MainPanel mainPanel = new MainPanel();
		Container contentPane = getContentPane();
		contentPane.add(mainPanel);

		pack();
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new LightningTest();
	}
}
