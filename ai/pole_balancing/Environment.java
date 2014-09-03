/*
 * 作成日: 2004/12/16
 *
 */
import java.awt.*;
/**
 * 環境クラス。エージェント以外のすべてを管理する。
 * 
 * @author mori
 *  
 */
public class Environment {

    // 台車の質量（単位:kg）
    public static final double MASS_CART = 1.0;
    // 棒の質量（単位:kg）
    public static final double MASS_POLE = 0.1;
    public static final double TOTAL_MASS = MASS_POLE + MASS_CART;
    // 棒の半分の長さ（単位:m）
    public static final double LENGTH = 1;
    public static final double POLE_MASS_LENGTH = MASS_POLE * LENGTH;
    // 台車に加える力（単位:N）
    public static final double FORCE_MAG = 10.0;
    // 重力加速度（単位:m/s^2）
    public static final double GRAVITY = 9.8;
    // ステップ時間（単位:s）
    public static final double TAU = 0.02;

    // 4/3（いちいち計算するのは面倒なので定数にする）
    public static final double FOUR_THIRDS = 1.3333333333333;
    // 角度のラジアン表示（これも予め計算しておく）
    private static final double ONE_DEGREE = 0.0174532;
    private static final double SIX_DEGREES = 0.1047192;
    private static final double TWELVE_DEGREES = 0.2094384;
    private static final double FIFTY_DEGREES = 0.87266;
    private static final double ONE_HUNDRED_EIGHTY_DEGREES = 3.141592;

    // 状態数（3 * 3 * 6 * 3 + 1です）
    public static final int NUM_STATE = 163;
    // 行動数（この環境で選択できる行動の数）
    public static final int NUM_ACTION = 2;

    // 環境の状態
    private State state;

    // 台車のイメージ
    private Image cartImage;
    // 台車の幅（単位:ピクセル）
    private int cartWidth;
    // 台車の高さ（単位:ピクセル）
    private int cartHeight;
    // 地面のy座標。
    private int groundHeight;

    /**
     * コンストラクタ。
     * 
     * @param panel パネルへの参照。
     */
    public Environment(MainPanel panel) {
        // 状態を作成
        state = new State();
        // 地面の場所はパネルの高さの5/6の位置とする
        groundHeight = MainPanel.HEIGHT * 5 / 6;
        // 台車のイメージをロードする
        loadImage(panel);
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
        int s1, s2, s3, s4;

        // 棒が倒れたり、範囲を超えた場合は失敗(162番の状態とする)
        if (isFailed()) {
            return 162;
        }

        // 台車、棒の状態から状態番号を求める
        // 位置xは3つに分割
        if (state.x < -0.8) {
            s1 = 0;
        } else if (state.x < 0.8) {
            s1 = 1;
        } else {
            s1 = 2;
        }

        // 速度xDotは3つに分割
        if (state.xDot < -0.5) {
            s2 = 0;
        } else if (state.xDot < 0.5) {
            s2 = 1;
        } else {
            s2 = 2;
        }

        // 角度thetaは6つに分割
        if (state.theta < -SIX_DEGREES) {
            s3 = 0;
        } else if (state.theta < -ONE_DEGREE) {
            s3 = 1;
        } else if (state.theta < 0) {
            s3 = 2;
        } else if (state.theta < ONE_DEGREE) {
            s3 = 3;
        } else if (state.theta < SIX_DEGREES) {
            s3 = 4;
        } else {
            s3 = 5;
        }

        // 角速度thetaDotは3つに分割
        if (state.thetaDot < -FIFTY_DEGREES) {
            s4 = 0;
        } else if (state.thetaDot < FIFTY_DEGREES) {
            s4 = 1;
        } else {
            s4 = 2;
        }

        // トリッキーだがこうすると状態番号は一意に求まる
        // 3, 6, 3は状態を分割した数
        return s1 * 3 * 6 * 3 + s2 * 6 * 3 + s3 * 3 + s4;
    }

