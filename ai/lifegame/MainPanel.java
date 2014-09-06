/*
 * 作成日: 2004/10/15
 *
 */
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.io.*;
import java.applet.*;
import java.util.*;
/**
 * ライフゲームメインパネル。
 * 
 * @author mori
 *  
 */
public class MainPanel extends JPanel
        implements
            Runnable,
            MouseListener,
            KeyListener {
    // パネルサイズ
    private static final int WIDTH = 480;
    private static final int HEIGHT = 480;
    // パネルのセル数
    private static final int ROW = 96;
    private static final int COL = 96;
    // セルのサイズ
    private static final int CS = 5;
    // セルの生死定数
    private static final int DEAD = 0;
    private static final int ALIVE = 1;
    // アニメーションの速度
    private static final int SLEEP = 100;
    // ランダムに発生する確率
    private static final double RAND_LIFE = 0.3;

    // フィールド。セルが活動する場所。
    private int[][] field;
    // 世代数
    private int generation;
    // スレッド
    private Thread thread;
    // 乱数生成器
    private Random rand;
    // ライフをセーブしたときの音
    private AudioClip saveAudio;
    // 情報パネルへの参照
    private InfoPanel infoPanel;

    // キーの現在位置
    private int r, c;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        rand = new Random();
        // フィールドを初期化
        field = new int[ROW][COL];
        clear();
        // キーの現在位置の初期化
        r = c = ROW / 2 - 1;
        // ライフを保存したときの音を読み込む
        saveAudio = Applet.newAudioClip(getClass().getResource("buble03.wav"));

        // イベントハンドラを登録
        addMouseListener(this);
        addKeyListener(this);
    }

    /**
     * 世代を進める。
     *  
     */
    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * 世代を止める。
     *  
     */
    public void stop() {
        if (thread != null) {
            thread = null;
        }
    }

    /**
     * フィールドを初期化する。
     *  
     */
    public void clear() {
        generation = 0;
        for (int i = 0; i < ROW; i++)
            for (int j = 0; j < COL; j++)
                field[i][j] = DEAD;
        repaint();
    }

    /**
     * 1世代だけ進める。
     *  
     */
    public void step() {
        int[][] nextField = new int[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                switch (around(i, j)) {
                    case 2 :
                        // 周囲の2セルが生きていればそのまま
                        nextField[i][j] = field[i][j];
                        break;
                    case 3 :
                        // 周囲の3セルが生きていれば繁殖
                        nextField[i][j] = ALIVE;
                        break;
                    default :
                        // それ以外では孤独死 or 窒息死
                        nextField[i][j] = DEAD;
                        break;
                }
            }
        }
        field = nextField;
        generation++;
        repaint();
    }

    public void run() {
        while (thread != null) {
            step();
            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paintComponent(Graphics g) {
        // セルを描画
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (field[i][j] == ALIVE) {
                    // 生きていたら黄色
                    g.setColor(Color.YELLOW);
                } else {
                    // 死んでいたら黒色
                    g.setColor(Color.BLACK);
                }
                g.fillRect(j * CS, i * CS, CS, CS);
            }
        }

        // 中心線を描く
        g.setColor(Color.RED);
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
        g.drawLine(0, HEIGHT / 2, WIDTH, HEIGHT / 2);

        // キーの現在位置を囲む
        g.setColor(Color.WHITE);
        g.drawRect(c * CS, r * CS, CS, CS);

        // 世代数を表示
        g.drawString("generation: " + generation, 2, 10);
    }

    /**
     * 世代数を返す。
     * 
     * @return 世代数。
     */
    public int getGeneration() {
        return generation;
    }

    public void mousePressed(MouseEvent e) {
        // フォーカスを移す
        requestFocus();

        int x = e.getX();
        int y = e.getY();

        // キーボードの位置を設定
        c = x / CS;
        r = y / CS;

        // マウスでクリックした位置のセルの生死を反転
        if (field[r][c] == DEAD) {
            field[r][c] = ALIVE;
        } else {
            field[r][c] = DEAD;
        }

        repaint();
    }

    public void mouseClicked(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * 座標(i,j)の周囲8方向の生きているセルの数を数える
     * 
     * @param i 行座標
     * @param j 列座標
     * @return 周囲8方向の生きているセルの数
     */
    private int around(int i, int j) {
        if (i == 0 || i == ROW - 1 || j == 0 || j == COL - 1)
            return 0;
        int sum = 0;
        sum += field[i - 1][j - 1]; // 左上
        sum += field[i][j - 1]; // 左
        sum += field[i + 1][j - 1]; // 左下
        sum += field[i - 1][j]; // 上
        sum += field[i + 1][j]; // 下
        sum += field[i - 1][j + 1]; // 右上
        sum += field[i][j + 1]; // 右
        sum += field[i + 1][j + 1]; // 右下

        return sum;
    }

    /**
     * 情報パネルへの参照をセットする。
     * 
     * @param infoPanel 情報パネル。
     */
    public void setInfoPanel(InfoPanel infoPanel) {
        this.infoPanel = infoPanel;
    }

    /**
     * フィールドにあるライフを保存する。
     *  
     */
    public void saveLife() {
        // 情報パネルからライフの名前を取得
        String lifeName = infoPanel.getLifeName();
        // 情報パネルからライフの説明を取得
        String lifeInfo = infoPanel.getLifeInfo();

        // もし名前が空白なら保存できない
        if (lifeName.equals("")) {
            JOptionPane.showMessageDialog(this, "名前が無いと悲しいです。", "名無し",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ファイルに保存する
        try {
            // ライフを保存するファイル
            File lifeFile = new File("life" + File.separator + lifeName);
            // すでにファイルがあるなら上書きしていいかダイアログを表示
            if (lifeFile.exists()) {
                int answer = JOptionPane.showConfirmDialog(this, lifeFile
                        .getName()
                        + "と同じ名前だけど上書きしていいですか?", "上書き確認",
                        JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.NO_OPTION)
                    return;
            } else {
                // はじめてのライフならコンボボックスにロードする
                // すでにあるファイルを上書きする場合は追加する必要はない
                infoPanel.addLife(lifeName);
            }
            // ファイルを開く
            PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(
                    lifeFile)));
            // ライフの説明を保存
            pr.println(lifeInfo);
            // 生きているセルの場所だけを保存
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (field[i][j] == ALIVE) {
                        pr.println(i + " " + j);
                    }
                }
            }
            // 保存したとき音を鳴らす
            saveAudio.play();
            // ファイルを閉じる
            pr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ライフをファイルから読み込んで配置する。
     * 
     * @param filename ライフが入っているファイル名。
     */
    public void loadLife(String filename) {
        // フィールドをクリアする
        clear();

        try {
            // ライフを読み込む
            BufferedReader br = new BufferedReader(new FileReader("life"
                    + File.separator + filename));
            // 説明を読み込んで情報パネルへ表示
            String lifeInfo = br.readLine();
            infoPanel.setLifeInfo(lifeInfo);
            // 生きているセルの位置を読み込む
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer parser = new StringTokenizer(line);
                while (parser.hasMoreTokens()) {
                    int i = Integer.parseInt(parser.nextToken());
                    int j = Integer.parseInt(parser.nextToken());
                    // フィールドをALIVEに設定
                    field[i][j] = ALIVE;
                }
            }
            // フィールドを再描画
            repaint();
        } catch (FileNotFoundException e) {
            // ファイルが見つからない場合は何もしない
            // コンボボックスに新しく入力したときにこの例外が起きる
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ランダムにライフを配置する。
     *  
     */
    public void randLife() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (rand.nextDouble() < RAND_LIFE) {
                    // RAND_LIFEの確率でセルをALIVEにする
                    field[i][j] = ALIVE;
                }
            }
        }
        repaint();
    }

    /**
     * パネルがキーボードを受け付けるようにする。
     */
    public boolean isFocusable() {
        return true;
    }

    /**
     * キーを押したときカーソルが動く。
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT :
                c--;
                if (c < 0)
                    c = 0;
                break;
            case KeyEvent.VK_RIGHT :
                c++;
                if (c > COL - 1)
                    c = COL - 1;
                break;
            case KeyEvent.VK_UP :
                r--;
                if (r < 0)
                    r = 0;
                break;
            case KeyEvent.VK_DOWN :
                r++;
                if (r > ROW - 1)
                    r = ROW - 1;
                break;
            case KeyEvent.VK_SPACE :
                // スペースを押すと生死が切り替わる
                if (field[r][c] == ALIVE) {
                    field[r][c] = DEAD;
                } else {
                    field[r][c] = ALIVE;
                }
                break;
        }
        repaint();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}