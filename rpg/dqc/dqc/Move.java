/*
 * Created on 2007/04/15
 */
package dqc;

public class Move extends MoveEvent {
    public Move(int x, int y, int imageNo, String destMapName, int destX, int destY) {
        super(x, y, imageNo, destMapName, destX, destY);
    }

    public void start(Hero hero, Map map, MessageWindow msgWnd) {
        DQC.soundManager.playSE("stairs");

        // 現在のマップから主人公を削除
        map.removeEvent(hero);
        
        // 移動先のマップに主人公を追加
        Map destMap = DQC.mapManager.getMap(getDestMapName());
        destMap.addEvent(hero);

        // 主人公の状態を再セット
        hero.setX(getDestX());
        hero.setY(getDestY());
        hero.setDirection(DQC.DOWN);
        hero.setMap(destMap);
        
        DQC.mapManager.setCurMap(destMap);
        DQC.soundManager.playBGM(destMap.getBGM());
    }
    
    /**
     * イベントの文字列を返す（デバッグ用）
     * 
     * @return イベント文字列
     */
    public String toString() {
        return "MOVE," + x + "," + y + "," + imageNo + "," + destMapName + "," + destX + "," + destY;
    }
}
