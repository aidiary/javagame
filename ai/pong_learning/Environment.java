/*
 * 作成日: 2004/12/16
 *
 */
import java.awt.*;
import java.util.Random;
/**
 * 環境クラス。エージェント以外のすべてを管理する。
 * 
 * @author mori
 *  
 */
public class Environment {
    // 状態数
    public static final int NUM_STATE = 4 * 4 * 8 + 1;
    // 行動数（この環境で選択できる行動の数）
    public static final int NUM_ACTION = 3;

    // ボールのサイズ
    private static final int BALL_SIZE = 10;
    // ラケットのサイズ
    private static final int RACKET_SIZE = MainPanel.WIDTH / 6;

    // 環境の状態
    private State state;

    // ボールの位置
    private int x, y;
    // ボールの速度
    private int vx, vy;
    // バウンド数
    private int bound;

    // 乱数生成器
    private static final Random rand = new Random();

    /**
     * コンストラクタ。
     * 
     * @param panel パネルへの参照。
     */
    public Environment(MainPanel panel) {
        // 状態を作成
        state = new State();
        x = rand.nextInt(MainPanel.WIDTH);
        y = 0;
        vx = 10 - rand.nextInt(5);
        vy = 5;
        bound = 0;
    }

    /**
     * 環境を初期化する。
     */
    public void init() {
        x = rand.nextInt(MainPanel.WIDTH);
        y = 0;
        vx = 10 - rand.nextInt(5);
        vy = 5;
        bound = 0;
    }

    /**
     * 環境の状態を返す。
     * 
     * @return 環境の状態。
     */
    public State getState() {
        return state;
    }

    /**
     * 環境の状態を一意の番号に変換して返す。
     * 
     * @return 環境の状態番号。
     */
    public int getStateNum() {
        int s1, s2, s3;

        // ボールを受け損なったら失敗。
        if (isFailed()) {
            return NUM_STATE - 1;
        }

        // ラケットの位置は4つに分割
        if (state.racketX == 0) {
            s1 = 0;
        } else if (state.racketX == RACKET_SIZE) {
            s1 = 1;
        } else if (state.racketX == RACKET_SIZE * 2) {
            s1 = 2;
        } else {
            s1 = 3;
        }

        // ラケットとボールの垂直距離は4つに分割
        if (state.dist < 75) {
            s2 = 0;
        } else if (state.dist < 150) {
            s2 = 1;
        } else if (state.dist < 225) {
            s2 = 2;
        } else {
            s2 = 3;
        }

        // ボール速度ベクトルの角度は8つに分割
        if (state.angle < 45) {
            s3 = 0;
        } else if (state.angle < 90) {
            s3 = 1;
        } else if (state.angle < 135) {
            s3 = 2;
        } else if (state.angle < 180) {
            s3 = 3;
        } else if (state.angle < 225) {
            s3 = 4;
        } else if (state.angle < 270) {
            s3 = 5;
        } else if (state.angle < 315) {
            s3 = 6;
        } else {
            s3 = 7;
        }

        // こうすると状態番号が一意になる
        return s1 * 4 * 8 + s2 * 8 + s3;
    }

    /**
     * 次の状態へ遷移する。
     * 
     * @param action 力を加える方向。
     */
    public void nextState(int action) {
        // ラケットを動かす
        switch (action) {
            case 0 :
                break;
            case 1 :
                state.racketX -= RACKET_SIZE;
                break;
            case 2 :
                state.racketX += RACKET_SIZE;
                break;
        }
        if (state.racketX < 0) {
            state.racketX = 0;
        } else if (state.racketX > MainPanel.WIDTH - RACKET_SIZE) {
            state.racketX = MainPanel.WIDTH - RACKET_SIZE;
        }

        // ボールの位置を更新
        x += vx;
        y += vy;
        // 壁にあたったら跳ね返る
        if (x < 0) {
            x = 0;
            vx = -vx;
        } else if (x > MainPanel.WIDTH - BALL_SIZE) {
            x = MainPanel.WIDTH - BALL_SIZE;
            vx = -vx;
        }
        // 壁またはラケットにあたったら跳ね返る
        if (y < 0) {
            y = 0;
            vy = -vy;
        } else if (y > MainPanel.HEIGHT - BALL_SIZE
                && x + BALL_SIZE / 2 >= state.racketX
                && x + BALL_SIZE / 2 <= state.racketX + RACKET_SIZE) {
            y = MainPanel.HEIGHT - BALL_SIZE;
            vy = -vy;
            // バウンド数を+1
            bound++;
        }

        state.dist = MainPanel.HEIGHT - y;
        state.angle = Math.atan((double) vy / vx);
        state.angle = state.angle * 180 / Math.PI;
        if (vx > 0 && vy < 0) {
            state.angle = -state.angle;
        } else if (vx > 0 && vy > 0) {
            state.angle = 360 - state.angle;
        } else if (vx < 0 && vy < 0) {
            state.angle = 180 - state.angle;
        } else if (vx < 0 && vy > 0) {
            state.angle = 180 - state.angle;
        }
    }

    /**
     * 状態に応じて報酬を返す。
     * 
     * @return 失敗状態は-1000、それ以外は0。
     */
    public double getReward() {
        // ゴールにいたら0、それ以外なら-1000
        if (isFailed()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * ラケットでボールを跳ね返せたか。
     * 
     * @return 失敗したらtrueを返す。
     */
    public boolean isFailed() {
        if (y > MainPanel.HEIGHT) {
            return true;
        }
        return false;
    }

    /**
     * ボールとラケットを描く
     * 
     * @param g グラフィックスオブジェクト。
     */
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        // ボールを描く
        g.fillOval(x, y, BALL_SIZE, BALL_SIZE);

        g.setColor(Color.MAGENTA);
        // ラケットを描く
        g.fillRect(state.racketX, MainPanel.HEIGHT - 2, RACKET_SIZE, 2);
    }

    /**
     * バウンド数をセットする。
     * 
     * @param bound バウンド数。
     */
    public void setBound(int bound) {
        this.bound = bound;
    }

    /**
     * バウンド数を返す。
     * 
     * @return バウンド数。
     */
    public int getBound() {
        return bound;
    }
}