import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

/*
 * Created on 2005/12/25
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
    private JMenuItem openItem;  // 開く
    private JMenuItem saveItem;  // 保存

    // MapSizeDialogから取得した行数・列数
    private int row, col;

    // スクロールペイン
    private JScrollPane scrollPane;

    // ファイル選択ダイアログ（カレントディレクトリが始点）
    private JFileChooser fileChooser = new JFileChooser(".");

    public MapEditor() {
        setTitle("マップを開く");

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
        openItem = new JMenuItem("開く");
        saveItem = new JMenuItem("保存");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);

        newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newMap();
            }
        });

        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openMap();
            }
        });

        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveMap();
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

    /**
     * メニュー->開く
     * マップを開く
     */
    public void openMap() {
        fileChooser.addChoosableFileFilter(new MapFileFilter());  // ファイルフィルタ
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setDialogTitle("マップを開く");
        int ret = fileChooser.showOpenDialog(null);
        File mapFile;
        if (ret == JFileChooser.APPROVE_OPTION) {
            // もし開くボタンが押されたらそのマップファイルをロードする
            mapFile = fileChooser.getSelectedFile();
            mainPanel.loadMap(mapFile);
            // パネルが大きくなったらスクロールバーを表示する
            scrollPane.getViewport().revalidate();
            scrollPane.getViewport().repaint();
        }
    }

    /**
     * メニュー->保存
     * マップを保存する
     */
    public void saveMap() {
        fileChooser.addChoosableFileFilter(new MapFileFilter());  // ファイルフィルタ
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setDialogTitle("マップを保存する");
        int ret = fileChooser.showSaveDialog(null);
        File mapFile;
        if (ret == JFileChooser.APPROVE_OPTION) {
            // もし保存ボタンが押されたらそのマップファイルをセーブする
            mapFile = fileChooser.getSelectedFile();
            mainPanel.saveMap(mapFile);
        }
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

    /**
     * マップファイルフィルタ（.mapファイルとフォルダだけ表示する）
     */
    private class MapFileFilter extends FileFilter {
        public boolean accept(File file) {
            String extension = "";  // 拡張子
            if (file.getPath().lastIndexOf('.') > 0) {
                extension = file.getPath().substring(file.getPath().lastIndexOf('.') + 1).toLowerCase();
            }
            if (extension != "") {
                return extension.equals("map");  // mapだったらtrueを返す
            } else {
                return file.isDirectory();  // ディレクトリだったらtrueを返す
            }
        }
        
        public String getDescription() {
            return "Map Files (*.map)";
        }
    }
}