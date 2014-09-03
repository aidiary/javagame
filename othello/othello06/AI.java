/*
 * Created on 2004/12/22
 *
 */

/**
 * オセロのAI。
 * 
 * @author mori
 *  
 */
public class AI {
    // 深読みするレベル（大きい値だとものすごい時間がかかってしまうので注意）
    private static final int SEARCH_LEVEL = 5;
    // メインパネルへの参照
    private MainPanel panel;

    /**
     * コンストラクタ。メインパネルへの参照を保存。
     * 
     * @param panel メインパネルへの参照。
     */
    public AI(MainPanel panel) {
        this.panel = panel;
    }

    /**
     * コンピュータの手を決定する。
     *  
     */
    public void compute() {
        // ミニマックス法で石を打つ場所を決める
        // 戻ってくる値は bestX+bestY*MASU
        int temp = minMax(true, SEARCH_LEVEL);

        // 場所を求める
        int x = temp % MainPanel.MASU;
        int y = temp / MainPanel.MASU;

        // 打った場所、ひっくり返した石の位置を記録
        Undo undo = new Undo(x, y);
        // その場所に実際に石を打つ
        panel.putDownStone(x, y, false);
        // 実際にひっくり返す
        panel.reverse(undo, false);
        // 手番を変える
        panel.nextTurn();
    }

    /**
     * ミニマックス法。最善手を探索する。打つ場所を探すだけで実際には打たない。
     * 
     * @param flag AIの手番のときtrue、プレイヤーの手番のときfalse。
     * @param level 先読みの手数。
     * @return 子ノードでは盤面の評価値。ルートノードでは最大評価値を持つ場所（bestX + bestY * MAS）。
     */
    private int minMax(boolean flag, int level) {
        // ノードの評価値
        int value;
        // 子ノードから伝播してきた評価値
        int childValue;
        // ミニマックス法で求めた最大の評価値を持つ場所
        int bestX = 0;
        int bestY = 0;

        // ゲーム木の末端では盤面評価
        // その他のノードはMIN or MAXで伝播する
        if (level == 0) {
            return 0; // 実際は盤面を評価して評価値を決める。ここではとりあえず0にしておく。
        }

        if (flag) {
            // AIの手番では最大の評価値を見つけたいので最初に最小値をセットしておく
            value = Integer.MIN_VALUE;
        } else {
            // プレイヤーの手番では最小の評価値を見つけたいので最初に最大値をセットしておく
            value = Integer.MAX_VALUE;
        }

        // 打てるところはすべて試す（試すだけで実際には打たない）
        for (int y = 0; y < MainPanel.MASU; y++) {
            for (int x = 0; x < MainPanel.MASU; x++) {
                if (panel.canPutDown(x, y)) {
                    Undo undo = new Undo(x, y);
                    // 試しに打ってみる（盤面描画はしないのでtrue指定）
                    panel.putDownStone(x, y, true);
                    // ひっくり返す（盤面描画はしないのでtrue指定）
                    panel.reverse(undo, true);
                    // 手番を変える
                    panel.nextTurn();
                    // 子ノードの評価値を計算（再帰）
                    // 今度は相手の番なのでflagが逆転する
                    childValue = minMax(!flag, level - 1);
                    // 子ノードとこのノードの評価値を比較する
                    if (flag) {
                        // AIのノードなら子ノードの中で最大の評価値を選ぶ
                        if (childValue > value) {
                            value = childValue;
                            bestX = x;
                            bestY = y;
                        }
                    } else {
                        // プレイヤーのノードなら子ノードの中で最小の評価値を選ぶ
                        if (childValue < value) {
                            value = childValue;
                            bestX = x;
                            bestY = y;
                        }
                    }
                    // 打つ前に戻す
                    panel.undoBoard(undo);
                }
            }
        }

        if (level == SEARCH_LEVEL) {
            // ルートノードなら最大評価値を持つ場所を返す
            return bestX + bestY * MainPanel.MASU;
        } else {
            // 子ノードならノードの評価値を返す
            return value;
        }
    }
}