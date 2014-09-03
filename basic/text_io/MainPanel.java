import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/*
 * Created on 2007/02/02
 */

public class MainPanel extends JPanel {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    private JTextArea textArea;
    private JButton readFileButton;
    private JButton writeFileButton;

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        initGUI();
    }

    /**
     * GUIを初期化する
     */
    private void initGUI() {
        textArea = new JTextArea();
        textArea.setEditable(true);

        readFileButton = new JButton("読み込み");
        writeFileButton = new JButton("書き込み");

        // 2つのボタンをまとめるパネル
        JPanel p1 = new JPanel();
        p1.add(readFileButton);
        p1.add(writeFileButton);

        setLayout(new BorderLayout());
        add(textArea, BorderLayout.CENTER);
        add(p1, BorderLayout.SOUTH);

        // 読み込みボタンを押したときの処理
        readFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // test.txtからファイルを読み込んでテキストエリアに表示
                    BufferedReader br = new BufferedReader(new FileReader("test.txt"));  // ファイルを開く
                    String line;
                    while ((line = br.readLine()) != null) {  // 1行ずつ読み込む
                        textArea.append(line + "\n");  // 読み込んだ1行をテキストエリアに表示
                    }
                    br.close();  // ファイルを閉じる
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // 書き込みボタンを押したときの処理
        writeFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // test.txtにテキストエリアの内容を書き込む
                    BufferedWriter bw = new BufferedWriter(new FileWriter("test.txt"));  // ファイルを開く
                    bw.write(textArea.getText());
                    bw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
