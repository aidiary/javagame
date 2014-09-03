/*
 * Created on 2005/12/25
 *
 */

/**
 * @author mori
 *
 */
public class ActionKey {
    // キーのモード
    // キーが押されている間はisPressed()はtrueを返す
    public static final int NORMAL = 0;
    // キーがはじめに押されたときだけisPressed()はtrueを返す
    // キーが押され続けても2回目以降はfalseを返す
    // このモードを使うとジャンプボタンを押し続けてもジャンプを繰り返さない
    public static final int DETECT_INITIAL_PRESS_ONLY = 1;
    
    // キーの状態
    // キーが離された
    private static final int STATE_RELEASED = 0;
    // キーが押されている
    private static final int STATE_PRESSED = 1;
    // キーが離されるのを待っている
    private static final int STATE_WAITING_FOR_RELEASE = 2;
    
    // キーのモード
    private int mode;
    // キーが押された回数
    private int amount;
    // キーの状態
    private int state;
    
    /**
     * 普通のコンストラクタではノーマルモード
     */
    public ActionKey() {
        this(NORMAL);
    }
    
    /**
     * モードを指定できるコンストラクタ
     * @param mode キーのモード
     */
    public ActionKey(int mode) {
        this.mode = mode;
        reset();
    }
    
    /**
     * キーの状態をリセット
     */
    public void reset() {
        state = STATE_RELEASED;
        amount = 0;
    }
    
    /**
     * キーが押されたとき呼び出す
     */
    public void press() {
        // STATE_WAITING_FOR_RELEASEのときは押されたことにならない
        if (state != STATE_WAITING_FOR_RELEASE) {
            amount++;
            state = STATE_PRESSED;
        }
    }
    
    /**
     * キーが離されたとき呼び出す
     */
    public void release() {
        state = STATE_RELEASED;
    }
    
    /**
     * キーが押されたか
     * @return 押されたらtrueを返す
     */
    public boolean isPressed() {
        if (amount != 0) {
            if (state == STATE_RELEASED) {
                amount = 0;
            } else if (mode == DETECT_INITIAL_PRESS_ONLY) {
                // 最初の1回だけtrueを返して押されたことにする
                // 次回からはSTATE_WAITING_FOR_RELEASEになるため
                // キーを押し続けても押されたことにならない
                state = STATE_WAITING_FOR_RELEASE;
                amount = 0;
            }
            
            return true;
        }
        
        return false;
    }
}
