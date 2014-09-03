import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/*
 * Created on 2005/12/29
 *
 */

/**
 * @author mori
 *
 */
public class Map {
    // マップ
    private int[][] map;
    
    // マップの行数・列数（単位：マス）
    private int row;
    private int col;
    
    // マップ全体の大きさ（単位：ピクセル）
    private int width;
    private int height;

    private static BufferedImage chipImage;
    private static Image warpImage;  // 旅の扉のアニメーション（アニメーションGIFはImageでないとだめみたい）

    // このマップにいるキャラクターたち
    private Vector charas = new Vector();
    // このマップにあるイベント
    private Vector events = new Vector();

    // BGM番号
    private int bgmNo;

    public Map(String mapName, int bgmNo) {
        this.bgmNo = bgmNo;

        // マップをロード
        load("map/" + mapName + ".map");

        // イベントをロード
        loadEvent("event/" + mapName + ".evt");

        // 初回の呼び出しのみイメージをロード
        if (chipImage == null) {
            loadImage();
        }
    }

    public void draw(Graphics g, int offsetX, int offsetY) {
        // オフセットを元に描画範囲を求める
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(DQ.WIDTH) + 1;
        // 描画範囲がマップの大きさより大きくならないように調整
        lastTileX = Math.min(lastTileX, col);
        
        int firstTileY = pixelsToTiles(-offsetY);
        int lastTileY = firstTileY + pixelsToTiles(DQ.HEIGHT) + 1;
        // 描画範囲がマップの大きさより大きくならないように調整
        lastTileY = Math.min(lastTileY, row);

        for (int i = firstTileY; i < lastTileY; i++) {
            for (int j = firstTileX; j < lastTileX; j++) {
                int mapChipNo = map[i][j];
                // イメージ上の位置を求める
                // マップチップイメージは8x8を想定
                int cx = (mapChipNo % 8) * Chipset.SIZE;
                int cy = (mapChipNo / 8) * Chipset.SIZE;
                g.drawImage(chipImage, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY,
                        tilesToPixels(j) + offsetX + Chipset.SIZE, tilesToPixels(i) + offsetY + Chipset.SIZE,
                        cx, cy, cx + Chipset.SIZE, cy + Chipset.SIZE, null);

                // (j, i) にあるイベントを描画
                for (int n=0; n<events.size(); n++) {
                    Event event = (Event)events.get(n);
                    // イベントが(j, i)にあれば描画
                    if (event.x == j && event.y == i) {
                        mapChipNo = event.chipNo;
                        cx = (mapChipNo % 8) * Chipset.SIZE;
                        cy = (mapChipNo / 8) * Chipset.SIZE;
                        if (event instanceof MoveEvent && mapChipNo == Chipset.WARP) {
                            // 移動できる旅のとびらの場合
                            // MoveEvent以外で旅のとびらチップを使った場合はアニメーションしない
                            g.drawImage(warpImage, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY, null);
                        } else {
                            g.drawImage(chipImage, tilesToPixels(j) + offsetX, tilesToPixels(i) + offsetY,
                                    tilesToPixels(j) + offsetX + Chipset.SIZE, tilesToPixels(i) + offsetY + Chipset.SIZE,
                                    cx, cy, cx + Chipset.SIZE, cy + Chipset.SIZE, null);
                        }
                    }
                }
            }
        }
        
        // このマップにいるキャラクターを描画
        for (int n=0; n<charas.size(); n++) {
            Chara chara = (Chara)charas.get(n);
            chara.draw(g, offsetX, offsetY);
        }
    }

