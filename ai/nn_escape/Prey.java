import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/*
 * Created on 2005/05/13
 *
 */

/**
 * 獲物クラス
 * ニューラルネットを使って逃避行動を学習する
 * @author mori
 *
 */
public class Prey extends Chara {
    private static final int GS = MainPanel.GS;
    
    // 訓練データセット
    // 周囲9マスで追跡者がいる場所を表している
    // 012
    // 345
    // 678
    private static final int[][] trainingSet = new int[][] {
            {1,0,0,0,0,0,0,0,0},  // 0
            {0,1,0,0,0,0,0,0,0},  // 1
            {0,0,1,0,0,0,0,0,0},  // 2
            {0,0,0,1,0,0,0,0,0},  // 3
            {0,0,0,0,0,0,0,0,0},  // 周りにいない
            {0,0,0,0,0,1,0,0,0},  // 5
            {0,0,0,0,0,0,1,0,0},  // 6
            {0,0,0,0,0,0,0,1,0},  // 7
            {0,0,0,0,0,0,0,0,1},  // 8
    };
    
    // 教師信号
    // 周囲9マスの逃げる場所を表している
    private static final int[][] teacherSet = new int[][] {
            {0,0,0,0,0,0,0,0,1},  // 8
            {0,0,0,0,0,0,0,1,0},  // 7
            {0,0,0,0,0,0,1,0,0},  // 6
            {0,0,0,0,0,1,0,0,0},  // 5
            {0,0,0,0,1,0,0,0,0},  // 4（移動しない）
            {0,0,0,1,0,0,0,0,0},  // 3
            {0,0,1,0,0,0,0,0,0},  // 2
            {0,1,0,0,0,0,0,0,0},  // 1
            {1,0,0,0,0,0,0,0,0},  // 0
    };
    
    // センサー
    private int[] sensor;
    // ニューラルネット
    private NeuralNetwork brain;
    // 追跡者への参照
    private Predator predator;
    private Random rand;
    
    public Prey(int x, int y, Predator predator) {
        super(x, y);
        this.predator = predator;
        
        brain = new NeuralNetwork();
        brain.init(9, 50, 9);

        // 訓練データセットと教師信号を元に学習する
        // ニューラルネットは予め学習しておく必要がある
        learn();
        
        // センサーは周囲9マス
        sensor = new int[9];
        
        rand = new Random(System.currentTimeMillis());
    }

    /**
     * ニューラルネットの出力に基づいて逃げる
     */
    public void escape() {
        // 周囲を観察してセンサーデータを取得
        // sensor[]に格納される
        sense();
        
        // ニューラルネットにsensorの値をセット
        for (int i=0; i<sensor.length; i++) {
            brain.setInput(i, sensor[i]);
        }
        // 出力を計算
        brain.feedForward();
        // 最大出力を持つノード番号を取得
        int dir = brain.getMaxOutputID();
        // その方向に逃げる
        move(dir);
    }
    
    /**
     * 獲物を描画する
     * 
     * @param g 描画オブジェクト
     */
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x * GS, y * GS, GS, GS);
    }
    
    /**
     * 訓練データを学習する
     */
    private void learn() {
        // 訓練データを学習
        double error = 1.0;
        int count = 0;
        while ((error > 0.0001) && (count < 50000)) {
            error = 0;
            count++;
            // 各訓練データを誤差が小さくなるまで繰り返し学習
            for (int i=0; i<trainingSet.length; i++) {
                // 入力値を設定
                for (int j=0; j<trainingSet[i].length; j++) {
                    brain.setInput(j, trainingSet[i][j]);
                }
                // 教師信号を設定
                for (int j=0; j<teacherSet[i].length; j++) {
                    brain.setTeacherValue(j, teacherSet[i][j]);
                }
                // 学習開始
                brain.feedForward();
                error += brain.calculateError();
                brain.backPropagate();
            }
            error /= trainingSet.length;
//            System.out.println(count + "\t" + error);
        }
    }
    
    /**
     * センサーデータをセットする
     */
    private void sense() {
        // 0で初期化
        for (int i=0; i<sensor.length; i++) {
            sensor[i] = 0;
        }
        
        // 追跡者の位置を取得
        int px = predator.x;
        int py = predator.y;
        
        // 追跡者のいる場所のセンサーを1にセット
        if (x-1 == px && y-1 == py) {
            sensor[0] = 1;
        } else if (x == px && y-1 == py) {
            sensor[1] = 1;
        } else if (x+1 == px && y-1 == py) {
            sensor[2] = 1;
        } else if (x-1 == px && y == py) {
            sensor[3] = 1;
        } else if (x+1 == px && y == py) {
            sensor[5] = 1;
        } else if (x-1 == px && y+1 == py) {
            sensor[6] = 1;
        } else if (x ==px && y+1 == py) {
            sensor[7] = 1;
        } else if (x+1 == px && y+1 == py) {
            sensor[8] = 1;
        }
      
//        for (int i=0; i<sensor.length; i++) {
//            System.out.print(sensor[i]);
//        }
//        System.out.println();
    }
}
