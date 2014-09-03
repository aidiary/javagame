import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/*
 * Created on 2005/12/23
 *
 */

/**
 * @author mori
 *  
 */
public class MapEditor extends JFrame {
    // メインパネル
    private MainPanel mainPanel;

    // マップチップパレット
    private PaletteDialog paletteDialog;

    // メニューアイテム
    private JMenuItem newItem; // 新規作成
    // MapSizeDialogから取得した行数・列数
    private int row, col;

    // スクロールペイン
    private JScrollPane scrollPane;

    public MapEditor() {
        setTitle("マップの新規作成");

        row = col = 16;  // デフォルト値
        initGUI();

        // パネルサイズに合わせてフレームサイズを自動設定
        pack();
    }

    /**
     * GUIを初期化
     */
    private void initGUI() {
        // マップチップパレットを作成
        paletteDialog = new PaletteDialog(this);
        paletteDialog.setVisible(true);

        // メインパネルを作成
        mainPanel = new MainPanel(paletteDialog);
        // メインパネルをスクロールペインの上に乗せる
        scrollPane = new JScrollPane(mainPanel);
        Container contentPane = getContentPane();
        contentPane.add(scrollPane);

        // ファイルメニュー
        JMenu fileMenu = new JMenu("ファイル");
        newItem = new JMenuItem("新規作成");
        fileMenu.add(newItem);
        newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newMap();
            }
        });

        // メニューバー
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    /**
     * メニュー->新規作成
     * 新しいマップを作成する
     */
    public void newMap() {
        // マップサイズダイアログを開く
        // rowとcolを設定
        MapSizeDialog dialog = new MapSizeDialog(this);
        dialog.setVisible(true);

        // ｷｬﾝｾﾙボタンが押されたときは何もしない
        if (!dialog.isOKPressed()) {
            return;
        }

        // メインパネルに新しいマップを作成
        mainPanel.initMap(row, col);

        // パネルの大きさをマップの大きさと同じにする
        // パネルが大きいときは自動的にスクロールバーが表示される
        mainPanel.setPreferredSize(new Dimension(col * MainPanel.CHIP_SIZE, row * MainPanel.CHIP_SIZE));
        // パネルが大きくなったらスクロールバーを表示する
        scrollPane.getViewport().revalidate();
        scrollPane.getViewport().repaint();
    }

    public static void main(String[] args) {
        MapEditor frame = new MapEditor();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * メニュー->新規作成で表示されるダイアログ
     * 行数と列数を入力してrowとcolを設定する
     */
    private class MapSizeDialog extends JDialog implements ActionListener {
        private JTextField rowTextField;
        private JTextField colTextField;
        private JButton okButton;
        private JButton cancelButton;

        // OKボタンが押されたらtrueになる
        // ｷｬﾝｾﾙボタンならfalseになる
        private boolean isOKPressed;;

        public MapSizeDialog(JFrame owner) {
            super(owner, "マップ作成", true);
            
            isOKPressed = false;

            rowTextField = new JTextField(4);
            colTextField = new JTextField(4);
            okButton = new JButton("OK");
            cancelButton = new JButton("ｷｬﾝｾﾙ");
            okButton.addActionListener(this);
            cancelButton.addActionListener(this);

            JPanel p1 = new JPanel();
            p1.add(new JLabel("行数"));
            p1.add(rowTextField);

            JPanel p2 = new JPanel();
            p2.add(new JLabel("列数"));
            p2.add(colTextField);

            JPanel p3 = new JPanel();
            p3.add(okButton);
            p3.add(cancelButton);

            Container contentPane = getContentPane();
            contentPane.setLayout(new GridLayout(3, 1));
            contentPane.add(p1);
            contentPane.add(p2);
            contentPane.add(p3);

            pack();
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == okButton) {
                try {
                    // MapEditorクラスのインスタンス変数rowとcolを設定
                    row = Integer.parseInt(rowTextField.getText());
                    col = Integer.parseInt(colTextField.getText());
                    // マップは最低16x16のサイズ
                    if (row < 16 || col < 16) {
                        JOptionPane.showMessageDialog(MapSizeDialog.this,
                                "マップの大きさは16x16以上にしてください");
                        row = col = 16;
                        return;
                    }
                    // マップの最大サイズは256x256
                    if (row > 256 || col > 256) {
                        JOptionPane.showMessageDialog(MapSizeDialog.this,
                                "マップの大きさは256x256以内にしてください");
                        row = col = 256;
                        return;
                    }
                } catch (NumberFormatException ex) {
                    // テキストボックスに数値以外が入力されたとき
                    JOptionPane.showMessageDialog(MapSizeDialog.this,
                            "数値を入力してください");
                    rowTextField.setText("");
                    colTextField.setText("");
                    return;
                }
                isOKPressed = true;
                setVisible(false);
            } else if (e.getSource() == cancelButton) {
                isOKPressed = false;
                // ｷｬﾝｾﾙなら何もしない
                setVisible(false);
            }
        }
        
        public boolean isOKPressed() {
            return isOKPressed;
        }
    }
}