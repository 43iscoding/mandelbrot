package mandelbrot.graphics;

import mandelbrot.util.ColorMapper;

import java.awt.*;

public class Gradient extends GraphicsWindow {

    @Override
    public void draw(Graphics graphics) {
        ColorMapper.registerMapping(0, getHeight(), V);
        ColorMapper.registerMapping(0, getWidth(), H);
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                int g = ColorMapper.mapInt(x, H);
                int b = ColorMapper.mapInt(y, V);
                int r = ColorMapper.mapInt(x + y, V);
                putPixel(x, y, new Color(r, g, b));
            }
        }
    }

    @Override
    public void initialize() {
        setSize(800, 600);
    }
}