    /**
     * 次の状態へ遷移する。 台車、棒の位置はシミュレーション計算で求める。
     * 
     * @param action 力を加える方向。
     */
    public void nextState(int action) {
        // 台車の加速度
        double xAcc;
        // 棒の角加速度
        double thetaAcc;
        // 台車に加える力
        double force;
        // cosθ
        double cosTheta;
        // sinθ
        double sinTheta;
        double temp;

        // actionに応じて力を加える方向を変える
        force = 0;
        switch (action) {
            case 0 :
                force = -FORCE_MAG;
                break;
            case 1 :
                force = FORCE_MAG;
                break;
        }

        // 台車と棒の運動方程式にしたがって次の状態を決める
        // 台車の詳しい運動方程式はWebを参照してください
        cosTheta = Math.cos(state.theta);
        sinTheta = Math.sin(state.theta);
        temp = (force + POLE_MASS_LENGTH * state.thetaDot * state.thetaDot
                * sinTheta) / TOTAL_MASS;
        thetaAcc = (GRAVITY * sinTheta - cosTheta * temp)
                / (LENGTH * (FOUR_THIRDS - MASS_POLE * cosTheta * cosTheta / TOTAL_MASS));
        xAcc = temp - POLE_MASS_LENGTH * thetaAcc * cosTheta / TOTAL_MASS;

        // 状態を更新する
        state.x += TAU * state.xDot;
        state.xDot += TAU * xAcc;
        state.theta += TAU * state.thetaDot;
        state.thetaDot += TAU * thetaAcc;
    }

    /**
     * 状態に応じて報酬を返す。
     * 
     * @return 失敗状態は-1000、倒れていなかったら0。
     */
    public double getReward() {
        // ゴールにいたら0、それ以外なら-1000
        if (isFailed()) {
            return -1000;
        } else {
            return 0;
        }
    }

    /**
     * 棒が倒れていないか
     * 
     * @return 棒が倒れたらtrueを返す
     */
    public boolean isFailed() {
        // 棒が倒れたり、範囲を超えた場合は失敗
        if (state.x < -2.4 || state.x > 2.4 || state.theta < -TWELVE_DEGREES
                || state.theta > TWELVE_DEGREES) {
            return true;
        }
        return false;
    }

    /**
     * 棒がだらりとたれさがっているか
     * 
     * @return たれさがったらtrueを返す
     */
    public boolean isDroopy() {
        // 棒がだらりとたれさがったらtrueを返す
        if ((state.theta > 0 && state.theta < ONE_HUNDRED_EIGHTY_DEGREES)
                || (state.theta < 0 && state.theta > -ONE_HUNDRED_EIGHTY_DEGREES)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 環境の幅を返す。パネルの幅と同じ。
     * 
     * @return 地面の幅。
     */
    public int getWidth() {
        return MainPanel.WIDTH;
    }

    /**
     * 地面の高さを返す。
     * 
     * @return 地面の高さ。
     */
    public int getGroundHeight() {
        return groundHeight;
    }

    /**
     * 台車と地面を描く。
     * 
     * @param g グラフィックスオブジェクト。
     */
    public void draw(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, groundHeight, MainPanel.WIDTH, MainPanel.HEIGHT
                - groundHeight);

        // 台車の重心の位置
        Point cartPos = new Point();
        // 棒の先端の座標
        Point polePos = new Point();

        // 台車の重心の実座標を計算
        // xを100倍にスケールアップしている
        cartPos.x = (int) (getWidth() / 2 + state.x * 100);
        cartPos.y = getGroundHeight() - cartHeight / 2;

        // 棒の先端の実座標を計算
        // 棒の長さを100倍にスケールアップしている
        polePos.x = (int) (cartPos.x + 2 * LENGTH * 100 * Math.sin(state.theta));
        polePos.y = (int) (cartPos.y - 2 * LENGTH * 100 * Math.cos(state.theta));

        // 台車を描画
        g.drawImage(cartImage, cartPos.x - cartWidth / 2, cartPos.y
                - cartHeight / 2, null);
        // 棒の描画
        g.drawLine(cartPos.x, cartPos.y, polePos.x, polePos.y);
    }

    /**
     * イメージをロードする。
     * 
     * @param panel パネルへの参照。
     */
    private void loadImage(MainPanel panel) {
        MediaTracker tracker = new MediaTracker(panel);

        // 台車のイメージを読み込む
        cartImage = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("cart.gif"));
        tracker.addImage(cartImage, 0);

        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 台車の大きさ
        cartWidth = cartImage.getWidth(panel);
        cartHeight = cartImage.getHeight(panel);
    }
}