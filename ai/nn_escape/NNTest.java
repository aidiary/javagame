/*
 * Created on 2005/05/04
 *
 */

/**
 * •¶š”F¯
 * @author mori
 */
public class NNTest {
    public static void main(String[] args) {
        NeuralNetwork nn = new NeuralNetwork();
        nn.init(9, 50, 9);
        nn.setLearningRate(0.2);
        
        // ŒP—ûƒf[ƒ^‚Ìì¬
        double[][] trainingSet = new double[][] {
                {1,0,0,0,0,0,0,0},
                {0,1,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,0},
                {0,0,0,1,0,0,0,0},
                {0,0,0,0,1,0,0,0},
                {0,0,0,0,0,1,0,0},
                {0,0,0,0,0,0,1,0},
                {0,0,0,0,0,0,0,1},
        };
        
        // ‹³tM†
        double[][] teacherSet = new double[][] {
                {0,0,0,0,0,0,0,1},
                {0,0,0,0,0,0,1,0},
                {0,0,0,0,0,1,0,0},
                {0,0,0,0,1,0,0,0},
                {0,0,0,1,0,0,0,0},
                {0,0,1,0,0,0,0,0},
                {0,1,0,0,0,0,0,0},
                {1,0,0,0,0,0,0,0},
        };
            
        // ŒP—ûƒf[ƒ^‚ğŠwK
        double error = 1.0;
        int count = 0;
        while ((error > 0.0001) && (count < 50000)) {
            error = 0;
            count++;
            // ŠeŒP—ûƒf[ƒ^‚ğŒë·‚ª¬‚³‚­‚È‚é‚Ü‚ÅŒJ‚è•Ô‚µŠwK
            for (int i=0; i<trainingSet.length; i++) {
                // “ü—Í’l‚ğİ’è
                for (int j=0; j<trainingSet[i].length; j++) {
                    nn.setInput(j, trainingSet[i][j]);
                }
                // ‹³tM†‚ğİ’è
                for (int j=0; j<teacherSet[i].length; j++) {
                    nn.setTeacherValue(j, teacherSet[i][j]);
                }
                // ŠwKŠJn
                nn.feedForward();
                error += nn.calculateError();
                nn.backPropagate();
            }
            error /= trainingSet.length;
            System.out.println(count + "\t" + error);
        }
        
        // ŠwKŠ®—¹
        nn.setInput(0, 1);
        nn.setInput(1, 0);
        nn.setInput(2, 0);
        nn.setInput(3, 0);
        nn.setInput(4, 0);
        nn.setInput(5, 0);
        nn.setInput(6, 0);
        nn.setInput(7, 0);
        nn.feedForward();   // o—Í‚ğŒvZ
        int id = nn.getMaxOutputID();
        System.out.println(id + " " + nn.getOutput(id));
    }
}
