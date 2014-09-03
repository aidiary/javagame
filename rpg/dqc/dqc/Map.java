package dqc;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * マップクラス
 */

public class Map {
    // チップサイズ
    private static final int CS = 32;
    // マップチップ数
    private static final int NUM_CHIPS = 480;
    // 1行のマップチップ数
    private static final int NUM_CHIPS_IN_ROW = 30;
    
    // マップ
    private int[][] map;
    
    // 移動可能情報：マップチップ番号=>0 or 1
    // 0なら移動可能
    // TODO: マップ単位で持たせてもよい
    private int[] moveType;
    
    // マップの行数・列数（マス単位）
    private int row;
    private int col;
    
    // マップ全体の大きさ（ピクセル単位）
    private int width;
    private int height;

    // マップチップイメージ
    private Image mapchipImage;

    // イベント
    private ArrayList eventList = new ArrayList();
    
    // イベントローダー
    private EventLoader eventLoader;
    
    // マップ名
    private String mapName;
    
    // BGM名
    private String bgmName;
    
    public Map(String mapName) {
        this.mapName = mapName;
        
        // マップをロード
        load("map/" + mapName + ".map");
        
        // マップチップ移動可能情報を読み込む
        loadMoveType("map/move_type.dat");

        // イメージをロード
        if (mapchipImage == null) {
            loadImage();
        }
        
        // イベントローダーを生成
        if (eventLoader == null) {
            eventLoader = new EventLoader(this);
        }
        
        // イベントをロード
        // マップ名と同名のイベントファイルを読み込む
        loadEvent("map/" + mapName + ".evt");
    }

    /**
     * マップを描画
     * 
     * @param g 描画デバイス
     * @param offsetX X方向オフセット
     * @param offsetY Y方向オフセット
     */
    public void draw(Graphics g, int offsetX, int offsetY) {
        // オフセットから描画範囲を求める
        int firstTileX = pixelsToTiles(offsetX);
        int lastTileX = firstTileX + pixelsToTiles(DQC.WIDTH) + 1;  // 1マス余分に
        // 描画範囲がマップより大きくならないように調整
        lastTileX = Math.min(lastTileX, col);
        
        int firstTileY = pixelsToTiles(offsetY);
        int lastTileY = firstTileY + pixelsToTiles(DQC.HEIGHT) + 1;  // 1マス余分に
        // 描画範囲がマップより大きくならないように調整
        lastTileY = Math.min(lastTileY, row);
        
        for (int i = firstTileY; i < lastTileY; i++) {
            for (int j = firstTileX; j < lastTileX; j++) {
                int mapchipNo = map[i][j];
                int cx = (mapchipNo % NUM_CHIPS_IN_ROW) * CS;
                int cy = (mapchipNo / NUM_CHIPS_IN_ROW) * CS;
                g.drawImage(mapchipImage,
                        tilesToPixels(j) - offsetX,
                        tilesToPixels(i) - offsetY,
                        tilesToPixels(j) - offsetX + CS,
                        tilesToPixels(i) - offsetY + CS,
                        cx, cy,
                        cx + CS, cy + CS,
                        null);
            }
        }
        
        drawEvent(g, offsetX, offsetY);
    }

    /**
     * このマップのイベントを描画
     * 
     * @param g 描画デバイス
     * @param offsetX X方向オフセット
     * @param offsetY Y方向オフセット
     */
    private void drawEvent(Graphics g, int offsetX, int offsetY) {
        for (int i=0; i<eventList.size(); i++) {
            Event evt = (Event)eventList.get(i);
            evt.draw(g, offsetX, offsetY);
        }
    }
    
    /**
     * 移動可能かどうか調べる
     * 
     * @param x X座標
     * @param y Y座標
     * @return 移動可能ならtrue
     */
    public boolean isMovable(int x, int y) {
        // マップチップをチェック
        int mapchipNo = map[y][x];
        if (moveType[mapchipNo] == 1) {
            return false;
        }

        // 移動不可のイベントがないかチェック
        Event evt = (Event) getEvent(x, y);
        if (evt != null && !evt.isMovable()) {
            return false;
        }
        
        // イベントがないか移動可能イベントなら移動可能
        return true;
    }
    
