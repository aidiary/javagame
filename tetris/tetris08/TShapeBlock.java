/*
 * Created on 2006/06/25
 */

public class TShapeBlock extends Block {
    public TShapeBlock(Field field) {
        super(field);

        // Å†Å†Å†Å†
        // Å†Å°Å†Å†
        // Å†Å°Å°Å†
        // Å†Å°Å†Å†
        block[1][1] = 1;
        block[2][1] = 1;
        block[2][2] = 1;
        block[3][1] = 1;
    }
}
