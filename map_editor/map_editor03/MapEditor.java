import java.awt.Container;

import javax.swing.JFrame;

/*
 * Created on 2005/12/17
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

    public MapEditor() {
        setTitle("マップチップの選択と描画");
        setResizable(false);
        
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
        Container contentPane = getContentPane();
        contentPane.add(mainPanel);
    }
    
    public static void main(String[] args) {
        MapEditor frame = new MapEditor();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
