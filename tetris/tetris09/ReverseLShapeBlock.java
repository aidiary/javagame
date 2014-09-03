/*
 * Created on 2006/07/08
 */

public class ReverseLShapeBlock extends Block {
    public ReverseLShapeBlock(Field field) {
        super(field);

        // Å†Å†Å†Å†
        // Å†Å°Å°Å†
        // Å†Å°Å†Å†
        // Å†Å°Å†Å†
        block[1][1] = 1;
        block[1][2] = 1;
        block[2][1] = 1;
        block[3][1] = 1;

        imageNo = Block.REVERSE_L_SHAPE;
    }
}
