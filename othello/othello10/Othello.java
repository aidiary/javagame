/*
 * 作成日: 2004/12/17
 *
 */
import java.awt.*;
import javax.swing.*;
/**
 * オセロアプリケーション。
 * 
 * @author mori
 *  
 */
public class Othello extends JFrame {
    public Othello() {
        // タイトルを設定
        setTitle("α-β法");
        // サイズ変更をできなくする
        setResizable(false);

        Container contentPane = getContentPane();

        // 情報パネルを作成する
        InfoPanel infoPanel = new InfoPanel();
        contentPane.add(infoPanel, BorderLayout.NORTH);

        // メインパネルを作成してフレームに追加
        MainPanel mainPanel = new MainPanel(infoPanel);
        contentPane.add(mainPanel, BorderLayout.CENTER);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        Othello frame = new Othello();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}