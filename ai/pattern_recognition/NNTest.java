/*
 * Created on 2005/05/04
 *
 */

/**
 * Exclusive OR‚ÌŠwK
 * @author mori
 */
public class NNTest {
    public static void main(String[] args) {
        NeuralNetwork nn = new NeuralNetwork();
        nn.init(2, 2, 1);
        nn.setLearningRate(0.2);
        
        // ŒP—ûƒf[ƒ^‚Ìì¬
        double[][] trainingSet = new double[4][3];
        // ŒP—ûƒf[ƒ^0
        trainingSet[0][0] = 0;  // “ü—Í1
        trainingSet[0][1] = 0;  // “ü—Í2
        trainingSet[0][2] = 0;  // ‹³t
        
        // ŒP—ûƒf[ƒ^1
        trainingSet[1][0] = 0;
        trainingSet[1][1] = 1;
        trainingSet[1][2] = 1;
        
        // ŒP—ûƒf[ƒ^2
        trainingSet[2][0] = 1;
        trainingSet[2][1] = 0;
        trainingSet[2][2] = 1;
        
        // ŒP—ûƒf[ƒ^3
        trainingSet[3][0] = 1;
        trainingSet[3][1] = 1;
        trainingSet[3][2] = 0;
        
        // ŒP—ûƒf[ƒ^‚ğŠwK
        double error = 1.0;
        int count = 0;
        while ((error > 0.0001) && (count < 50000)) {
            error = 0;
            count++;
            // 4‚Â‚ÌŒP—ûƒf[ƒ^‚ğŒë·‚ª¬‚³‚­‚È‚é‚Ü‚ÅŒJ‚è•Ô‚µŠwK
            for (int i=0; i<4; i++) {
                // “ü—Í‘w‚É’l‚ğİ’è
                nn.setInput(0, trainingSet[i][0]);
                nn.setInput(1, trainingSet[i][1]);
                // ‹³tM†‚ğİ’è
                nn.setTeacherValue(0, trainingSet[i][2]);
                // ŠwKŠJn
                nn.feedForward();
                error += nn.calculateError();
                nn.backPropagate();
            }
            error /= 4.0;
            System.out.println(count + "\t" + error);
        }
        
        // ŠwKŠ®—¹
        nn.setInput(0, 0);  // “ü—Í1
        nn.setInput(1, 0);  // “ü—Í2
        nn.feedForward();   // o—Í‚ğŒvZ
        System.out.println(nn.getOutput(0));
    }
}
