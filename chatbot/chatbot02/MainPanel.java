import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * Created on 2007/03/10
 */

public class MainPanel extends JPanel implements ActionListener {
    private static final int WIDTH = 480;
    private static final int HEIGHT = 480;

    // メッセージ履歴表示エリア
    private JTextArea dialogueArea;
    // メッセージ入力フィールド
    private JTextField inputField;

    private Chatbot chatbot = new Chatbot("人工無脳2号");

    public MainPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        initGUI();
    }

    /**
     * GUIを初期化する
     */
    private void initGUI() {
        setLayout(new BorderLayout());

        // メッセージ履歴表示エリア
        dialogueArea = new JTextArea();
        dialogueArea.setEditable(false);
        dialogueArea.setLineWrap(true);
        dialogueArea.append("人工無脳プロトタイプ\n\n");

        // メッセージ入力フィールド
        inputField = new JTextField("メッセージを入力してください");
        inputField.selectAll();

        // パネルに追加
        JScrollPane scrollPane = new JScrollPane(dialogueArea);
        scrollPane.setAutoscrolls(true);
        add(scrollPane, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        // 入力メッセージを取得
        String input = inputField.getText();
        dialogueArea.append("あなた\t" + input + "\n");

        // 人工無脳の反応メッセージを取得
        String response = chatbot.getResponse(input);
        dialogueArea.append(chatbot.getName() + "\t" + response + "\n");
        dialogueArea.setCaretPosition(dialogueArea.getText().length());

        inputField.setText("");
    }
}
