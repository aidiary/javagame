/*
 * Created on 2005/05/04
 *
 */

/**
 * @author mori
 *
 */
public class NeuralNetwork {
    private Layer inputLayer;   // 入力層
    private Layer hiddenLayer;  // 隠れ層
    private Layer outputLayer;  // 出力層
    
    /**
     * ニューラルネットを初期化
     * @param numInputNodes 入力層のノード数
     * @param numHiddenNodes 隠れ層のノード数
     * @param numOutputNodes 出力層のノード数
     */
    public void init(int numInputNodes, int numHiddenNodes, int numOutputNodes) {
        inputLayer = new Layer();
        hiddenLayer = new Layer();
        outputLayer = new Layer();
        
        // 入力層（親層がないことに注意）
        inputLayer.numNodes = numInputNodes;
        inputLayer.numChildNodes = numHiddenNodes;
        inputLayer.numParentNodes = 0;
        inputLayer.init(numInputNodes, null, hiddenLayer);
        inputLayer.randomizeWeights();
        
        // 隠れ層
        hiddenLayer.numNodes = numHiddenNodes;
        hiddenLayer.numChildNodes = numOutputNodes;
        hiddenLayer.numParentNodes = numInputNodes;
        hiddenLayer.init(numHiddenNodes, inputLayer, outputLayer);
        hiddenLayer.randomizeWeights();

        // 出力層（子層がないことに注意）
        outputLayer.numNodes = numOutputNodes;
        outputLayer.numChildNodes = 0;
        outputLayer.numParentNodes = numHiddenNodes;
        outputLayer.init(numOutputNodes, hiddenLayer, null);
        // 出力層に重みはないのでrandomizeWeights()は必要ない
    }
    
    /**
     * 入力層への入力を設定する
     * @param i ノード番号
     * @param value 値
     */
    public void setInput(int i, double value) {
        if ((i >= 0) && (i < inputLayer.numNodes)) {
            inputLayer.neuronValues[i] = value;
        }
    }
    
    /**
     * 出力層の出力を得る
     * @param i ノード番号
     * @return 出力層i番目のノードの出力値
     */
    public double getOutput(int i) {
        if ((i >= 0) && (i < outputLayer.numNodes)) {
            return outputLayer.neuronValues[i];
        }
        
        return Double.MAX_VALUE;  // エラー
    }
    
    /**
     * 教師信号を設定する
     * @param i ノード番号
     * @param value 教師信号の値
     */
    public void setTeacherValue(int i, double value) {
        if ((i >= 0) && (i < outputLayer.numNodes)) {
            outputLayer.teacherValues[i] = value;
        }
    }
    
    /**
     * 前向き伝播（順番は重要）
     */
    public void feedForward() {
        inputLayer.calculateNeuronValues();
        hiddenLayer.calculateNeuronValues();
        outputLayer.calculateNeuronValues();
    }
    
    /**
     * 逆向き伝播（順番は重要）
     */
    public void backPropagate() {
        outputLayer.calculateErrors();
        hiddenLayer.calculateErrors();
        
        hiddenLayer.adjustWeights();
        inputLayer.adjustWeights();
    }
    
    /**
     * 出力層で最大出力を持つノード番号を返す
     * @return 最大出力を持つノード番号
     */
    public int getMaxOutputID() {
        double max = outputLayer.neuronValues[0];
        int id = 0;
        
        for (int i=1; i<outputLayer.numNodes; i++) {
            if (outputLayer.neuronValues[i] > max) {
                max = outputLayer.neuronValues[i];
                id = i;
            }
        }
        
        return id;
    }
    
    /**
     * 出力と教師信号の平均2乗誤差を計算する
     * @return 平均2乗誤差
     */
    public double calculateError() {
        double error = 0;
        
        for (int i=0; i<outputLayer.numNodes; i++) {
            error += Math.pow(outputLayer.neuronValues[i] - outputLayer.teacherValues[i], 2);
        }
        
        error /= outputLayer.numNodes;
        
        return error;
    }
    
    /**
     * 学習率を設定する
     * @param rate 学習率
     */
    public void setLearningRate(double rate) {
        inputLayer.learningRate = rate;
        hiddenLayer.learningRate = rate;
        outputLayer.learningRate = rate;
    }
}
