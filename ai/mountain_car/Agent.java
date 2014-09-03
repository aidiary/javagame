/*
 * 作成日: 2005/02/15
 *
 */
import java.util.*;

/**
 * エージェントクラス 強化学習で利用する各メソッドを実装
 * 
 * @author mori
 *  
 */
public class Agent {
    // 強化学習パラメータ
    private static final double ALPHA = 0.1; // 学習率
    private static final double EPSILON = 0; // 探査率
    private static final double GAMMA = 1.0; // 割引率

    // 状態数・行動数を環境から取得
    // いちいちEnvironment.をつけるのはめんどくさいから
    private static final int NUM_STATE = Environment.NUM_STATE;
    private static final int NUM_ACTION = Environment.NUM_ACTION;

    // 価値関数（価値関数の更新＝学習です）
    private double[][] Q;
    // 乱数発生器
    private Random rand;
    // 環境への参照
    private Environment env;

    /**
     * コンストラクタ
     * 
     * @param env 環境への参照
     */
    public Agent(Environment env) {
        this.env = env;

        // 価値関数を作成
        Q = new double[NUM_STATE][NUM_ACTION];
        // 価値関数を初期化
        initQ();
        rand = new Random();
    }

    /**
     * 価値関数を初期化する
     */
    public void initQ() {
        for (int s = 0; s < NUM_STATE; s++) {
            for (int a = 0; a < NUM_ACTION; a++) {
                Q[s][a] = 0.0;
            }
        }
    }

    /**
     * 価値関数にしたがって行動を選択する。最大の価値関数値を持つ行動を選ぶ貪欲（greedy）法を用いる
     * 
     * @return 選択した行動
     */
    public int selectBestAction() {
        // 現在の環境の状態を取得
        int s = env.getStateNum();
        // 最大の価値関数値を持つ行動
        int bestAction = 0;

        // 最大の価値関数値を持つ行動を探す
        for (int a = 1; a < NUM_ACTION; a++) {
            if (Q[s][a] > Q[s][bestAction]) {
                bestAction = a;
            }
        }

        return bestAction;
    }

    /**
     * 価値関数にしたがって行動を選択する。epsilonの確率でランダム行動を取るe-greedy法を用いる
     * 
     * @return 選択した行動
     */
    public int selectAction() {
        int action = 0;

        // とりあえずgreedy法で行動選択
        action = selectBestAction();
        // EPSILONの確率でランダム行動を選択
        if (rand.nextDouble() < EPSILON) {
            action = rand.nextInt(NUM_ACTION);
        }

        return action;
    }

    /**
     * Q-Learningを使って価値関数を更新する
     * 
     * @param s 状態
     * @param a 行動
     * @param r 報酬
     * @param next_s 次の状態
     * @param next_a 次の行動
     */
    public void updateQ(int s, int a, double r, int next_s, int next_a) {
        Q[s][a] += ALPHA * (r + GAMMA * Q[next_s][next_a] - Q[s][a]);
    }
}