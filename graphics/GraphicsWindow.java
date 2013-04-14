package mandelbrot.graphics;

import java.applet.Applet;
import java.awt.*;

public abstract class GraphicsWindow extends Applet {



    @Override
    public void init() {
        initialize();
    }

    public abstract void initialize();

    protected static String V = "Vertical";
    protected static String H = "Horizontal";
    protected static String TO_RGB = "ToRGB";

    @Override
    public void paint(Graphics graphics) {
        draw(graphics);
    }

    public abstract void draw(Graphics graphics);

    public void putPixel(int x, int y, Color color) {
        Graphics g = getGraphics();
        g.setColor(color);
        g.fillRect(x, y, 1, 1);
    }
}
