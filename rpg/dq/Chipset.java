/*
 * Created on 2006/02/04
 */

/**
 * @author mori
 */
public interface Chipset {
    // チップセットのサイズ（単位：ピクセル）
    public static final int SIZE = 32;
    
    // チップセット
    public static final int FLOOR = 0;  // 床
    public static final int WALL = 1;  // 壁
    public static final int THRONE = 2;  // 台座
    public static final int PLAIN = 3;  // 平地
    public static final int FLOWER = 4;  // 花壇
    public static final int WATER = 5;  // 海
    public static final int BARRIER = 6;  // バリア
    public static final int WOOD = 7;  // 森
    public static final int HILL = 8;  // 丘
    public static final int MOUNTAIN = 9;  // 山
    public static final int DESERT = 10;  // 砂漠
    public static final int POISON = 11;  // 毒沼
    public static final int TOWN = 12;  // 街
    public static final int CASTLE = 13;  // 城
    public static final int BRIDGE = 14;  // 橋
    public static final int DOWNSTAIRS = 15;  // 下り階段
    public static final int UPSTAIRS = 16;  // 上り階段
    public static final int TREASURE = 17;  // 宝箱
    public static final int DOOR = 18;  // ドア
    public static final int CAVE = 19;  // 洞窟
    public static final int WEAPON = 20;  // 武器屋
    public static final int ITEM = 21;  // 道具屋
    public static final int INN = 22;  // 宿屋
    public static final int WARP = 23;  // 旅のとびら
    public static final int MSGBOARD = 24;  // 立て札
    public static final int CHURCH = 25;  // 教会
}
