/*
 * Created on 2006/06/25
 */

public class LShapeBlock extends Block {
    public LShapeBlock(Field field) {
        super(field);

        // Å†Å†Å†Å†
        // Å†Å°Å°Å†
        // Å†Å†Å°Å†
        // Å†Å†Å°Å†
        block[1][1] = 1;
        block[1][2] = 1;
        block[2][2] = 1;
        block[3][2] = 1;
    }
}
