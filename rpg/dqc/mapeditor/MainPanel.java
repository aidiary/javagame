/*
 * マップエディタのメインパネル
 */
package mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dqc.Chara;
import dqc.Door;
import dqc.Event;
import dqc.Move;
import dqc.Treasure;

public class MainPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    
    public static final int CS = 16;
    
    // マップチップパレットへの参照
    private MapchipPalette mapchipPalette;
    
    // マップチップイメージ
    private Image[] mapchipImages;
    
    // キャラクターイメージ
    private Image[][] charaImages;
    
    // マップ
    private int[][] map;
    // マップの大きさ
    private int row;
    private int col;
    
    // マウスの位置（ピクセル単位）
    private int mouseX, mouseY;
    // マウスの位置（マス単位）
    private int x, y;
    
    // イベントダイアログへの参照
    private EventDialog eventDialog;
    
    // 情報パネルへの参照
    private InfoPanel infoPanel;
    
    public MainPanel(MapchipPalette mapchipPalette, CharaPalette charaPalette, EventDialog eventDialog, InfoPanel infoPanel) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        
        this.mapchipPalette = mapchipPalette;
        mapchipImages = mapchipPalette.getMapchipImages();
        
        charaImages = charaPalette.getCharaImages();
        
        this.eventDialog = eventDialog;
        this.infoPanel = infoPanel;
        
        // マップを初期化
        initMap(30, 40);
    }
    
    /**
     * マップを初期化
     * 
     * @param row 行数
     * @param col 列数
     */
    public void initMap(int row, int col) {
        this.row = row;
        this.col = col;
        map = new int[row][col];
        
        // マップチップパレットで選択されているマップチップ番号を取得
        int selectedMapchipNo = mapchipPalette.getSelectedMapchipNo();
        for (int i=0; i<row; i++) {
            for (int j=0; j<col; j++) {
                map[i][j] = selectedMapchipNo;
            }
        }
    }
    
    /**
     * マップをファイルから読み込む
     * 
     * @param mapFile マップファイル名
     */
    public void loadMap(String mapFile) {
        try {
            FileInputStream in = new FileInputStream(mapFile);
            
            // 行数・列数を読み込む
            row = in.read();
            col = in.read();
            
            // マップを読み込む
            map = new int[row][col];
            for (int i=0; i<row; i++) {
                for (int j=0; j<col; j++) {
                    map[i][j] = in.read();  // 下位8ビットを読み込み
                    map[i][j] = (in.read() << 8) | map[i][j];  // 上位8ビットを読み込んで結合
                }
            }
            
            in.close();
            
            // パネルの大きさをマップの大きさと同じにする
            setPreferredSize(new Dimension(col * CS, row * CS));
            
            // イベントの読み込み
            String eventFile = mapFile.substring(0, mapFile.lastIndexOf(".")) + ".evt";
            loadEvent(eventFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * イベントの読み込み
     * 
     * @param eventFile イベントファイル名
     */
    private void loadEvent(String eventFile) {
        // 現在のイベントを削除
        eventDialog.setEventList(null);
        
        ArrayList eventList = new ArrayList();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(eventFile));
            String line;
            while ((line = br.readLine()) != null) {
                // 空行を読み飛ばす
                if (line.equals("")) continue;
                // コメント行を読み飛ばす
                if (line.startsWith("#")) continue;
                StringTokenizer st = new StringTokenizer(line, ",");
                // イベント情報を取得する
                // イベントタイプを取得してイベントごとに処理する
                String eventType = st.nextToken();
                if (eventType.equals("BGM")) {  // BGMイベント
                    // TODO: マップのBGMをセットするダイアログを作る
                } else if (eventType.equals("CHARA")) {  // キャラクターイベント
                    eventList.add(createChara(st));
                } else if (eventType.equals("TREASURE")) {  // 宝箱イベント
                    eventList.add(createTreasure(st));
                } else if (eventType.equals("DOOR")) {  // ドアイベント
                    eventList.add(createDoor(st));
                } else if (eventType.equals("MOVE")) {  // 移動イベント
                    eventList.add(createMove(st));
                }
            }
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        eventDialog.setEventList(eventList);
    }

    /**
     * マップをファイルへ書き込む
     * 
     * @param mapFile マップファイル
     */
    public void saveMap(String mapFile) {
        try {
            FileOutputStream out = new FileOutputStream(mapFile);

            // 行数・列数を書き込む
            out.write(row);
            out.write(col);
            
            // マップデータを書き込む
            for (int i=0; i<row; i++) {
                for (int j=0; j<col; j++) {
                    out.write(map[i][j]);  // 下位8ビットを書き込み
                    out.write(map[i][j] >> 8);  // 上位8ビットを書き込み
                }
            }
            
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // イベントの保存
        String eventFile = mapFile.substring(0, mapFile.lastIndexOf(".")) + ".evt";
        saveEvent(eventFile);
    }
    
    /**
     * イベントを保存
     * 
     * @param eventFile イベントファイル名
     */
    private void saveEvent(String eventFile) {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(
                    new FileWriter(eventFile)));
            
            // TODO: BGM名は可変に
            ArrayList eventList = eventDialog.getEventList();
            for (int i=0; i<eventList.size(); i++) {
                Event evt = (Event) eventList.get(i);
                pw.println(evt.toString());
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 選択しているマップチップで塗りつぶす
     *
     */
    public void fillMap() {
        int selectedMapchipNo = mapchipPalette.getSelectedMapchipNo();
        
        for (int i=0; i<row; i++) {
            for (int j=0; j<col; j++) {
                map[i][j] = selectedMapchipNo;
            }
        }
        
        repaint();
    }
    
    /**
     * イベントを追加する
     *
     */
    public void addEvent() {
        // すでにイベントがあったら置けない
        ArrayList eventList = eventDialog.getEventList();
        for (int i=0; i<eventList.size(); i++) {
            Event evt = (Event) eventList.get(i);
            if (x == evt.getX() && y == evt.getY()) {
                JOptionPane.showMessageDialog(MainPanel.this, "すでにイベントがあります。");
                return;
            }
        }
        eventDialog.setPos(x, y);
        eventDialog.setVisible(true);
    }

    public void removeEvent() {
        // カーソル位置のイベントを検索する
        ArrayList eventList = eventDialog.getEventList();
        for (int i=0; i<eventList.size(); i++) {
            Event evt = (Event) eventList.get(i);
            if (x == evt.getX() && y == evt.getY()) {
                eventList.remove(evt);  // イベントを削除
                repaint();
                return;
            }
        }
        
        JOptionPane.showMessageDialog(MainPanel.this, "削除できるイベントはありません。");
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // マップチップを描画
        for (int i=0; i<row; i++) {
            for (int j=0; j<col; j++) {
                g.drawImage(mapchipImages[map[i][j]], j * CS, i * CS, this);
            }
        }

        // グリッドを描画
        g.setColor(Color.BLACK);
        for (int i=0; i<row; i+=4) {
            g.drawLine(0, i * CS, col * CS, i * CS);
        }
        for (int j=0; j<col; j+=4) {
            g.drawLine(j*CS, 0, j*CS, row * CS);
        }
        
        // イベントを描画
        g.setColor(Color.RED);
        ArrayList eventList = eventDialog.getEventList();
        if (eventList != null) {
            for (int i=0; i<eventList.size(); i++) {
                Event evt = (Event) eventList.get(i);

                if (evt instanceof Chara) {
                    Chara chara = (Chara)evt;
                    g.drawImage(charaImages[chara.getImageNo()][chara.getDirection()], chara.getX() * CS, chara.getY() * CS, this);
                } else {
                    g.drawImage(mapchipImages[evt.getImageNo()], evt.getX() * CS, evt.getY() * CS, this);
                }
                g.drawRect(evt.getX() * CS, evt.getY() * CS, CS, CS);
            }
        }
        
        // マップの選択位置にカーソル表示
        g.setColor(Color.YELLOW);
        g.drawRect(x * CS, y * CS, CS, CS);
    }

    public void mouseClicked(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        
        x = mouseX / CS;
        y = mouseY / CS;
        
        if (SwingUtilities.isLeftMouseButton(e)) {  // 左クリックの場合
            // マップチップパレットから取得した番号をセット
            if (x >= 0 && x < col && y >= 0 && y < row) {
                map[y][x] = mapchipPalette.getSelectedMapchipNo();
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {  // 右クリックの場合
            // 現在位置のマップチップ番号をセット
            mapchipPalette.setSelectedMapchipNo(map[y][x]);
            mapchipPalette.repaint();
        }
        
        // 情報パネルを更新
        infoPanel.setPos(x, y, mouseX, mouseY);
        
        repaint();
        
        // TODO: ダブルクリックでイベントダイアログ開く
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        
        x = mouseX / CS;
        y = mouseY / CS;
        
        // マップチップパレットから取得した番号をセット
        if (x >= 0 && x < col && y >= 0 && y < row) {
            map[y][x] = mapchipPalette.getSelectedMapchipNo();
        }
        
        // 情報パネルを更新
        infoPanel.setPos(x, y, mouseX, mouseY);
        
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
    
    /**
     * CHARAイベントを読み込んでCharaオブジェクトを生成
     * 
     * @param st
     */
    private Chara createChara(StringTokenizer st) {
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        int imageNo = Integer.parseInt(st.nextToken());
        int direction = Integer.parseInt(st.nextToken());
        int moveType = Integer.parseInt(st.nextToken());
        String message = st.nextToken();
        Chara chara = new Chara(x, y, imageNo, direction, moveType, message, null);

        return chara;
    }
    
    /**
     * TREASUREイベントを読み込んでTreasureオブジェクトを生成
     * 
     * @param st
     */
    private Treasure createTreasure(StringTokenizer st) {
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        int imageNo = Integer.parseInt(st.nextToken());
        String itemName = st.nextToken();
        Treasure treasure = new Treasure(x, y, imageNo, itemName);
        
        return treasure;
    }

    /**
     * DOORイベントを読み込んでDoorオブジェクトを生成
     * 
     * @param st
     */
    private Door createDoor(StringTokenizer st) {
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        int imageNo = Integer.parseInt(st.nextToken());
        Door door = new Door(x, y, imageNo);
        
        return door;
    }
    
    /**
     * MOVEイベントを読み込んでMoveオブジェクトを生成
     * 
     * @param st
     */
    private Move createMove(StringTokenizer st) {
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        int imageNo = Integer.parseInt(st.nextToken());
        String destMapName = st.nextToken();
        int destX = Integer.parseInt(st.nextToken());
        int destY = Integer.parseInt(st.nextToken());
        Move move = new Move(x, y, imageNo, destMapName, destX, destY);
        
        return move;
    }
}
