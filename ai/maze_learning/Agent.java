import java.util.*;

/**
 * エージェントクラス。強化学習で利用する各メソッドを実装。
 * 
 * @author mori
 *  
 */
public class Agent {
    // 強化学習パラメータ
    private static final double ALPHA = 0.1; // 学習率
    private static final double EPSILON = 0.01; // 探査率
    private static final double GAMMA = 0.95; // 割引率

    // 迷路から取得した状態数、行動数
    private int numState;
    private int numAction;

    // 価値関数
    private double[][] Q;

    // 迷路への参照
    private Maze maze;

    // 乱数発生器
    private Random rand;

    /**
     * コンストラクタ。
     * 
     * @param maze 迷路への参照。
     */
    public Agent(Maze maze) {
        this.maze = maze;
        // 状態数・行動数を迷路環境から取得
        numState = maze.getNumState();
        numAction = maze.getNumAction();
        // 価値関数を作成
        Q = new double[numState][numAction];
        // 価値関数を初期化
        initQ();
        rand = new Random();
    }

    /**
     * 価値関数を初期化する。
     */
    public void initQ() {
        for (int s = 0; s < numState; s++) {
            for (int a = 0; a < numAction; a++) {
                Q[s][a] = 0.0;
            }
        }
    }

    /**
     * 価値関数にしたがって行動を選択する。最大の価値関数値を持つ行動を選ぶ貪欲（greedy）法を用いる。
     * 
     * @return 選択した行動。
     */
    public int selectBestAction() {
        // 現在の環境の状態を取得
        int s = maze.getStateNum();
        // 最大の価値関数値を持つ行動
        int bestAction = 0;

        // 最大の価値関数値を持つ行動を探す
        for (int a = 1; a < numAction; a++) {
            if (Q[s][a] > Q[s][bestAction]) {
                bestAction = a;
            }
        }

        return bestAction;
    }

    /**
     * 価値関数にしたがって行動を選択する。epsilonの確率でランダム行動を取るe-greedy法を用いる。
     * 
     * @return 選択した行動。
     */
    public int selectAction() {
        int action = 0;

        // とりあえずgreedy法で行動選択
        action = selectBestAction();
        // EPSILONの確率でランダム行動を選択
        if (rand.nextDouble() < EPSILON) {
            action = rand.nextInt(numAction);
        }

        return action;
    }

    /**
     * Q-Learningを使って価値関数を更新する。
     * 
     * @param s 状態。
     * @param a 行動。
     * @param r 報酬。
     * @param next_s 次の状態。
     * @param next_a 次の行動。
     */
    public void updateQ(int s, int a, double r, int next_s, int next_a) {
        Q[s][a] += ALPHA * (r + GAMMA * Q[next_s][next_a] - Q[s][a]);
    }
}