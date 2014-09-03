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

    private static final double G = 9.8; // 重力加速度
    private static final double M1 = 1.0; // 棒1の質量
    private static final double M2 = 1.0; // 棒2の質量
    // Stateクラスでも使うのでpublic指定
    public static final double L1 = 1.0; // 棒1の長さ
    public static final double L2 = 1.0; // 棒2の長さ
    private static final double LC1 = 0.5; // 棒1の重心までの距離
    private static final double LC2 = 0.5; // 棒2の重心までの距離
    private static final double I1 = 1.0; // 棒1の慣性モーメント
    private static final double I2 = 1.0; // 棒2の慣性モーメント
    private static final double T = 0.05; // 時間ステップ

    // 状態分割で使う角度のラジアン表示
    private static final double ONE_FORTH = 0.785394;
    private static final double TWO_FORTHS = 1.570788;
    private static final double THREE_FORTHS = 2.356182;
    private static final double ONE = 3.141592;
    private static final double FIVE_FORTHS = 3.92697;
    private static final double SIX_FORTHS = 4.712364;
    private static final double SEVEN_FORTHS = 5.497758;
    private static final double TWO = 6.283184;
    private static final double THREE = 9.424776;
    private static final double FOUR = 12.566368;
    private static final double SIX = 18.849552;
    private static final double NINE = 28.274328;
   
    // バーの高さ Stateクラスでも使うのでpublic指定
    public static final int BAR_HEIGHT = 30;

    // 状態数（8 * 4 * 8 * 6）
    public static final int NUM_STATE = 1536;
    // 行動数（この環境で選択できる行動の数）
    public static final int NUM_ACTION = 3;

    // 環境の状態
    private State state;

    /**
     * コンストラクタ
     * 
     * @param panel パネルへの参照
     */
    public Environment(MainPanel panel) {
        // 状態を作成
        state = new State();
    }

    /**
     * 環境を初期化する
     */
    public void init() {
        state.init();
    }

    /**
     * 環境の状態を返す
     * 
     * @return 環境の状態
     */
    public State getState() {
        return state;
    }

    /**
     * 環境の状態を一意の番号に変換して返す
     * 
     * @return 環境の状態番号
     */
    public int getStateNum() {
        int s1, s2, s3, s4;

        // 8部分に分割
        if (state.theta1 < ONE_FORTH) {
            s1 = 0;
        } else if (state.theta1 < TWO_FORTHS) {
            s1 = 1;
        } else if (state.theta1 < THREE_FORTHS) {
            s1 = 2;
        } else if (state.theta1 < ONE) {
            s1 = 3;
        } else if (state.theta1 < FIVE_FORTHS) {
            s1 = 4;
        } else if (state.theta1 < SIX_FORTHS) {
            s1 = 5;
        } else if (state.theta1 < SEVEN_FORTHS) {
            s1 = 6;
        } else {
            s1 = 7;
        }

        // 4部分に分割
        if (state.thetaDot1 < -TWO) {
            s2 = 0;
        } else if (state.thetaDot1 < 0) {
            s2 = 1;
        } else if (state.thetaDot1 < TWO) {
            s2 = 2;
        } else {
            s2 = 3;
        }

        // 8部分に分割
        if (state.theta2 < ONE_FORTH) {
            s3 = 0;
        } else if (state.theta2 < TWO_FORTHS) {
            s3 = 1;
        } else if (state.theta2 < THREE_FORTHS) {
            s3 = 2;
        } else if (state.theta2 < ONE) {
            s3 = 3;
        } else if (state.theta2 < FIVE_FORTHS) {
            s3 = 4;
        } else if (state.theta2 < SIX_FORTHS) {
            s3 = 5;
        } else if (state.theta2 < SEVEN_FORTHS) {
            s3 = 6;
        } else {
            s3 = 7;
        }

        // 6部分に分割
        if (state.thetaDot2 < -SIX) {
            s4 = 0;
        } else if (state.thetaDot2 < -THREE) {
            s4 = 1;
        } else if (state.thetaDot2 < 0) {
            s4 = 2;
        } else if (state.thetaDot2 < THREE) {
            s4 = 3;
        } else if (state.thetaDot2 < SIX) {
            s4 = 4;
        } else {
            s4 = 5;
        }

        // 状態は全部で1536通り。0-1535を返す
        // 下の式は状態番号を一意に求めるためのもの
        return s1 * 4 * 8 * 6 + s2 * 8 * 6 + s3 * 6 + s4;
    }

    /**
     * 次の状態へ遷移する
     * 
     * @param action 力を加える方向
     */
    public void nextState(int action) {
        double thetaAcc1, thetaAcc2, d1, d2, phi1, phi2;
        double tau = 0;

        switch (action) {
            case (0) :
                tau = 1.0;
                break;
            case (1) :
                tau = -1.0;
                break;
            case (2) :
                tau = 0.0;
                break;
        }

        // アクロバットの運動方程式
        d1 = M1 * LC1 * LC1 + M2
                * (L1 * L1 + LC2 * LC2 + 2 * L1 * LC2 * Math.cos(state.theta2))
                + I1 + I2;
        d2 = M2 * (LC2 * LC2 + L1 * LC2 * Math.cos(state.theta2)) + I2;
        phi1 = -M2 * L1 * LC2 * state.thetaDot2 * state.thetaDot2 * Math.sin(state.theta2)
                - 2 * M2 * L1 * LC2 * state.thetaDot2 * state.thetaDot1
                * Math.sin(state.theta2) + (M1 * LC1 + M2 * L1) * G
                * Math.cos(state.theta1 - Math.PI / 2) + state.theta2;
        phi2 = M2 * LC2 * G * Math.cos(state.theta1 + state.theta2 - Math.PI / 2);

        thetaAcc2 = 1
                / (M2 * LC2 * LC2 + I2 - (d2 * d2 / d1))
                * (tau + (d2 / d1) * phi1 - M2 * L1 * LC2 * state.thetaDot1
                        * state.thetaDot1 * Math.sin(state.theta2) - phi2);
        thetaAcc1 = -(1 / d1) * (d2 * thetaAcc2 + phi1);

        state.thetaDot1 += T * thetaAcc1;
        // 範囲内かチェック
        if (state.thetaDot1 < -FOUR) {
            state.thetaDot1 = -FOUR;
        }
        if (state.thetaDot1 > FOUR) {
            state.thetaDot1 = FOUR;
        }
        
        state.theta1 += T * state.thetaDot1;

        state.thetaDot2 += T * thetaAcc2;
        // 範囲内かチェック
        if (state.thetaDot2 < -NINE) {
            state.thetaDot2 = -NINE;
        }
        if (state.thetaDot2 > NINE) {
            state.thetaDot2 = NINE;
        }
        
        state.theta2 += T * state.thetaDot2;
    }

    /**
     * 状態に応じて報酬を返す。
     * 
     * @return ゴール状態では0、それ以外では-1
     */
    public double getReward() {
        if (state.isTouchBar()) {
            return 0;
        }
        return -1;
    }

    /**
     * 1エピソードが終了したらtrueを返す
     * 
     * @return 足先が棒に届いていたらtrue
     */
    public boolean isEnd() {
        return state.isTouchBar();
    }

    /**
     * 車と山を描く
     * 
     * @param g グラフィックスオブジェクト
     */
    public void draw(Graphics g) {
        int x1, y1;  // 棒1の先端座標
        int x2, y2;  // 棒2の先端座標
        
        int width = MainPanel.WIDTH;
        int height = MainPanel.HEIGHT;
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        
        // 100はスケール
        x1 = (int) ((L1 * 100) * Math.sin(state.theta1) + width/2);
        y1 = (int) ((L1 * 100) * Math.cos(state.theta1) + height/2);

        x2 = (int) ((L1 * 100) * Math.sin(state.theta1) + (L2 * 100)
                * Math.sin(state.theta1 + state.theta2) + width/2);
        y2 = (int) ((L1 * 100) * Math.cos(state.theta1) + (L2 * 100)
                * Math.cos(state.theta1 + state.theta2) + 200); 
        
        g.setColor(Color.RED);
        // 中心から棒1の先端座標まで直線を描く
        g.drawLine(width/2, height/2, x1, y1);
        
        g.setColor(Color.BLUE);
        // 棒1の先端座標から棒2の先端座標まで直線を描く
        g.drawLine(x1, y1, x2, y2);
        
        g.setColor(Color.DARK_GRAY);
        g.drawLine(0, BAR_HEIGHT, width, BAR_HEIGHT);
    }
}