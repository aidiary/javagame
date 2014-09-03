/*
 * Created on 2005/02/15
 *
 */

/**
 * 環境の状態を表すクラス
 * 
 * @author mori
 *  
 */
public class State {
    
    // 位置の範囲
    public static final double MIN_POS = -1.2;
    public static final double MAX_POS = 0.6;
    
    // 速度の範囲
    private static final double MIN_VEL = -0.07;
    private static final double MAX_VEL = 0.07;
    
    // ゴール位置
    private static final double GOAL_POS = 0.5;
    
    // 車の位置
    public double pos;
    // 車の速度
    public double vel;
    
    public State() {
        this(-0.5, 0);
    }

    public State(double pos, double vel) {
        pos = -0.5;
        vel = 0.0;
    }

    public void init() {
        pos = -0.5;
        vel = 0.0;
    }
    
    public boolean isGoal() {
        if (pos > GOAL_POS) {
            return true;
        }
        return false;
    }
    
    public void bound() {
        // 範囲を超えていないかチェックする
        if (pos > MAX_POS) {
            pos = MAX_POS;
        }
        if (pos < MIN_POS) {
            pos = MIN_POS;
        }
        
        if (vel > MAX_VEL) {
            vel = MAX_VEL;
        }
        if (vel < MIN_VEL) {
            vel = MIN_VEL;
        }
    }
}