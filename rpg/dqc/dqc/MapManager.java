package dqc;

import java.util.HashMap;

public class MapManager {
    // マップ名
    private static final String[] mapNames = {
        "king_room", "castle", "overworld", "town"};
    
    // マップ名->マップオブジェクトへのハッシュ
    private HashMap maps;

    // 現在のマップ
    public Map curMap;
    
    public MapManager() {
        maps = new HashMap();
        
        loadMap();
    }
    
    /**
     * マップを返す
     * 
     * @param mapName マップ名
     * @return マップオブジェクト
     */
    public Map getMap(String mapName) {
        return (Map)maps.get(mapName);
    }
    
    /**
     * 現在のマップを返す
     * 
     * @return 現在のマップ
     */
    public Map getCurMap() {
        return curMap;
    }
    
    /**
     * 移動先のマップをセットする
     * 
     * @param map 移動先のマップ
     */
    public void setCurMap(Map map) {
        curMap = map;
    }
    
    /**
     * マップをロードする
     *
     */
    private void loadMap() {
        for (int i=0; i<mapNames.length; i++) {
            Map map = new Map(mapNames[i]);
            maps.put(mapNames[i], map);
        }
    }
}
