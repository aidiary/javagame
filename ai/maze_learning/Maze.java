import java.awt.*;
import java.util.*;

public class Maze {
    // グリッドサイズ（Grid Size）
    private static final int GS = 16;

    // 方向を表す定数
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;

    // 床と壁の定数値を定義
    private static final int FLOOR = 0;
    private static final int WALL = 1;

    // 迷路の行数、列数
    private int row;
    private int col;
    // スタートの座標
    private Point startPos;
    // ゴールの座標
    private Point goalPos;
    // 状態数
    private int numState;
    // 行動数
    private int numAction;

    // 迷路
    private int[][] maze;
    // 環境の状態
    private State state;

    // 乱数生成器
    private Random rand;
    // イメージ
    private Image floorImage, wallImage, throneImage, slimeImage;

    /**
     * コンストラクタ。
     * 
     * @param row 迷路の行数。
     * @param col 迷路の列数。
     * @param start 迷路のスタート座標。
     * @param goal 迷路のゴール座標。
     */
    public Maze(int row, int col, Point start, Point goal, Component panel) {
        this.row = row;
        this.col = col;
        startPos = new Point(start);
        goalPos = new Point(goal);
        // 状態数は迷路のマスの数だけある
        numState = row * col;
        // 行動数は上下左右の4つ
        numAction = 4;

        rand = new Random();

        // ランダム迷路を作成
        maze = new int[row][col];

        // 状態を初期化
        state = new State(startPos.x, startPos.y);
        build();
        
        // イメージをロード
        loadImage(panel);
    }

    /**
     * 迷路を初期化する。
     * 迷路ではエージェントの位置が迷路の状態なので
     * エージェントをスタート地点に戻す
     * 
     */
    public void init() {
        state.setPos(startPos.x, startPos.y);
    }

    /**
     * エージェントのいる座標から状態番号を計算して返す。 迷路の左上から右下に向かって各マスに順番に番号を割り当てている。
     * 
     * @return 状態番号。
     */
    public int getStateNum() {
        return state.y * getCol() + state.x;
    }

    /**
     * 次の状態へ遷移する。 実際にはエージェントを移動する。
     * 
     * @param action 移動する方向。
     */
    public void nextState(int action) {
        switch (action) {
            case UP :
                if (!isHit(state.x, state.y - 1))
                    state.y--;
                break;
            case DOWN :
                if (!isHit(state.x, state.y + 1))
                    state.y++;
                break;
            case LEFT :
                if (!isHit(state.x - 1, state.y))
                    state.x--;
                break;
            case RIGHT :
                if (!isHit(state.x + 1, state.y))
                    state.x++;
                break;
        }
    }

    /**
     * 環境の状態に応じて報酬を返す。
     * 
     * @return ゴールにいたら0、それ以外の状態なら-1を得る。
     */
    public double getReward() {
        // ゴールにいたら0、それ以外なら-1
        if (isGoal()) {
            return 0.0;
        } else {
            return -1.0;
        }
    }

    /**
     * (x,y)に壁があるか調べる。
     * 
     * @param x x座標。
     * @param y y座標。
     * @return (x,y)に壁があったらtrueを返す。
     */
    public boolean isHit(int x, int y) {
        if (maze[y][x] == WALL)
            return true;
        else
            return false;
    }

    /**
     * エージェントがゴールにいるか調べる。
     * 
     * @return エージェントがゴールにいたらtrueを返す。
     */
    public boolean isGoal() {
        if (state.x == getGoalPos().x && state.y == getGoalPos().y) {
            return true;
        }
        return false;
    }

    /**
     * 迷路を描く。
     * 
     * @param g グラフィックスオブジェクト。
     */
    public void draw(Graphics g) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (maze[i][j] == FLOOR)
                    g.drawImage(floorImage, j * GS, i * GS, null);
                else if (maze[i][j] == WALL)
                    g.drawImage(wallImage, j * GS, i * GS, null);
            }
        }

        // スタートとゴールのイメージを描く
        g.drawImage(throneImage, startPos.x * GS, startPos.y * GS, null);
        g.drawImage(throneImage, goalPos.x * GS, goalPos.y * GS, null);

        // エージェントを描く
        g.drawImage(slimeImage, state.x * GS, state.y * GS, GS, GS, null);
    }

    /**
     * 迷路の行数を返す。
     * 
     * @return 行数。
     */
    public int getRow() {
        return row;
    }

    /**
     * 迷路の列数を返す。
     * 
     * @return 列数。
     */
    public int getCol() {
        return col;
    }

    /**
     * 迷路の状態数を返す。
     * 
     * @return 状態数。
     */
    public int getNumState() {
        return numState;
    }

    /**
     * 迷路で取りえる行動数を返す。
     * 
     * @return 行動数。
     */
    public int getNumAction() {
        return numAction;
    }

    /**
     * スタート座標を返す。
     * 
     * @return スタート座標。
     */
    public Point getStartPos() {
        return new Point(startPos);
    }
    
    /**
     * ゴール座標を返す。
     * 
     * @return ゴール座標。
     */
    public Point getGoalPos() {
        return new Point(goalPos);
    }

    /**
     * イメージをロードする。
     * 
     * @param panel
     */
    private void loadImage(Component panel) {
        MediaTracker tracker = new MediaTracker(panel);

        // 床のイメージを読み込む
        floorImage = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("floor.gif"));
        tracker.addImage(floorImage, 0);

        // 壁のイメージを読み込む
        wallImage = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("wall.gif"));
        tracker.addImage(wallImage, 0);

        // スタートとゴールのイメージを読み込む
        throneImage = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("throne.gif"));
        tracker.addImage(throneImage, 0);

        // スライムのイメージを読み込む
        slimeImage = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("slime.gif"));
        tracker.addImage(slimeImage, 0);

        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 迷路の壁を棒倒し法で作る。
     */
    public void build() {
        init();
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                maze[i][j] = FLOOR;
            }
        }
        
        // 北側外壁を作る
        for (int i = 0; i < col; i++)
            maze[0][i] = WALL;
        // 南側外壁を作る
        for (int i = 0; i < col; i++)
            maze[row - 1][i] = WALL;
        // 西側外壁を作る
        for (int i = 0; i < row; i++)
            maze[i][0] = WALL;
        // 東側外壁を作る
        for (int i = 0; i < row; i++)
            maze[i][col - 1] = WALL;
        // 内壁を1つおきに作る
        for (int i = 0; i < row; i += 2)
            for (int j = 0; j < col; j += 2)
                maze[i][j] = WALL;
        // 一番左の内壁は北南西東のいずれかに壁をのばす
        for (int i = 2; i < row - 1; i += 2) {
            int dir = rand.nextInt(4);
            switch (dir) {
                case UP :
                    maze[i - 1][2] = WALL;
                    break;
                case DOWN :
                    maze[i + 1][2] = WALL;
                    break;
                case LEFT :
                    maze[i][1] = WALL;
                    break;
                case RIGHT :
                    maze[i][3] = WALL;
                    break;
            }
        }
        // その他の内壁は北南東のいずれかに壁をのばす
        for (int i = 2; i < row - 1; i += 2) {
            for (int j = 4; j < col - 1; j += 2) {
                int dir = rand.nextInt(4);
                switch (dir) {
                    case UP :
                        maze[i - 1][j] = WALL;
                        break;
                    case DOWN :
                        maze[i + 1][j] = WALL;
                        break;
                    case RIGHT :
                        maze[i][j + 1] = WALL;
                        break;
                }
            }
        }
    }
}