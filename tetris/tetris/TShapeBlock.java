/*
 * Created on 2005/08/20
 *
 */

/**
 * @author mori
 *
 */
public class TShapeBlock extends Block {
    public TShapeBlock(Board board) {
        super(board);

        // Å†Å†Å†Å†
        // Å†Å°Å†Å†
        // Å†Å°Å°Å†
        // Å†Å°Å†Å†
        block[1][1] = 1;
        block[2][1] = 1;
        block[2][2] = 1;
        block[3][1] = 1;
        
        imageNo = Block.T_SHAPE;
    }
}
