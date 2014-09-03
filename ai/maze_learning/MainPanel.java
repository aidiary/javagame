import java.awt.*;
import javax.swing.*;

public class MainPanel extends JPanel implements Runnable {
    // パネルサイズ
    private static final int WIDTH = 624;
    private static final int HEIGHT = 624;

    // 1エピソードの最大ステップ数（Max Steps per Episode）
    private static final double MSE = 10000;

    // 迷路環境
    private Maze maze;
    // エージェント
    private Agent agent;
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
     * コンストラクタ。迷路とエージェントを作成してスレッドを開始。
     */
    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        maze = new Maze(39, 39, new Point(1, 1), new Point(37, 37), this);
        agent = new Agent(maze);

        sleepTime = 100;
        skipFlag = false;

        thread = new Thread(this);
        thread.start();
    }

    /**
     * 迷路とエージェントを初期化して最初から学習を始める。
     */
    public void init() {
        // ランダム迷路を作り直す
        maze.build();
        // エージェントの学習結果を消去
        agent.initQ();
        episode = step = 0;
        infoPanel.setEpisodeLabel(0 + "");
        infoPanel.setStepLabel(0 + "");
    }

    /**
     * 強化学習で迷路を学習する。
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
        // 1エピソード内の報酬合計
        int sumReward;

        episode = 0;
        // 価値関数を初期化
        agent.initQ();
        while (true) {
            // エピソード開始
            step = 0;
            sumReward = 0;
            // 迷路の状態を初期化する
            maze.init();
            // 状態を知覚する
            state = maze.getStateNum();
            while (!maze.isGoal() && step < MSE) {
                // ステップ開始
                // 行動を選択＋実際に移動
                action = agent.selectAction();
                maze.nextState(action);
                if (!skipFlag) repaint();
                // 次状態を知覚する
                nextState = maze.getStateNum();
                // 報酬を得る
                reward = maze.getReward();
                // 価値が最大の行動を選択する（移動はしない）
                nextAction = agent.selectBestAction();
                // 価値関数を更新する（Q-Learning）
                agent.updateQ(state, action, reward, nextState, nextAction);
                // 次状態へ遷移
                state = nextState;
                sumReward += reward;
                // ステップ終了
                step++;

                if (!skipFlag) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // エピソード終了
            //System.out.println("EPISODE: " + episode + "\t" + "STEP: " + step);
            infoPanel.setEpisodeLabel(episode + "");
            infoPanel.setStepLabel(step + "");
            episode++;
        }
    }

    /**
     * アニメーションをスキップするかを設定する。
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
     * 迷路とエージェントを描く。
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        if (!skipFlag) {
            // 迷路を描く
            maze.draw(g);
        } else {
            drawTextCentering(g, "Now Learning...");
        }
    }

    /**
     * 現在のエピソード数を返す。
     * @return エピソード数。
     */
    public int getEpisode() {
        return episode;
    }

    /**
     * 現在のステップ数を返す。
     * @return ステップ数。
     */
    public int getStep() {
        return step;
    }

    /**
     * インフォメーションパネルへの参照をセットする。
     * @param panel　インフォメーションパネル。
     */
    public void setInfoPanel(InfoPanel panel) {
        infoPanel = panel;
    }

    // 画面の中央に文字列を表示する
    private void drawTextCentering(Graphics g, String s) {
        Font f = new Font("SansSerif", Font.BOLD, 20);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        g.setColor(Color.YELLOW);
        g.drawString(
            s,
            WIDTH / 2 - fm.stringWidth(s) / 2,
            HEIGHT / 2 + fm.getDescent());
    }
}
