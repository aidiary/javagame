/*
 * Created on 2006/07/08
 */

public class BarBlock extends Block {
    public BarBlock(Field field) {
        super(field);

        // Å†Å°Å†Å†
        // Å†Å°Å†Å†
        // Å†Å°Å†Å†
        // Å†Å°Å†Å†
        block[0][1] = 1;
        block[1][1] = 1;
        block[2][1] = 1;
        block[3][1] = 1;

        imageNo = Block.BAR;
    }
}
