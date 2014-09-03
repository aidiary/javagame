/*
 * Created on 2005/05/04
 *
 */

/**
 * 文字認識
 * @author mori
 */
public class NNTest2 {
    public static void main(String[] args) {
        NeuralNetwork nn = new NeuralNetwork();
        nn.init(25, 50, 10);
        nn.setLearningRate(0.2);
        
        // 訓練データの作成
        double[][] trainingSet = new double[][] {
                {1,1,1,1,1,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,1,1,1,1,1},  // 0
                {0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0},  // 1
                {1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1},  // 2
                {1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1},  // 3
                {1,0,0,0,0,1,0,0,0,0,1,0,1,0,0,1,1,1,1,1,0,0,1,0,0},  // 4
                {1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1},  // 5
                {1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,0,0,0,1,1,1,1,1,1},  // 6
                {1,1,1,1,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1},  // 7
                {1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,0,0,0,1,1,1,1,1,1},  // 8
                {1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,0,0,0,0,1,0,0,0,0,1},  // 9
        };
        
        // 教師信号（それぞれのパターンにあたる部分だけ1）
        // 単位行列になることに注意！
        double[][] teacherSet = new double[][] {
                {1,0,0,0,0,0,0,0,0,0},  // 0
                {0,1,0,0,0,0,0,0,0,0},  // 1
                {0,0,1,0,0,0,0,0,0,0},  // 2
                {0,0,0,1,0,0,0,0,0,0},  // 3
                {0,0,0,0,1,0,0,0,0,0},  // 4
                {0,0,0,0,0,1,0,0,0,0},  // 5
                {0,0,0,0,0,0,1,0,0,0},  // 6
                {0,0,0,0,0,0,0,1,0,0},  // 7
                {0,0,0,0,0,0,0,0,1,0},  // 8
                {0,0,0,0,0,0,0,0,0,1},  // 9
        };
            
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
                    nn.setInput(j, trainingSet[i][j]);
                }
                // 教師信号を設定
                for (int j=0; j<teacherSet[i].length; j++) {
                    nn.setTeacherValue(j, teacherSet[i][j]);
                }
                // 学習開始
                nn.feedForward();
                error += nn.calculateError();
                nn.backPropagate();
            }
            error /= trainingSet.length;
            System.out.println(count + "\t" + error);
        }
        
        // 学習完了
        // 3番目の訓練データを入れてみる
        int testData = 3;
        for (int j=0; j<trainingSet[testData].length; j++) {
            nn.setInput(j, trainingSet[testData][j]);
        }
        nn.feedForward();   // 出力を計算
        int id = nn.getMaxOutputID();
        // ちゃんと3が出力されたでしょ？
        System.out.println(id + " " + nn.getOutput(id));
    }
}
