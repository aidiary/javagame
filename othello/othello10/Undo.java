/*
 * Created on 2004/12/22
 *
 */
import java.awt.*;
/**
 * ”Õ–Ê‚ğ1è–ß‚·‚½‚ß‚Ìî•ñ‚ğ‚Ü‚Æ‚ß‚½ƒNƒ‰ƒXB
 * @author mori
 *
 */
public class Undo {
    // Î‚ğ‘Å‚ÂêŠ
    public int x;
    public int y;
    // ‚Ğ‚Á‚­‚è•Ô‚Á‚½Î‚Ì”
    public int count;
    // ‚Ğ‚Á‚­‚è•Ô‚Á‚½Î‚ÌêŠ
    public Point[] pos;
    
    public Undo(int x, int y) {
        this.x = x;
        this.y = y;
        count = 0;
        pos = new Point[64];
    }
}
