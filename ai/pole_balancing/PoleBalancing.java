import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

/*
 * 作成日: 2004/12/07
 *
 */

/**
 * アプリケーションクラス。
 * @author mori
 *
 */
public class PoleBalancing extends JFrame {
    public PoleBalancing() {
        // タイトルを設定
        setTitle("倒立振子制御問題");

        Container contentPane = getContentPane();
        
        // インフォメーションパネルを作成
        InfoPanel infoPanel = new InfoPanel();
        contentPane.add(infoPanel, BorderLayout.NORTH);
        
        // メインパネルを作成
        MainPanel mainPanel = new MainPanel();
        contentPane.add(mainPanel, BorderLayout.CENTER);
        // メインパネルにインフォメーションパネルを渡す
        mainPanel.setInfoPanel(infoPanel);
        
        // コントロールパネルを作成
        ControlPanel controlPanel = new ControlPanel(mainPanel);
        contentPane.add(controlPanel, BorderLayout.SOUTH);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        PoleBalancing frame = new PoleBalancing();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

