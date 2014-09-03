/*
 * Created on 2005/08/20
 *
 */

/**
 * @author mori
 *
 */
public class BarBlock extends Block {
    public BarBlock(Board board) {
        super(board);

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
