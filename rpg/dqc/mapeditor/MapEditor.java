/*
 * マップエディタメインフレーム
 */
package mapeditor;

import java.awt.BorderLayout;
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

public class MapEditor extends JFrame implements ActionListener {
    // メインパネル
    private MainPanel mainPanel;
    private JScrollPane scrollPane;
    
    // マップチップパレット
    private MapchipPalette mapchipPalette;
    // キャラクターパレット
    private CharaPalette charaPalette;
    
    // 情報パネル
    private InfoPanel infoPanel;
    
    // メニューアイテム
    private JMenuItem newItem;  // 新規作成
    private JMenuItem openItem;  // 開く
    private JMenuItem saveItem;  // 保存
    private JMenuItem exitItem;  // 終了
    private JMenuItem fillItem;  // 塗りつぶし
    private JMenuItem addEventItem;  // イベント追加
    private JMenuItem removeEventItem;  // イベント削除
    private JMenuItem versionItem;  // バージョン情報
    
    // マップの大きさ
    // MapSizeDialogで値をセットする
    private int row, col;
    
    // ファイル選択ダイアログ
    private JFileChooser fileChooser = new JFileChooser(".");
    
    public MapEditor() {
        setTitle("Map Editor");

        initGUI();

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * GUIを初期化
     * 
     */
    private void initGUI() {
        // マップチップパレット
        mapchipPalette = new MapchipPalette(this);
        mapchipPalette.setVisible(true);

        // キャラクターパレット
        charaPalette = new CharaPalette(this);
        charaPalette.setVisible(true);
        
        // イベントダイアログ
        EventDialog eventDialog = new EventDialog(this, mapchipPalette, charaPalette);
        eventDialog.setVisible(false);
        
        // 情報パネル
        infoPanel = new InfoPanel();
        
        // メインパネル
        mainPanel = new MainPanel(mapchipPalette, charaPalette, eventDialog, infoPanel);
        scrollPane = new JScrollPane(mainPanel);

        // イベントダイアログにメインパネルへの参照をセット
        eventDialog.setMainPanel(mainPanel);
        
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(infoPanel, BorderLayout.NORTH);
        // メニューバーを作成
        createMenu();
    }

    /**
     * メニューバーを作成
     *
     */
    private void createMenu() {
        JMenu fileMenu = new JMenu("ファイル");
        JMenu editMenu = new JMenu("編集");
        JMenu eventMenu = new JMenu("イベント");
        JMenu helpMenu = new JMenu("ヘルプ");
        
        newItem = new JMenuItem("新規作成");
        openItem = new JMenuItem("開く");
        saveItem = new JMenuItem("保存");
        exitItem = new JMenuItem("終了");
        fillItem = new JMenuItem("塗りつぶし");
        addEventItem = new JMenuItem("追加");
        removeEventItem = new JMenuItem("削除");
        versionItem = new JMenuItem("バージョン情報");
        
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();  // 区切り
        fileMenu.add(exitItem);
        editMenu.add(fillItem);
        eventMenu.add(addEventItem);
        eventMenu.add(removeEventItem);
        helpMenu.add(versionItem);
        
        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);
        fillItem.addActionListener(this);
        addEventItem.addActionListener(this);
        removeEventItem.addActionListener(this);
        versionItem.addActionListener(this);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(eventMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == newItem) {
            newMap();
        } else if (source == openItem) {
            openMap();
        } else if (source == saveItem) {
            saveMap();
        } else if (source == exitItem) {
            exit();
        } else if (source == fillItem) {
            mainPanel.fillMap();
        } else if (source == addEventItem) {
            mainPanel.addEvent();
        } else if (source == removeEventItem) {
            mainPanel.removeEvent();
        } else if (source == versionItem) {
            JOptionPane.showMessageDialog(MapEditor.this,
                    "マップエディター",
                    "バージョン情報",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * メニュー/新規作成 新しいマップを作成する
     * 
     */
    private void newMap() {
        // マップサイズダイアログを開く
        MapSizeDialog dialog = new MapSizeDialog(this);
        dialog.setVisible(true);
        
        // ｷｬﾝｾﾙボタンが押されたら何もしない
        if (!dialog.isOKPressed()) {
            return;
        }
        
        // メインパネルに新しいマップを作成
        mainPanel.initMap(row, col);
        
        // パネルの大きさをマップの大きさと同じにする
        mainPanel.setPreferredSize(new Dimension(col * MainPanel.CS, row * MainPanel.CS));
        // スクロールバーを表示
        scrollPane.getViewport().revalidate();
        scrollPane.getViewport().repaint();
    }

    /**
     * メニュー/開く マップを開く
     *
     */
    private void openMap() {
        fileChooser.addChoosableFileFilter(new MapFileFilter());
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setDialogTitle("マップを開く");
        
        int ret = fileChooser.showOpenDialog(null);
        
        File mapFile;
        if (ret == JFileChooser.APPROVE_OPTION) {
            // もし開くボタンが押されたらマップファイルをロードする
            mapFile = fileChooser.getSelectedFile();
            mainPanel.loadMap(mapFile.getPath());
            // スクロールバーを表示
            scrollPane.getViewport().revalidate();
            scrollPane.getViewport().repaint();
        }
    }
    
    /**
     * メニュー/保存 マップを保存する
     *
     * @return ダイアログで押されたボタン
     */
    private int saveMap() {
        fileChooser.addChoosableFileFilter(new MapFileFilter());
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setDialogTitle("マップを保存する");
        
        int ret = fileChooser.showSaveDialog(null);

        File mapFile;
        if (ret == JFileChooser.APPROVE_OPTION) {
            // もし保存ボタンが押されたらマップファイルをセーブする
            mapFile = fileChooser.getSelectedFile();
            mainPanel.saveMap(mapFile.getPath());
        }
        
        return ret;
    }
    
    /**
     * TODO:アプリケーションを終了する
     *
     */
    private void exit() {
        
    }
    
    /**
     * マップサイズダイアログ
     */
    private class MapSizeDialog extends JDialog implements ActionListener {
        private JTextField rowTextField;
        private JTextField colTextField;
        private JButton okButton;
        private JButton cancelButton;
        
        // OKボタンが押されたtrueになる
        private boolean isOKPressed;
        
        public MapSizeDialog(JFrame owner) {
            super(owner, "新規マップ作成", true);
            
            isOKPressed = false;
            
            initGUI();
        }
        
        /**
         * GUIを初期化
         *
         */
        private void initGUI() {
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
                    // MapEditorのインスタンス変数にセット
                    row = Integer.parseInt(rowTextField.getText());
                    col = Integer.parseInt(colTextField.getText());
                } catch (NumberFormatException ex) {
                    // テキストボックスに数値以外が入力されたとき
                    JOptionPane.showMessageDialog(MapSizeDialog.this,
                            "数値を入力してください。");
                    rowTextField.setText("");
                    colTextField.setText("");
                    return;
                }
                
                // マップは最低15x20
                if (row < 15 || col < 20) {
                    JOptionPane.showMessageDialog(MapSizeDialog.this,
                            "マップの大きさは15x20以上にしてください。");
                    row = 15;
                    col = 20;
                    return;
                }
                
                // マップは最大256x256
                if (row > 256 || col > 256) {
                    JOptionPane.showMessageDialog(MapSizeDialog.this,
                            "マップの大きさは256x256以内にしてください。");
                    row = col = 256;
                    return;
                }
                
                isOKPressed = true;
                setVisible(false);
            } else if (e.getSource() == cancelButton) {
                isOKPressed = false;
                setVisible(false);
            }
        }
        
        /**
         * OKボタンが押されたかチェック
         * 
         * @return ダイアログのOKボタンが押されたらtrue
         */
        public boolean isOKPressed() {
            return isOKPressed;
        }
    }
    
    private class MapFileFilter extends FileFilter {
        public boolean accept(File file) {
            String extension = "";  // 拡張子
            if (file.getPath().lastIndexOf('.') > 0) {
                extension = file.getPath().substring(
                        file.getPath().lastIndexOf('.') + 1).toLowerCase();
            }
            
            // mapファイルかディレクトリだったらtrueを返す
            if (extension != "") {
                return extension.equals("map");
            } else {
                return file.isDirectory();
            }
        }
        
        public String getDescription() {
            return "Map Files (*.map)";
        }
    }
    
    public static void main(String[] args) {
        new MapEditor();
    }
}
