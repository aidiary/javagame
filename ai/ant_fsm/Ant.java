import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

/*
 * Created on 2005/04/24
 *
 */

/**
 * @author mori
 *  
 */
public class Ant {
    // チップセットのサイズ（単位：ピクセル）
    private static final int CS = 16;

    // 蟻のタイプ
    public static final int BLACK_ANT = 0; // 黒蟻
    public static final int RED_ANT = 1; // 赤蟻

    // 蟻の内部状態
    public static final int FORAGE = 0; // 食べ物を探す
    public static final int GO_HOME = 1; // 巣に戻る
    public static final int THIRSTY = 2; // 水を探す
    public static final int DEAD = 3; // 死

    private int type; // 蟻のタイプ
    private int state; // 蟻の内部状態
    private int x; // 蟻の位置
    private int y;

    private Map map; // マップへの参照
    private MainPanel panel; // パネルへの参照
    private Image image; // 蟻のイメージ

    private Random rand = new Random();

    public Ant(int type, int state, int x, int y, Map map, MainPanel panel) {
        this.type = type;
        this.state = state;
        this.x = x;
        this.y = y;

        this.map = map;
        this.panel = panel;

        // イメージをロード
        if (type == BLACK_ANT) {
            ImageIcon icon = new ImageIcon(getClass().getResource(
                    "black_ant.gif"));
            image = icon.getImage();
        } else {
            ImageIcon icon = new ImageIcon(getClass()
                    .getResource("red_ant.gif"));
            image = icon.getImage();
        }

    }

    /**
     * 内部状態に応じて蟻を動かす
     */
    public void act() {
        System.out.println(state);
        switch (state) {
            case FORAGE :
                forage();
                break;
            case GO_HOME :
                goHome();
                break;
            case THIRSTY :
                thirsty();
                break;
            case DEAD :
                dead();
                break;
        }
    }

    /**
     * 食べ物を探して歩き回る
     */
    private void forage() {
        int dir = rand.nextInt(4);
        switch (dir) {
            case 0 : // 上へ移動
                if (!map.isHit(x, y - 1))
                    y--;
                break;
            case 1 : // 右へ移動
                if (!map.isHit(x + 1, y))
                    x++;
                break;
            case 2 : // 下へ移動
                if (!map.isHit(x, y + 1))
                    y++;
                break;
            case 3 : // 左へ移動
                if (!map.isHit(x - 1, y))
                    x--;
                break;
        }

        // 食べ物を見つけた！
        if (map.isFood(x, y)) {
            // 食べ物を消す
            map.setGround(x, y);
            // 状態遷移
            state = GO_HOME;
            // 新しい食べ物をセット
            map.scatterFood();
        }

        // 毒があった・・・
        if (map.isPoison(x, y)) {
            // 毒を消す
            map.setGround(x, y);
            // 状態遷移
            state = DEAD;
            // 新しい毒をセット
            map.scatterPoison();
        }
    }

    /**
     * 巣に戻る
     */
    private void goHome() {
        int homeX, homeY;

        if (type == BLACK_ANT) {
            homeX = Map.BLACK_HOME_POS.x;
            homeY = Map.BLACK_HOME_POS.y;
        } else {
            homeX = Map.RED_HOME_POS.x;
            homeY = Map.RED_HOME_POS.y;
        }

        if (x < homeX) {
            if (!map.isHit(x + 1, y))
                x++;
        } else if (x > homeX) {
            if (!map.isHit(x - 1, y))
                x--;
        }

        if (y < homeY) {
            if (!map.isHit(x, y + 1))
                y++;
        } else if (y > homeY) {
            if (!map.isHit(x, y - 1))
                y--;
        }

        // 毒があった・・・
        if (map.isPoison(x, y)) {
            // 毒を消す
            map.setGround(x, y);
            // 状態遷移
            state = DEAD;
            // 新しい毒をセット
            map.scatterPoison();
        }

        // 巣についた
        if (x == homeX && y == homeY) {
            // 状態遷移
            state = THIRSTY;
            // 新しい蟻が誕生
            panel.birthAnt(type, homeX, homeY);
        }
    }

    /**
     * 水を探す
     */
    private void thirsty() {
        int dir = rand.nextInt(4);
        switch (dir) {
            case 0 : // 上へ移動
                if (!map.isHit(x, y - 1))
                    y--;
                break;
            case 1 : // 右へ移動
                if (!map.isHit(x + 1, y))
                    x++;
                break;
            case 2 : // 下へ移動
                if (!map.isHit(x, y + 1))
                    y++;
                break;
            case 3 : // 左へ移動
                if (!map.isHit(x - 1, y))
                    x--;
                break;
        }

        // 水があった！
        if (map.isWater(x, y)) {
            // 水を消す
            map.setGround(x, y);
            // 状態遷移
            state = FORAGE;
            // 新しい水をセット
            map.scatterWater();
        }

        // 毒があった・・・
        if (map.isPoison(x, y)) {
            // 毒を消す
            map.setGround(x, y);
            // 状態遷移
            state = DEAD;
            // 新しい毒をセット
            map.scatterPoison();
        }
    }

    /**
     * 蟻が死ぬ
     */
    private void dead() {
        x = y = -1;
    }

    /*
     * 蟻を描画
     */
    public void draw(Graphics g) {
        g.drawImage(image, x * CS, y * CS, null);
    }
}