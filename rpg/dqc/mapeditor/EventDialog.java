package mapeditor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dqc.Chara;
import dqc.Door;
import dqc.Move;
import dqc.Treasure;

public class EventDialog extends JDialog {
    // イベント座標
    private int x, y;
    
    // イメージ番号
    private int mapchipImageNo = 12;
    private int charaImageNo = 0;
    
    // 分割されたイメージ
    private Image[] mapchipImages;
    private Image[][] charaImages;
    
    // イベントリスト
    private ArrayList eventList = new ArrayList();

    // GUI部品
    private JLabel mapchipLabel;
    private JTextField destMapNameText;
    private JTextField destXText, destYText;
    private JButton okButton, cancelButton;
    private JLabel charaLabel;
    private JComboBox directionBox, moveTypeBox;
    private JTextArea messageArea;
    private JLabel treasureLabel;
    private JTextField itemText;
    private JLabel doorLabel;
    
    // マップチップパレットへの参照
    private MapchipPalette mapchipPalette;
    // キャラクターパレットへの参照
    private CharaPalette charaPalette;
    // メインパネルへの参照
    private MainPanel mainPanel;
    
    public EventDialog(JFrame owner, MapchipPalette mapchipPalette, CharaPalette charaPalette) {
        super(owner, "イベントダイアログ", false);
        setResizable(false);
        
        this.mapchipPalette = mapchipPalette;
        this.charaPalette = charaPalette;
        
        // 分割したマップチップイメージをマップチップパレットから取得
        mapchipImages = mapchipPalette.getMapchipImages();
        charaImages = charaPalette.getCharaImages();
        
        // GUIを初期化
        initGUI();
        pack();
    }
    
    /**
     * イベントリストを返す
     * 
     * @return イベントリスト
     */
    public ArrayList getEventList() {
        return eventList;
    }
    
    /**
     * イベントリストをセットする
     * 
     * @param eventList イベントリスト
     */
    public void setEventList(ArrayList eventList) {
        this.eventList = eventList;
    }
    
    private void initGUI() {
        // タブペイン
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // 移動イベントタブ
        JPanel moveEventPanel = createMoveEventTab();
        // キャラクターイベントタブ
        JPanel charaEventPanel = createCharaEventTab();
        // 宝箱イベントタブ
        JPanel treasureEventPanel = createTreasureEventTab();
        // 扉イベントタブ
        JPanel doorEventPanel = createDoorEventTab();
        
        tabbedPane.addTab("移動", moveEventPanel);
        tabbedPane.addTab("キャラ", charaEventPanel);
        tabbedPane.addTab("宝箱", treasureEventPanel);
        tabbedPane.addTab("扉", doorEventPanel);
        
        getContentPane().add(tabbedPane);
    }

    /**
     * 移動イベントタブを作成
     * 
     * @return 移動イベントタブのパネル
     */
    private JPanel createMoveEventTab() {
        JPanel moveEventPanel = new JPanel();
        moveEventPanel.setLayout(new BorderLayout());
        
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(7, 2));
        
