/*
 * Created on 2005/01/21
 *
 */

/**
 * 環境の状態を表すクラス。
 * 
 * @author mori
 *  
 */
public class State {
    // ラケットの位置
    public int racketX;
    // ラケットとボールの垂直距離
    public int dist;
    // ボール速度ベクトルの角度
    public double angle;

    public State() {
        racketX = 0;
    }
}