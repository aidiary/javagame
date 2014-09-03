import java.awt.Point;
import java.util.LinkedList;

/*
 * Created on 2005/04/17
 *
 */

/**
 * ノードクラス
 * 
 * @author mori
 */
public class Node implements Comparable {
    // ノードのマップ内での座標
    public Point pos;
    // スタート地点からのコスト
    public int costFromStart;
    // ゴールまでのヒューリスティックコスト
    public int heuristicCostToGoal;
    // 親ノード
    public Node parentNode;

    public Node(Point pos) {
        this.pos = pos;
    }

    /**
     * ヒューリスティックコストを計算する
     * 
     * @param node 対象ノード
     * @return ヒューリスティックコスト
     */
    public int getHeuristicCost(Node node) {
        // 対象ノードまでの直線距離をヒューリスティックコストとする
        // (x1, y1)-(x2, y2)間の直線距離は√(x2-x1)^2 + (y2-y1)^2
        int m = node.pos.x - pos.x;
        int n = node.pos.y - pos.y;

        return (int) Math.sqrt(m * m + n * n);
    }

    /**
     * ノードが同じか調べる（オーバーライド）
     * 
     * @param ノード
     * @return 同じノードだったらtrueを返す
     */
    public boolean equals(Object node) {
        // ノードの座標が一緒だったら同じとみなす
        if (pos.x == ((Node) node).pos.x && pos.y == ((Node) node).pos.y) {
            return true;
        }

        return false;
    }

    /**
     * ノードの大小をコストを元に比較する（オーバーライド）
     */
    public int compareTo(Object node) {
        // コスト=スタートノードからのコスト+ヒューリスティックコスト
        int c1 = costFromStart + heuristicCostToGoal;
        int c2 = ((Node) node).costFromStart
                + ((Node) node).heuristicCostToGoal;

        if (c1 < c2)
            return -1;
        else if (c1 == c2)
            return 0;
        else
            return 1;
    }

    /**
     * 隣接するノードのリストを返す
     * 
     * @return 隣接ノードのリスト
     */
    public LinkedList getNeighbors() {
        LinkedList neighbors = new LinkedList();
        int x = pos.x;
        int y = pos.y;

        // 上下左右のみしか移動できないように制限した

        // 上
        neighbors.add(new Node(new Point(x, y - 1)));
        // 右上
        // neighbors.add(new Node(new Point(x+1, y-1)));
        // 右
        neighbors.add(new Node(new Point(x + 1, y)));
        // 右下
        // neighbors.add(new Node(new Point(x+1, y+1)));
        // 下
        neighbors.add(new Node(new Point(x, y + 1)));
        // 左下
        // neighbors.add(new Node(new Point(x-1, y+1)));
        // 左
        neighbors.add(new Node(new Point(x - 1, y)));
        // 左上
        // neighbors.add(new Node(new Point(x-1, y-1)));

        return neighbors;
    }
}