        mapchipLabel = new JLabel();
        mapchipLabel.setIcon(new ImageIcon(mapchipImages[mapchipImageNo]));
        mapchipLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // 選択されているマップチップをラベルにセット
                mapchipImageNo = mapchipPalette.getSelectedMapchipNo();
                mapchipLabel.setIcon(new ImageIcon(mapchipImages[mapchipImageNo]));
            }
        });
        p1.add(new JLabel("マップチップ"));
        p1.add(mapchipLabel);
        
        // 移動先マップ名
        destMapNameText = new JTextField(8);
        p1.add(new JLabel("移動先マップ名"));
        p1.add(destMapNameText);
        
        // 移動先座標
        destXText = new JTextField(8);
        p1.add(new JLabel("移動先X"));
        p1.add(destXText);
        
        destYText = new JTextField(8);
        p1.add(new JLabel("移動先Y"));
        p1.add(destYText);
        
        // OK、キャンセルボタン
        JPanel p2 = new JPanel();
        okButton = new JButton("OK");
        cancelButton = new JButton("ｷｬﾝｾﾙ");
        p2.add(okButton);
        p2.add(cancelButton);
        
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Moveの生成
                Move evt;
                try {
                    evt = new Move(x, y, mapchipImageNo,
                            destMapNameText.getText(),
                            Integer.parseInt(destXText.getText()),
                            Integer.parseInt(destYText.getText()));
                } catch (NumberFormatException ex) {
                    // 数値以外が入力され、Integerに変換できなかったとき
                    JOptionPane.showMessageDialog(EventDialog.this, "X座標とY座標は数値を入力してください。");
                    destXText.setText("");
                    destYText.setText("");
                    return;
                }
                // イベントリストに追加
                eventList.add(evt);
                setVisible(false);
                mainPanel.repaint();
                System.out.println("移動イベント追加: " + evt);
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        moveEventPanel.add(p1, BorderLayout.CENTER);
        moveEventPanel.add(p2, BorderLayout.SOUTH);
        
        return moveEventPanel;
    }
    
    /**
     * キャラクターイベントタブを作成
     * 
     * @return キャラクターイベントタブのパネル
     */
    private JPanel createCharaEventTab() {
        JPanel charaEventPanel = new JPanel();
        charaEventPanel.setLayout(new BorderLayout());
        
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(3, 2));
        
        charaLabel = new JLabel();
        charaLabel.setIcon(new ImageIcon(charaImages[charaImageNo][0]));
        charaLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // 選択されているキャラクターをラベルにセット
                charaImageNo = charaPalette.getSelectedCharaNo();
                int index = directionBox.getSelectedIndex();
                charaLabel.setIcon(new ImageIcon(charaImages[charaImageNo][index]));
            }
        });
        p1.add(new JLabel("キャラクター"));
        p1.add(charaLabel);
        
        // 向き
        directionBox = new JComboBox();
        directionBox.setEditable(false);
        directionBox.addItem("下向き");
        directionBox.addItem("上向き");
        directionBox.addItem("左向き");
        directionBox.addItem("右向き");
        directionBox.setSelectedIndex(0);  // デフォルトは下向き
        directionBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = directionBox.getSelectedIndex();
                // ラベルの絵の向きを変える
                charaLabel.setIcon(new ImageIcon(charaImages[charaImageNo][index]));
            }
        });
        p1.add(new JLabel("向き"));
        p1.add(directionBox);
        
        // 移動タイプ
        moveTypeBox = new JComboBox();
        moveTypeBox.setEditable(false);
        moveTypeBox.addItem("移動しない");
        moveTypeBox.addItem("ランダム移動");
        p1.add(new JLabel("移動タイプ"));
        p1.add(moveTypeBox);
        
        // メッセージ
        JPanel p2 = new JPanel();
        messageArea = new JTextArea();
        messageArea.setRows(5);
        messageArea.setColumns(20);
        messageArea.setLineWrap(true);
        messageArea.setText("メッセージを入力してください。");
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(messageArea);
        p2.add(scrollPane);
        
        // OK、キャンセルボタン
        JPanel p3 = new JPanel();
        okButton = new JButton("OK");
        cancelButton = new JButton("ｷｬﾝｾﾙ");
        p3.add(okButton);
        p3.add(cancelButton);
        
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Charaの生成
                Chara chara = new Chara(x, y, charaImageNo,
                            directionBox.getSelectedIndex(),
                            moveTypeBox.getSelectedIndex(),
                            messageArea.getText(),
                            null);
                // イベントリストに追加
                eventList.add(chara);
                setVisible(false);
                mainPanel.repaint();
                System.out.println("キャラクターイベント追加: " + chara);
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        charaEventPanel.add(p1, BorderLayout.NORTH);
        charaEventPanel.add(p2, BorderLayout.CENTER);
        charaEventPanel.add(p3, BorderLayout.SOUTH);
        
        return charaEventPanel;
    }
    
    /**
     * 宝箱イベントタブを作成
     * TODO: 宝箱のイメージを変えられるようにする
     * TODO: TreasureEventはつぼなどアイテム入手一般に使う
     * 
     * @return 宝箱イベントタブのパネル
     */
    private JPanel createTreasureEventTab() {
        JPanel treasureEventPanel = new JPanel();
        treasureEventPanel.setLayout(new BorderLayout());
        
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(2, 2));
        
        mapchipImageNo = 115;  // 115は宝箱の絵
        treasureLabel = new JLabel();
        treasureLabel.setIcon(new ImageIcon(mapchipImages[mapchipImageNo]));
        treasureLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // 選択されているマップチップをラベルにセット
                mapchipImageNo = mapchipPalette.getSelectedMapchipNo();
                treasureLabel.setIcon(new ImageIcon(mapchipImages[mapchipImageNo]));
            }
        });
        
        p1.add(new JLabel("宝箱"));
        p1.add(treasureLabel);
        
        itemText = new JTextField(8);
        p1.add(new JLabel("アイテム名"));
        p1.add(itemText);
        
        // OK、キャンセルボタン
        JPanel p2 = new JPanel();
        okButton = new JButton("OK");
        cancelButton = new JButton("ｷｬﾝｾﾙ");
        p2.add(okButton);
        p2.add(cancelButton);
        
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TreasureEventの作成
                Treasure treasure = new Treasure(x, y, mapchipImageNo, itemText.getText());
                
                // イベントリストに追加
                eventList.add(treasure);
                setVisible(false);
                mainPanel.repaint();
                System.out.println("宝箱イベント追加: " + treasure);
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        treasureEventPanel.add(p1, BorderLayout.NORTH);
        treasureEventPanel.add(p2, BorderLayout.SOUTH);
        
        return treasureEventPanel;
    }
    
    /**
     * 扉イベントタブを作成
     * TODO: 牢屋とかに対応できるようにイメージを可変にする
     * 
     * @return 扉イベントタブのパネル
     */
    private JPanel createDoorEventTab() {
        JPanel treasureEventPanel = new JPanel();
        treasureEventPanel.setLayout(new BorderLayout());
        
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(1, 2));
        
        mapchipImageNo = 85;  // 85は宝箱の絵
        
        doorLabel = new JLabel();
        doorLabel.setIcon(new ImageIcon(mapchipImages[mapchipImageNo]));
        doorLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // 選択されているマップチップをラベルにセット
                mapchipImageNo = mapchipPalette.getSelectedMapchipNo();
                doorLabel.setIcon(new ImageIcon(mapchipImages[mapchipImageNo]));
            }
        });
        
        p1.add(new JLabel("扉"));
        p1.add(doorLabel);
        
        // OK、キャンセルボタン
        JPanel p2 = new JPanel();
        okButton = new JButton("OK");
        cancelButton = new JButton("ｷｬﾝｾﾙ");
        p2.add(okButton);
        p2.add(cancelButton);
        
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // DoorEventの作成
                Door door = new Door(x, y, mapchipImageNo);
                
                // イベントリストに追加
                eventList.add(door);
                setVisible(false);
                mainPanel.repaint();
                System.out.println("扉イベント追加: " + door);
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        treasureEventPanel.add(p1, BorderLayout.NORTH);
        treasureEventPanel.add(p2, BorderLayout.SOUTH);
        
        return treasureEventPanel;
    }
    
    /**
     * イベント座標をセット
     * 
     * @param x X座標
     * @param y Y座標
     */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * メインパネルへの参照をセット
     * 
     * @param mainPanel メインパネル
     */
    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }
}
