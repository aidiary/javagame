/*
 * 作成日: 2004/12/17
 *
 */
import java.awt.*;
import javax.swing.*;
/**
 * オセロアプリケーションです。
 * @author mori
 *
 */
public class Othello extends JFrame {
    public Othello() {
        // タイトルを設定
        setTitle("石をひっくり返す");
        // サイズ変更をできなくする
        setResizable(false);
        // メインパネルを作成してフレームに追加
        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    public static void main(String[] args) {
        Othello frame = new Othello();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