    /**
     * (x,y)にぶつかるものがあるか調べる。
     * @param x マップのx座標
     * @param y マップのy座標
     * @return (x,y)にぶつかるものがあったらtrueを返す。
     */
    public boolean isHit(int x, int y) {
        // ぶつかる
        if (map[y][x] == Chipset.WALL || map[y][x] == Chipset.THRONE ||
                map[y][x] == Chipset.WATER || map[y][x] == Chipset.MOUNTAIN ||
                map[y][x] == Chipset.WEAPON || map[y][x] == Chipset.ITEM ||
                map[y][x] == Chipset.INN) {
            return true;
        }

        // 他のキャラクターがいるか
        for (int i = 0; i < charas.size(); i++) {
            Chara chara = (Chara) charas.get(i);
            if (chara.getX() == x && chara.getY() == y) {
                return true;
            }
        }

        // ぶつかるイベントがあるか
        for (int i = 0; i < events.size(); i++) {
            Event event = (Event)events.get(i);
            if (event.x == x && event.y == y) {
                return event.isHit;
            }
        }

        // なければぶつからない
        return false;
    }

    /**
     * キャラクターをこのマップに追加する
     * @param chara キャラクター
     */
    public void addChara(Chara chara) {
        charas.add(chara);
    }

    /**
     * キャラクターをこのマップから削除する
     * @param chara キャラクター
     */
    public void removeChara(Chara chara) {
        charas.remove(chara);
    }

    /**
     * (x,y)にキャラクターがいるか調べる
     * @param x X座標
     * @param y Y座標
     * @return (x,y)にいるキャラクター、いなかったらnull
     */
    public Chara charaCheck(int x, int y) {
        for (int i=0; i<charas.size(); i++) {
            Chara chara = (Chara)charas.get(i);
            if (chara.getX() == x && chara.getY() == y) {
                return chara;
            }
        }
        
        return null;
    }

    /**
     * (x,y)にイベントがあるか調べる
     * @param x X座標
     * @param y Y座標
     * @return (x,y)にいるイベント、いなかったらnull
     */
    public Event eventCheck(int x, int y) {
        for (int i=0; i<events.size(); i++) {
            Event event = (Event)events.get(i);
            if (event.x == x && event.y == y) {
                return event;
            }
        }
        
        // 画面のはしだったら強制的にイベントファイルの最初にあるMOVE_EVENT
        // イベントファイルの最初には外へ出るMOVE_EVENTがあるとする
        if (x == 0 || y == 0 || x == col - 1 || y == row - 1) {
            return (Event)events.get(0);
        }
        
        return null;
    }

    /**
     * 登録されているイベントを削除する
     * @param event 削除したいイベント
     */
    public void removeEvent(Event event) {
        events.remove(event);
    }

    /**
     * ピクセル単位をマス単位に変更する
     * @param pixels ピクセル単位
     * @return マス単位
     */
    public static int pixelsToTiles(double pixels) {
        return (int)Math.floor(pixels / Chipset.SIZE);
    }
    
