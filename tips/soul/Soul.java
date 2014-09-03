import java.awt.Color;
import java.awt.Graphics;

/*
 * Created on 2007/02/04
 */

public class Soul {
    private double x, y; // ˆÊ’u
    private double cx, cy; // ‰ñ“]‚Ì’†SÀ•W
    private double radius; // ‰ñ“]”¼Œa
    private double velocity; // Šp‘¬“xi“x/•bj
    private Color color; // ‰~‚ÌF

    public Soul(double cx, double cy, double radius, double velocity, Color color) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        this.velocity = velocity;
        this.color = color;

        x = cx + radius;
        y = cy;
    }

    public void draw(Graphics g) {
        g.setColor(color);

        g.fillOval((int) x, (int) y, 16, 16);
    }

    public void update(double t) {
        x = cx + radius * Math.cos(Math.toRadians(velocity * t));
        y = cy + radius * Math.sin(Math.toRadians(velocity * t)); // -‚É‚·‚é‚Æ‹t‰ñ“]
    }

    public void setCenter(double cx, double cy) {
        this.cx = cx;
        this.cy = cy;
    }
}
