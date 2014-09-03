/*
 * Created on 2007/04/15
 */
package dqc;

public abstract class OpenEvent extends Event {

    public OpenEvent(int x, int y, int imageNo) {
        super(x, y, imageNo, false);
    }

    public abstract void start(Hero hero, Map map, MessageWindow msgWnd);
}