    /**
     * マス単位をピクセル単位に変更する
     * @param tiles マス単位
     * @return ピクセル単位
     */
    public static int tilesToPixels(int tiles) {
        return tiles * Chipset.SIZE;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector getCharas() {
        return charas;
    }

    /**
     * マップチップ番号を返す
     * @param x X座標
     * @param y Y座標
     * @return マップチップ番号
     */
    public int getMapChip(int x, int y) {
        return map[y][x];
    }

    /**
     * BGM番号を返す
     * @return BGM番号
     */
    public int getBgmNo() {
        return bgmNo;
    }

    /**
     * ファイルからマップを読み込む
     * @param filename 読み込むマップデータのファイル名
     */
    private void load(String filename) {
        try {
            InputStream in = getClass().getResourceAsStream(filename);
            // 行数と列数を読み込む
            row = in.read();
            col = in.read();
            // マップサイズを設定
            width = col * Chipset.SIZE;
            height = row * Chipset.SIZE;
            // マップを作成
            map = new int[row][col];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    map[i][j] = in.read();
                }
            }
//            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * イベントをロードする
     * @param filename イベントファイル
     */
    private void loadEvent(String filename) {
        try {
            // イベントの文字コードはシフトJIS
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    getClass().getResourceAsStream(filename), "Shift_JIS"));
            String line;
            while ((line = br.readLine()) != null) {
                // 空行は読み飛ばす
                if (line.equals("")) continue;
                // コメント行は読み飛ばす
                if (line.startsWith("#")) continue;
                StringTokenizer st = new StringTokenizer(line, ",");
                // イベント情報を取得する
                // イベントタイプを取得してイベントごとに処理する
                String eventType = st.nextToken();
                if (eventType.equals("CHARA")) {  // キャラクターイベント
                    makeCharacter(st);
                } else if (eventType.equals("TREASURE")) {  // 宝箱イベント
                    makeTreasure(st);
                } else if (eventType.equals("DOOR")) {  // ドアイベント
                    makeDoor(st);
                } else if (eventType.equals("MOVE")) {  // 移動イベント
                    makeMove(st);
                } else if (eventType.equals("MESSAGE")) {  // メッセージイベント
                    makeMessage(st);
                } else if (eventType.equals("MSGBOARD")) {  // 立て札
                    makeMessageBoard(st);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadImage() {
        // マップチップのイメージをロード
        try {
            chipImage = ImageIO.read(getClass().getResource("image/mapchip.gif"));
 
            ImageIcon icon = new ImageIcon(getClass().getResource("image/warp.gif"));
            warpImage = icon.getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * キャラクターイベントを作成
     */
    private void makeCharacter(StringTokenizer st) {
        // イベントの座標
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        // キャラクタ番号
        int charaNo = Integer.parseInt(st.nextToken());
        // 向き
        int dir = Integer.parseInt(st.nextToken());
        // 移動タイプ
        int moveType = Integer.parseInt(st.nextToken());
        // メッセージ
        String message = st.nextToken();
        // キャラクターを作成
        Chara c = new Chara(x, y, charaNo, dir, moveType, this);
        // メッセージを登録
        c.setMessage(message);
        // キャラクターベクトルに登録
        charas.add(c);
    }

    /**
     * 宝箱イベントを作成
     */
    private void makeTreasure(StringTokenizer st) {
        // 宝箱の座標
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        // アイテム名
        String itemName = st.nextToken();
        // 宝箱イベントを作成
        TreasureEvent t = new TreasureEvent(x, y, itemName);
        // 宝箱イベントを登録
        events.add(t);
    }

    /**
     * とびらイベントを作成
     */
    private void makeDoor(StringTokenizer st) {
        // とびらの座標
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        // とびらイベントを作成
        DoorEvent d = new DoorEvent(x, y);
        // とびらイベントを登録
        events.add(d);
    }

    /**
     * 移動イベントを作成
     */
    private void makeMove(StringTokenizer st) {
        // 移動イベントの座標
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        // チップ番号
        int chipNo = Integer.parseInt(st.nextToken());
        // 移動先のマップ番号
        int destMapNo = Integer.parseInt(st.nextToken());
        // 移動先のX座標
        int destX = Integer.parseInt(st.nextToken());
        // 移動先のY座標
        int destY = Integer.parseInt(st.nextToken());
        // 移動イベントを作成
        MoveEvent m = new MoveEvent(x, y, chipNo, destMapNo, destX, destY);
        // 移動イベントを登録
        events.add(m);
    }

    /**
     * メッセージイベントを作成
     */
    private void makeMessage(StringTokenizer st) {
        // 移動イベントの座標
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        // チップ番号
        int chipNo = Integer.parseInt(st.nextToken());
        // メッセージ
        String message = st.nextToken();
        // メッセージイベントを作成
        MessageEvent m = new MessageEvent(x, y, chipNo, message);
        // メッセージイベントを登録
        events.add(m);
    }

    /**
     * 立て札を作成
     */
    private void makeMessageBoard(StringTokenizer st) {
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        // メッセージ
        String message = st.nextToken();
        // メッセージイベントを作成
        MessageBoardEvent m = new MessageBoardEvent(x, y, message);
        // メッセージイベントを登録
        events.add(m);
    }

    /**
     * マップをコンソールに表示。デバッグ用。
     */
    public void show() {
        for (int i=0; i<row; i++) {
            for (int j=0; j<col; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
}
