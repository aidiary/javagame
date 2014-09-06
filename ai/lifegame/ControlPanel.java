/*
 * 作成日: 2004/10/15
 *
 */
import javax.swing.*;
import java.awt.event.*;
/**
 * ライフゲームコントロールパネル。
 * 
 * @author mori
 *  
 */
public class ControlPanel extends JPanel implements ActionListener {
    // メインパネルへの参照
    private MainPanel panel;

    // STARTボタン
    private JButton startButton;
    // STOPボタン
    private JButton stopButton;
    // STEPボタン
    private JButton stepButton;
    // CLEARボタン
    private JButton clearButton;
    // SAVEボタン
    private JButton saveButton;
    // RANDボタン
    private JButton randButton;

    /**
     * コンストラクタ。
     * 
     * @param panel メインパネルへの参照。
     */
    public ControlPanel(MainPanel panel) {
        this.panel = panel;

        // ボタンを作成
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        stepButton = new JButton("Step");
        clearButton = new JButton("Clear");
        saveButton = new JButton("Save");
        randButton = new JButton("Rand");

        // イベントハンドラを追加する
        startButton.addActionListener(this);
        stopButton.addActionListener(this);
        stepButton.addActionListener(this);
        clearButton.addActionListener(this);
        saveButton.addActionListener(this);
        randButton.addActionListener(this);

        // ボタンを配置する
        add(startButton);
        add(stopButton);
        add(stepButton);
        add(clearButton);
        add(saveButton);
        add(randButton);

        // ストップボタンは最初は押せない
        stopButton.setEnabled(false);
    }

    /**
     * ボタンのイベントハンドラ。
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            // STARTボタンが押された場合
            // ボタンの押せる押せないをセット
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            stepButton.setEnabled(false);
            clearButton.setEnabled(false);
            panel.start();
        } else if (e.getSource() == stopButton) {
            // STOPボタンが押された場合
            // ボタンの押せる押せないをセット
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            stepButton.setEnabled(true);
            clearButton.setEnabled(true);
            panel.stop();
        } else if (e.getSource() == stepButton) {
            // STEPボタンが押された場合
            // 1世代進める
            panel.step();
        } else if (e.getSource() == clearButton) {
            // CLEARボタンが押された場合
            // フィールドをクリア
            panel.clear();
        } else if (e.getSource() == saveButton) {
            // SAVEボタンが押された場合
            // ライフを保存
            panel.saveLife();
        } else if (e.getSource() == randButton) {
            panel.randLife();
        }
    }
}