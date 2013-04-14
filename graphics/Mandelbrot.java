package mandelbrot.graphics;

import mandelbrot.math.Complex;
import mandelbrot.util.ColorMapper;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class Mandelbrot extends GraphicsWindow implements MouseListener {

    private static int iteration = 0;
    private static int MAX_ITERATION = 1000;

    private static Color[][] pixelMap;
    private static final int MIN_CDF = -1;
    private static Map<Integer, Integer> cdfMap;

    private static final int HEIGHT = 400;//600;
    private static final int WIDTH = 400;//1050;

    //selection
    private static int fromX;
    private static int fromY;
    private static int toX;
    private static int toY;

    @Override
    public void initialize() {
        addMouseListener(this);
        Dimension dimension = new Dimension(WIDTH, HEIGHT);
        setSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        pixelMap = new Color[WIDTH][HEIGHT];
        ColorMapper.registerMapping(0, MAX_ITERATION, TO_RGB);
        ColorMapper.registerGradientMapping(Color.BLACK, Color.WHITE);
        //fillData(-2.5, 1.5, -2, 2);
        fillData(-0.08122396122148443, -0.08064465926699224, -0.8807629490282812, -0.8802512534376563);
    }

    private void fillData(double fromX, double toX, double fromY, double toY) {
        ColorMapper.registerMapping(0, HEIGHT, V, fromY, toY);
        ColorMapper.registerMapping(0, WIDTH, H, fromX, toX);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                double x0 = ColorMapper.mapDouble(i, H);
                double y0 = ColorMapper.mapDouble(j, V);

                Complex complex0 = new Complex(x0, y0);
                Complex complex = new Complex(0, 0);

                iteration = 0;

                while (complex.absSqr() < 4 && iteration < MAX_ITERATION) {
                    complex = complex.times(complex).plus(complex0);
                    iteration++;
                }

                int color = ColorMapper.mapInt(iteration, TO_RGB);
                putPixelToMap(i, j, new Color(color, color, color));
            }
        }
        equalizeMap(pixelMap);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        fromX = e.getX();
        fromY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        toX = e.getX();
        toY = e.getY();
        double mappedFromX = ColorMapper.mapDouble(fromX, H);
        double mappedToX = ColorMapper.mapDouble(toX, H);
        double mappedFromY = ColorMapper.mapDouble(fromY, V);
        double mappedToY = ColorMapper.mapDouble(toY, V);
        System.out.println(mappedFromX + ", " + mappedToX + ", " + mappedFromY + ", " + mappedToY);
        fillData(mappedFromX, mappedToX, mappedFromY, mappedToY);
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void draw(Graphics graphics) {
        drawMap();

    }

    public void putPixelToMap(int x, int y, Color color) {
        pixelMap[x][y] = color;
    }

    public void equalizeMap(Color[][] map) {
        Map<Integer, Integer> histogram = new HashMap<Integer, Integer>(255);
        for (Color[] row : map) {
            for (Color color : row) {
                //fill histogram
                Integer value = histogram.get(color.getRed());
                if (value == null) {
                    value = 0;
                }
                histogram.put(color.getRed(), ++value);
            }
        }
        SortedSet<Integer> sortedKeys = new TreeSet<Integer>(histogram.keySet());
        cdfMap = new HashMap<Integer, Integer>(255);
        Iterator<Integer> iterator = sortedKeys.iterator();
        int value = iterator.next();
        cdfMap.put(MIN_CDF, histogram.get(value));
        cdfMap.put(value, histogram.get(value));
        while (iterator.hasNext()) {
            //fill CDF
            int prev = value;
            value = iterator.next();
            cdfMap.put(value, cdfMap.get(prev) + histogram.get(value));
        }
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int color = pixelMap[x][y].getRed();
                int eqColor = calculateEqualized(color);
                pixelMap[x][y] = ColorMapper.mapGradient(eqColor);
            }
        }
    }

    private int calculateEqualized(int color) {
        float numerator = cdfMap.get(color) - cdfMap.get(MIN_CDF);
        float denominator = HEIGHT * WIDTH - cdfMap.get(MIN_CDF);
        return Math.round(numerator / denominator * 255);
    }

    private void drawMap() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                putPixel(x, y, pixelMap[x][y]);
            }
        }
    }

}
