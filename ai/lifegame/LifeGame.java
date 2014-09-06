/*
 * 作成日: 2004/10/15
 *
 */
import java.awt.*;
import javax.swing.*;
/**
 * ライフゲームアプリケーション。
 * 
 * @author mori
 *  
 */
public class LifeGame extends JFrame {
    public LifeGame() {
        // タイトルを設定
        setTitle("ライフゲーム");
        // サイズ変更を不可能にする
        setResizable(false);

        // メインパネルを作成
        MainPanel mainPanel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(mainPanel, BorderLayout.CENTER);

        // コントロールパネルを作成
        ControlPanel ctrlPanel = new ControlPanel(mainPanel);
        contentPane.add(ctrlPanel, BorderLayout.NORTH);

        // ライフゲームの情報パネル
        InfoPanel infoPanel = new InfoPanel(mainPanel);
        contentPane.add(infoPanel, BorderLayout.SOUTH);

        // メインパネルに情報パネルを渡す
        mainPanel.setInfoPanel(infoPanel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        LifeGame frame = new LifeGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}