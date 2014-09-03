/**
 * 環境の状態を表すクラス。迷路環境の状態はエージェントの座標によって決まる。
 * 
 * @author mori
 *  
 */
public class State {
    // エージェントの座標
    public int x;
    public int y;

    public State(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
}