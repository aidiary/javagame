/*
 * Created on 2004/12/24
 *
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
/**
 * 情報パネル。ライフの選択と説明を追加する。
 * 
 * @author mori
 *  
 */
public class InfoPanel extends JPanel implements ActionListener {
    // メインパネルへの参照
    private MainPanel panel;
    // ライフの名前を表示するコンボボックス
    private JComboBox lifeBox;
    // 説明を表示するエリア
    private JTextArea infoArea;

    /**
     * コンストラクタ。
     * 
     * @param panel メインパネルへの参照。
     */
    public InfoPanel(MainPanel panel) {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(480, 100));

        this.panel = panel;

        // lifeBoxとinfoLabelを貼り付けるパネル
        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout());

        // 名前ボックスを作成
        lifeBox = new JComboBox();
        // 書き換えられるようにする
        lifeBox.setEditable(true);
        // ライフをロードする
        loadLife();
        // イベントリスナーを追加
        lifeBox.addActionListener(this);
        // 名前ボックスを追加
        tempPanel.add(lifeBox, BorderLayout.NORTH);

        // 説明エリアを作成
        infoArea = new JTextArea();
        infoArea.setPreferredSize(new Dimension(480, 100));
        // 折り返されるようにする
        infoArea.setLineWrap(true);
        // 情報ラベルを追加
        tempPanel.add(infoArea, BorderLayout.CENTER);
        // ようこそライフゲームへ
        infoArea.setText("ようこそライフゲームへ。");

        add(tempPanel);
    }

    /**
     * 名前ボックスに入力されている文字列を返す。
     * 
     * @return 名前ボックスに入力されている文字列。
     */
    public String getLifeName() {
        return (String) lifeBox.getEditor().getItem();
    }

    /**
     * 説明エリアに入力されている文字列を返す。
     * 
     * @return 説明エリアに入力されている文字列。
     */
    public String getLifeInfo() {
        return infoArea.getText();
    }

    /**
     * 説明エリアに文字列をセットする。
     */
    public void setLifeInfo(String str) {
        infoArea.setText(str);
    }

    /**
     * ライフファイルをロードして名前ボックスにセットする。
     *  
     */
    public void loadLife() {
        // 一番上は空白にする
        lifeBox.addItem("");
        // ライフが入っているディレクトリ
        File lifeDir = new File("life");
        // その中に入っているファイルの名前を取得
        String[] lifeNameList = lifeDir.list();
        if (lifeNameList == null) return;
        for (int i = 0; i < lifeNameList.length; i++) {
            lifeBox.addItem(lifeNameList[i]);
        }
    }

    /**
     * 名前ボックスに追加する。
     * 
     * @param lifeName 追加するライフの名前。
     */
    public void addLife(String lifeName) {
        lifeBox.addItem(lifeName);
    }

    /**
     * 名前ボックスでライフを選択したとき呼ばれる。
     */
    public void actionPerformed(ActionEvent e) {
        // 選択されているライフの名前
        String filename = (String) lifeBox.getSelectedItem();
        // 空白のとき
        if (filename.equals("")) {
            setLifeInfo("");
            panel.clear();
        } else {
            // 空白以外のときはそのファイルを読み込む
            panel.loadLife(filename);
        }
    }
}