    /**
     * (x,y)のイベントを返す
     * 
     * @param x X座標
     * @param y Y座標
     * @return (x,y)のイベント。なければnull。
     */
    public Event getEvent(int x, int y) {
        for (int i=0; i<eventList.size(); i++) {
            Event evt = (Event)eventList.get(i);
            if (evt.x == x && evt.y == y) {
                return evt;
            }
        }
        return null;
    }
    
    /**
     * マップチップ番号を返す
     * 
     * @param x X座標
     * @param y Y座標
     * @return マップチップ番号
     */
    public int getMapchipNo(int x, int y) {
        return map[y][x];
    }

    /**
     * イベントをこのマップに追加する
     * 
     * @param evt イベント
     */
    public void addEvent(Event evt) {
        eventList.add(evt);
    }
    
    /**
     * イベントを削除する
     * 
     * @param evt イベント
     */
    public void removeEvent(Event evt) {
        eventList.remove(evt);
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
     * マップの行数を返す
     * @return 行数
     */
    public int getRow() {
        return row;
    }
    
    /**
     * マップの列数を返す
     * 
     * @return 列数
     */
    public int getCol() {
        return col;
    }
    
    /**
     * マップの幅を返す
     * 
     * @return マップの幅
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * マップの高さを返す
     * 
     * @return マップの高さ
     */
    public int getHeight() {
        return height;
    }

    /**
     * マップ名を返す
     * 
     * @return マップ名
     */
    public String getMapName() {
        return mapName;
    }
    
    /**
     * BGM名を返す
     * 
     * @return BGM名
     */
    public String getBGM() {
        return bgmName;
    }
    
    /**
     * BGMをセット
     * 
     * @param bgmName BGM名
     */
    public void setBGM(String bgmName) {
        this.bgmName = bgmName;
    }
    
    /**
     * マップをロード
     * 
     * @param filename ファイル名
     */
    private void load(String filename) {
        System.out.println(filename);
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream(filename);
            
            // 行数・列数を読み込む
            row = in.read();
            col = in.read();
            width = col * CS;
            height = row * CS;

            // マップを読み込む
            map = new int[row][col];
            for (int i=0; i<row; i++) {
                for (int j=0; j<col; j++) {
                    map[i][j] = in.read();  // 下位8ビットを読み込み
                    map[i][j] = (in.read() << 8) | map[i][j];  // 上位8ビットを読み込んで結合
                }
            }
            
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * マップチップ移動可能情報を読み込む
     * 
     * @param filename ファイル名
     */
    private void loadMoveType(String filename) {
        moveType = new int[NUM_CHIPS];
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)));
            String line = br.readLine();
            for (int i=0; i<NUM_CHIPS; i++) {
                moveType[i] = Integer.parseInt(line.charAt(i) + "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * イメージをロード
     * 
     */
    private void loadImage() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        mapchipImage = toolkit.getImage(getClass().getClassLoader().getResource("image/mapchip.png"));
    }
    
    /**
     * イベントをロードする
     * 
     * @param eventFile イベントファイル
     */
    private void loadEvent(String eventFile) {
        eventList = eventLoader.load(eventFile);
//        for (int i=0; i<eventList.size(); i++) {
//            Event evt = (Event)eventList.get(i);
//            System.out.println(evt);
//        }
    }
    
    /**
     * ピクセル単位をマス単位に変更する
     * 
     * @param pixels ピクセル単位
     * @return マス単位
     */
    private int pixelsToTiles(int pixels) {
        return (int)Math.floor(pixels / CS);
    }
    
    /**
     * マス単位をピクセル単位に変更する
     * 
     * @param tiles マス単位
     * @return ピクセル単位
     */
    private int tilesToPixels(int tiles) {
        return tiles * CS;
    }
}
