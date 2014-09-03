import java.util.Random;

/*
 * Created on 2005/05/03
 *
 */

/**
 * ニューラルネットを構成する層クラス
 * 
 * @author mori
 *  
 */
public class Layer {
    int numNodes; // ノード数
    int numChildNodes; // 子層のノード数
    int numParentNodes; // 親層のノード数
    double[][] weights; // この層と子層間の重み
    double[] neuronValues; // ノードの活性値
    double[] teacherValues; // 教師信号
    double[] errors; // 誤差
    double[] biasWeights; // バイアスの重み
    double[] biasValues; // バイアス値（バイアスの重み*バイアス値がいわゆる閾値）
    double learningRate; // 学習率

    Layer parentLayer; // 親層への参照
    Layer childLayer; // 子層への参照

    Random rand;

    public Layer() {
        parentLayer = null;
        childLayer = null;

        rand = new Random();
    }

    /**
     * 層を初期化する
     * 
     * @param numNodes 層に含まれるノード数
     * @param parent 親層への参照
     * @param child 子層への参照
     */
    public void init(int numNodes, Layer parent, Layer child) {
        neuronValues = new double[numNodes];
        teacherValues = new double[numNodes];
        errors = new double[numNodes];

        if (parent != null) { // 隠れ層・出力層
            parentLayer = parent;
        }

        if (child != null) { // 入力層・隠れ層
            childLayer = child;

            weights = new double[numNodes][numChildNodes];

            // 入力層・隠れ層が隠れ層・出力層のバイアスを管理する
            // だからnumChildNodes個の大きさになっている
            // こうするとプログラムが簡潔になる
            biasValues = new double[numChildNodes];
            biasWeights = new double[numChildNodes];
        } else {
            weights = null;
            biasValues = null;
            biasWeights = null;
        }

        // 0で初期化
        for (int i = 0; i < numNodes; i++) {
            neuronValues[i] = 0;
            teacherValues[i] = 0;
            errors[i] = 0;

            if (child != null) { // 入力層・隠れ層
                for (int j = 0; j < numChildNodes; j++) {
                    weights[i][j] = 0;
                }
            }
        }

        // バイアス値と重みを初期化
        if (child != null) { // 入力層・隠れ層
            for (int i = 0; i < numChildNodes; i++) {
                biasValues[i] = -1;
                biasWeights[i] = 0;
            }
        }

        // 学習率
        learningRate = 0.2;
    }

    /**
     * 重みをランダムに設定する
     */
    public void randomizeWeights() {
        rand.setSeed(System.currentTimeMillis());

        // 重みは-1.0～1.0の乱数
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numChildNodes; j++) {
                int num = rand.nextInt(200);
                weights[i][j] = num / 100.0 - 1;
            }
        }

        // バイアスも-1.0～1.0の乱数
        for (int i = 0; i < numChildNodes; i++) {
            int num = rand.nextInt(200);
            biasWeights[i] = num / 100.0 - 1;
        }
    }

    /**
     * 誤差を計算する
     */
    public void calculateErrors() {
        if (childLayer == null) { // 出力層
            for (int i = 0; i < numNodes; i++) {
                errors[i] = (teacherValues[i] - neuronValues[i])
                        * neuronValues[i] * (1.0 - neuronValues[i]);
            }
        } else if (parentLayer == null) { // 入力層
            for (int i = 0; i < numNodes; i++) {
                errors[i] = 0.0;
            }
        } else { // 隠れ層
            for (int i = 0; i < numNodes; i++) {
                double sum = 0;
                for (int j = 0; j < numChildNodes; j++) {
                    sum += childLayer.errors[j] * weights[i][j];
                }
                errors[i] = sum * neuronValues[i] * (1.0 - neuronValues[i]);
            }
        }
    }

    /**
     * 誤差をもとに重みを調整する
     */
    public void adjustWeights() {
        double dw;

        if (childLayer != null) {  // 入力層・隠れ層
            // 重みを調整
            for (int i = 0; i < numNodes; i++) {
                for (int j = 0; j < numChildNodes; j++) {
                    dw = learningRate * childLayer.errors[j] * neuronValues[i];
                    weights[i][j] += dw;
                }
            }

            // バイアス（閾値）も調整
            for (int i = 0; i < numChildNodes; i++) {
                biasWeights[i] += learningRate * childLayer.errors[i]
                        * biasValues[i];
            }
        }
    }

    /**
     * 層の各ニューロンの活性値を計算する（前向き）
     */
    public void calculateNeuronValues() {
        double sum;

        if (parentLayer != null) {  // 隠れ層・出力層
            for (int j = 0; j < numNodes; j++) {
                sum = 0.0;
                // 親の層の出力値と重みをかけて足し合わせる
                for (int i = 0; i < numParentNodes; i++) {
                    sum += parentLayer.neuronValues[i]
                            * parentLayer.weights[i][j];
                }
                // バイアス（閾値）
                // バイアスは親の層が持っている
                sum += parentLayer.biasValues[j] * parentLayer.biasWeights[j];

                // シグモイド関数を通す
                neuronValues[j] = sigmoid(sum);
            }
        }
    }

    /**
     * シグモイド関数
     */
    private double sigmoid(double x) {
        return 1.0 / (1 + Math.exp(-x));
    }
}