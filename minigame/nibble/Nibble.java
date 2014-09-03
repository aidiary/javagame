/*
 * 作成日: 2004/12/14
 *
 */
import java.awt.*;
import javax.swing.*;
/**
 * Nibbleアプリケーション
 * 
 * @author mori
 *  
 */
public class Nibble extends JFrame {
    public Nibble() {
        // タイトルを設定
        setTitle("Nibble");

        Container contentPane = getContentPane();

        // インフォメーションパネルを作成
        InfoPanel infoPanel = new InfoPanel();
        contentPane.add(infoPanel, BorderLayout.NORTH);

        // メインパネルを作成してフレームに追加
        MainPanel mainPanel = new MainPanel(infoPanel);
        contentPane.add(mainPanel, BorderLayout.CENTER);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        Nibble frame = new Nibble();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}