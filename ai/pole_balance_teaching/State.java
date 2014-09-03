/*
 * Created on 2005/01/21
 *
 */

/**
 * 環境の状態を表すクラス。
 * @author mori
 *
 */
public class State {
    // 台車の位置
    public double x;
    // 台車の速度
    public double xDot;
    // 棒の角度
    public double theta;
    // 棒の角速度
    public double thetaDot;

    public State() {
        this(0, 0, 0, 0);
    }

    public State(double x, double xDot, double theta, double thetaDot) {
        this.x = x;
        this.xDot = xDot;
        this.theta = theta;
        this.thetaDot = thetaDot;
    }

    public void init() {
        this.x = 0;
        this.xDot = 0;
        this.theta = 0;
        this.thetaDot = 0;
    }
}
