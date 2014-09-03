import java.awt.Point;
import java.util.LinkedList;

/*
 * Created on 2005/04/17
 *
 */

/**
 * A*クラス
 * 
 * @author mori
 */
public class Astar {
    // オープンリスト
    private PriorityList openList;
    // クローズドリスト
    private LinkedList closedList;
    // マップへの参照
    private Map map;

    public Astar(Map map) {
        this.map = map;
        // PriorityListは自作クラス（Astarの内部クラスとして定義）
        openList = new PriorityList();
        closedList = new LinkedList();
    }

    /**
     * A*で求めたスタートからゴールまでのパスを返す
     * 
     * @param startPos スタート地点
     * @param goalPos ゴール地点
     * @return A*で求めたパス
     */
    public LinkedList searchPath(Point startPos, Point goalPos) {
        int cnt = 0;
        // スタートノードとゴールノードを作成
        Node startNode = new Node(startPos);
        Node goalNode = new Node(goalPos);

        // スタートノードを設定
        startNode.costFromStart = 0;
        startNode.heuristicCostToGoal = startNode.getHeuristicCost(goalNode);
        startNode.parentNode = null;

        // スタートノードをオープンリストに追加
        openList.add(startNode);

        // オープンリストが空になるまでまわす
        while (!openList.isEmpty()) {
            // openListはコストが小さい順に並んでいるため
            // 最小コストノードは一番目にある
            // そのノードを取り出す
            Node curNode = (Node) openList.removeFirst();

            // そのノードがゴールノードと一致しているか
            // 一致（equals）の定義はNodeクラスで定義
            if (curNode.equals(goalNode)) {
                // ゴールノードからパスを生成
                // goalNodeはコストなどが設定されてないので
                // 引数としてcurNodeを渡すところに注意
                return constructPath(curNode);
            } else { // 一致してない場合
                // 現在のノードをクローズドリストに移す
                closedList.add(curNode);
                // 現在のノードに隣接する各ノードを調べる
                LinkedList neighbors = curNode.getNeighbors();
                for (int i = 0; i < neighbors.size(); i++) { // 各隣接ノードに対して
                    // ノードを1つ取得
                    Node neighborNode = (Node) neighbors.get(i);
                    // 条件検査用情報を取得
                    // オープンリストに含まれているか？
                    boolean isOpen = openList.contains(neighborNode);
                    // クローズドリストに含まれているか？
                    boolean isClosed = closedList.contains(neighborNode);
                    // 障害物でないか？
                    boolean isHit = map.isHit(neighborNode.pos.x,
                            neighborNode.pos.y);

                    if (!isOpen && !isClosed && !isHit) {
                        // オープンリストに移してコストを計算する
                        // スタートからのコスト（costFromStart）は親のコストの隣なので+1する
                        neighborNode.costFromStart = curNode.costFromStart + 1;
                        // ヒューリスティックコスト
                        neighborNode.heuristicCostToGoal = neighborNode
                                .getHeuristicCost(goalNode);
                        // 親ノード
                        neighborNode.parentNode = curNode;
                        // オープンリストに追加
                        openList.add(neighborNode);
                    }
                }
            }
        }

        // 再呼び出しがあるかもしれないので消去しておく
        openList.clear();
        closedList.clear();

        // ループをまわしてパスが見つからなかった場合はnullを返す
        return null;
    }

    /**
     * ゴールノードまでのパスを構築する
     * 
     * @param node ゴールノード
     * @return スタートノードからゴールノードまでのパス
     */
    private LinkedList constructPath(Node node) {
        LinkedList path = new LinkedList();

        // 親ノードを次々たどる
        while (node.parentNode != null) {
            // 最初に追加するのがミソ
            // スタートノードがLinkedListの先頭
            // ゴールノードがLinkedListの最後になるようにする
            path.addFirst(node);
            node = node.parentNode;
        }

        // スタートノード（node.parentNode == nullとなるノード）
        // も追加しておく
        path.addFirst(node);

        return path;
    }

    /**
     * オープンリストの中身を表示する補助メソッド
     */
    private void showOpenList() {
        System.out.println("【オープンリスト】");
        for (int i = 0; i < openList.size(); i++) {
            Node node = (Node) openList.get(i);
            System.out.print("(" + node.pos.x + ", " + node.pos.y + ")");
        }
        System.out.println();
    }

    /**
     * クローズドリストの中身を表示する補助メソッド
     */
    private void showClosedList() {
        System.out.println("【クローズドリスト】");
        for (int i = 0; i < closedList.size(); i++) {
            Node node = (Node) closedList.get(i);
            System.out.print("(" + node.pos.x + ", " + node.pos.y + ")");
        }
        System.out.println();
    }

    /**
     * 自動的にコストの小さい順にノードが並ぶように拡張したリスト
     */
    private class PriorityList extends LinkedList {
        // リストに追加するaddメソッドをオーバーライド
        public void add(Node node) {
            for (int i = 0; i < size(); i++) {
                // ノードの大小を調べて小さい順に並ぶように追加する
                // ノードの大小を調べるcompareToはNodeクラスで定義
                if (node.compareTo(get(i)) <= 0) {
                    add(i, node);
                    return;
                }
            }
            addLast(node);
        }
    }
}