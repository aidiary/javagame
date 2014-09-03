/*
 */
package dqc;

public abstract class SearchEvent extends Event {

    public SearchEvent(int x, int y, int imageNo, boolean isMovable) {
        super(x, y, imageNo, isMovable);
    }
    
    public abstract void start(Hero hero, Map map, MessageWindow msgWnd);
}
