/*
 * 作成日: 2005/02/15
 *
 */
import java.awt.*;
/**
 * 環境クラス エージェント以外のすべてを管理する
 * 
 * @author mori
 *  
 */
public class Environment {

    // 状態数
    public static final int NUM_STATE = 320;
    // 行動数（この環境で選択できる行動の数）
    public static final int NUM_ACTION = 3;

    // 環境の状態
    private State state;

    /**
     * コンストラクタ。
     * 
     * @param panel パネルへの参照。
     */
    public Environment(MainPanel panel) {
        // 状態を作成
        state = new State();
    }

    /**
     * 環境を初期化する。
     */
    public void init() {
        state.init();
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
        int s1, s2;

        // 車の位置、速度から状態番号を求める
        // 位置は20分割
        if (state.pos < -1.2) {
            s1 = 0;
        } else if (state.pos < -1.1) {
            s1 = 1;
        } else if (state.pos < -1.0) {
            s1 = 2;
        } else if (state.pos < -0.9) {
            s1 = 3;
        } else if (state.pos < -0.8) {
            s1 = 4;
        } else if (state.pos < -0.7) {
            s1 = 5;
        } else if (state.pos < -0.6) {
            s1 = 6;
        } else if (state.pos < -0.5) {
            s1 = 7;
        } else if (state.pos < -0.4) {
            s1 = 8;
        } else if (state.pos < -0.3) {
            s1 = 9;
        } else if (state.pos < -0.2) {
            s1 = 10;
        } else if (state.pos < -0.1) {
            s1 = 11;
        } else if (state.pos < 0) {
            s1 = 12;
        } else if (state.pos < 0.1) {
            s1 = 13;
        } else if (state.pos < 0.2) {
            s1 = 14;
        } else if (state.pos < 0.3) {
            s1 = 15;
        } else if (state.pos < 0.4) {
            s1 = 16;
        } else if (state.pos < 0.5) {
            s1 = 17;
        } else if (state.pos < 0.6) {
            s1 = 18;
        } else {
            s1 = 19;
        }

        // 速度は16分割
        if (state.vel < -0.07) {
            s2 = 0;
        } else if (state.vel < -0.06) {
            s2 = 1;
        } else if (state.vel < -0.05) {
            s2 = 2;
        } else if (state.vel < -0.04) {
            s2 = 3;
        } else if (state.vel < -0.03) {
            s2 = 4;
        } else if (state.vel < -0.02) {
            s2 = 5;
        } else if (state.vel < -0.01) {
            s2 = 6;
        } else if (state.vel < 0) {
            s2 = 7;
        } else if (state.vel < 0.01) {
            s2 = 8;
        } else if (state.vel < 0.02) {
            s2 = 9;
        } else if (state.vel < 0.03) {
            s2 = 10;
        } else if (state.vel < 0.04) {
            s2 = 11;
        } else if (state.vel < 0.05) {
            s2 = 12;
        } else if (state.vel < 0.06) {
            s2 = 13;
        } else if (state.vel < 0.07) {
            s2 = 14;
        } else {
            s2 = 15;
        }

        // トリッキーだがこうすると状態番号は一意に求まる
        return s1 * 16 + s2;
    }

    /**
     * 次の状態へ遷移する
     * 
     * @param action 力を加える方向
     */
    public void nextState(int action) {
        // 速度を更新する
        state.vel += 0.001 * (action - 1) + -0.0025 * Math.cos(3 * state.pos);
        state.bound();
        // 位置を更新する
        state.pos += state.vel;
        state.bound();
    }

    /**
     * 状態に応じて報酬を返す。
     * 
     * @return ゴール状態では0、それ以外では-1
     */
    public double getReward() {
        // ゴールにいたら0、それ以外なら-1
        if (state.isGoal()) {
            return 0;
        }

        return -1;
    }

    /**
     * 1エピソードが終了したらtrueを返す
     * 
     * @return 車がゴール状態にいたらtrue
     */
    public boolean isEnd() {
        return state.isGoal();
    }

    /**
     * 車と山を描く
     * 
     * @param g グラフィックスオブジェクト
     */
    public void draw(Graphics g) {
        // 1mあたりのピクセル数を求める
        int wScale = (int) (MainPanel.WIDTH / 1.8);
        int hScale = (int) ((MainPanel.HEIGHT - 20) / 2.0);

        // メインパネルの大きさ
        int width = MainPanel.WIDTH;
        int height = MainPanel.HEIGHT;

        // 画面をクリアする
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 坂を描く
        g.setColor(Color.DARK_GRAY);
        // 0.01mおきに線でつないで曲線を描く
        Point p, oldp;
        oldp = new Point(0, (int) ((height / 2) - Math.sin(3 * State.MIN_POS)
                * hScale));
        for (double x = State.MIN_POS; x < State.MAX_POS; x += 0.01) {
            // 山は-sin3tの曲線
            // 1.2を足しているのは左端を0とするため
            p = new Point((int) ((x + 1.2) * wScale),
                    (int) ((height / 2) - Math.sin(3 * x) * hScale));
            g.drawLine(oldp.x, oldp.y, p.x, p.y);
            oldp = p;
        }

        // エージェントを描く
        g.fillOval((int) ((state.pos + 1.2) * wScale - 10), (int) ((height / 2)
                - Math.sin(3 * state.pos) * hScale - 10), 20, 20);
    }
}