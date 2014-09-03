import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

/*
 * Created on 2005/04/16
 *
 */

/**
 * @author mori
 *  
 */
public class Predator {
    // グリッドサイズ
    private static final int CS = 16;

    // 方向定数
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;

    // 位置
    public int x;
    public int y;

    // 向き
    private int direction;

    // マップへの参照
    private Map map;

    // 追跡者のイメージ
    private Image image;

    public Predator(Map map) {
        this(0, 0, map);
    }

    public Predator(int x, int y, Map map) {
        this.x = x;
        this.y = y;
        this.map = map;
        direction = DOWN;

        // イメージをロード
        ImageIcon icon = new ImageIcon(getClass().getResource("enemy.gif"));
        image = icon.getImage();
    }

    /**
     * 左手法で巡回する 向いている方向に対して左、前、右、後の優先順位で移動する
     *  
     */
    public void patrol() {
        if (direction == RIGHT) { // 右を向いているとき
            if (!map.isHit(x, y - 1)) { // 左手（つまり上）に移動できれば
                // 移動する
                y--;
                // 上を向く
                direction = UP;
            } else if (!map.isHit(x + 1, y)) { // 前（右）
                x++;
                direction = RIGHT;
            } else if (!map.isHit(x, y + 1)) { // 右手（下）
                y++;
                direction = DOWN;
            } else if (!map.isHit(x - 1, y)) { // 後（左）
                x--;
                direction = LEFT;
            }
        } else if (direction == DOWN) { // 下を向いているとき
            if (!map.isHit(x + 1, y)) { // 左手（右）
                x++;
                direction = RIGHT;
            } else if (!map.isHit(x, y + 1)) { // 前（下）
                y++;
                direction = DOWN;
            } else if (!map.isHit(x - 1, y)) { // 右手（左）
                x--;
                direction = LEFT;
            } else if (!map.isHit(x, y - 1)) { // 後（上）
                y--;
                direction = UP;
            }
        } else if (direction == LEFT) { // 左を向いているとき
            if (!map.isHit(x, y + 1)) { // 左手（下）
                y++;
                direction = DOWN;
            } else if (!map.isHit(x - 1, y)) { // 前（左）
                x--;
                direction = LEFT;
            } else if (!map.isHit(x, y - 1)) { // 右手（上）
                y--;
                direction = UP;
            } else if (!map.isHit(x + 1, y)) { // 後（右）
                x++;
                direction = RIGHT;
            }
        } else if (direction == UP) { // 上を向いているとき
            if (!map.isHit(x - 1, y)) { // 左手（左）
                x--;
                direction = LEFT;
            } else if (!map.isHit(x, y - 1)) { // 前（上）
                y--;
                direction = UP;
            } else if (!map.isHit(x + 1, y)) { // 右手（右）
                x++;
                direction = RIGHT;
            } else if (!map.isHit(x, y + 1)) { // 後（下）
                y++;
                direction = DOWN;
            }
        }
    }

    /**
     * 追跡者を描画する
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g) {
        g.drawImage(image, x * CS, y * CS, x * CS + CS, y * CS + CS, direction
                * CS, 0, direction * CS + CS, CS, null);
    }
}