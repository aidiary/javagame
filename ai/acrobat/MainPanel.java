/*
 * 作成日: 2005/02/15
 *
 */
import java.awt.*;
import javax.swing.*;
/**
 * メインパネル 強化学習を実装してアニメーションにしている
 * 
 * @author mori
 *  
 */
public class MainPanel extends JPanel implements Runnable {
    // パネルサイズ
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;

    // エージェント
    private Agent agent;
    // 環境
    private Environment env;
    // エージェントアニメーション用スレッド
    private Thread thread;
    // アニメーションのスピード
    private int sleepTime;
    // アニメーションをスキップするかのフラグ
    private boolean skipFlag;
    // エピソード数とステップ数（インフォメーションパネルに表示するのでインスタンス変数にする）
    private int episode, step;
    // InfoPanelへの参照
    private InfoPanel infoPanel;

    /**
     * コンストラクタ。環境とエージェントを作成してスレッドを開始。
     */
    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        env = new Environment(this);
        agent = new Agent(env);

        sleepTime = 50;
        skipFlag = false;

        thread = new Thread(this);
        thread.start();
    }

    /**
     * パネルを初期化する。
     */
    public void init() {
        agent.initQ();
        env.init();
        episode = step = 0;
        infoPanel.setEpisodeLabel(0 + "");
        infoPanel.setStepLabel(0 + "");
    }

    /**
     * 強化学習で倒立振子制御問題を学習する。
     */
    public void run() {
        // 状態
        int state;
        // 行動
        int action;
        // 次状態
        int nextState;
        // 次行動
        int nextAction;
        // 報酬
        double reward;

        episode = 0;
        // 価値関数を初期化
        agent.initQ();
        while (true) {
            // エピソード開始
            step = 0;
            // 環境の状態を初期化する
            env.init();
            // 環境の状態を知覚する
            state = env.getStateNum();
            while (!env.isEnd() && step < 10000) {
                // ステップ開始
                // 行動を選択
                action = agent.selectAction();
                // 行動に応じて環境の状態を遷移
                env.nextState(action);
                // 次状態を知覚する
                nextState = env.getStateNum();
                // 報酬を得る
                reward = env.getReward();
                // 価値が最大の行動を選択する（移動はしない）
                nextAction = agent.selectBestAction();
                // 価値関数を更新する（Q-Learning）
                agent.updateQ(state, action, reward, nextState, nextAction);
                // 次状態へ遷移
                state = nextState;
                // ステップ終了
                step++;
                infoPanel.setStepLabel(step + "");

                if (!skipFlag) {
                    repaint();
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // エピソード終了
            //System.out.println("EPISODE: " + episode + "\t" + "STEP: " +
            // step);
            episode++;
            infoPanel.setEpisodeLabel(episode + "");
        }
    }

    /**
     * アニメーションをスキップするかを設定する。
     * 
     * @param flag スキップするならtrue。
     */
    public void skip(boolean flag) {
        if (flag) {
            skipFlag = true;
        } else {
            skipFlag = false;
        }
    }

    /**
     * 地面とエージェントを描く。
     */
    public void paintComponent(Graphics g) {
        // 画面をクリアする
        g.setColor(getBackground());
        g.fillRect(0, 0, WIDTH, HEIGHT);
        if (!skipFlag) {
            // 地面を描く
            env.draw(g);
        } else {
            drawTextCentering(g, "Now Learning...");
        }
    }

    /**
     * 現在のエピソード数を返す。
     * 
     * @return エピソード数。
     */
    public int getEpisode() {
        return episode;
    }

    /**
     * 現在のステップ数を返す。
     * 
     * @return ステップ数。
     */
    public int getStep() {
        return step;
    }

    /**
     * インフォメーションパネルへの参照をセットする。
     * 
     * @param panel インフォメーションパネル。
     */
    public void setInfoPanel(InfoPanel panel) {
        infoPanel = panel;
    }

    // 画面の中央に文字列を表示する
    private void drawTextCentering(Graphics g, String s) {
        Font f = new Font("SansSerif", Font.BOLD, 20);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        g.setColor(Color.RED);
        g.drawString(s, WIDTH / 2 - fm.stringWidth(s) / 2, HEIGHT / 2
                + fm.getDescent());
    }
}

