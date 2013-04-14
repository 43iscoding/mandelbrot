package mandelbrot.util;

public enum MappingMode {
    RGB16(0,255),
    MANDELBROT_BASE_X(-2.5, 1.5),
    MANDELBROT_BASE_Y(-2, 2),
    MANDELBROT_ZOOM_X(-0.5, -0.25),
    MANDELBROT_ZOOM_Y(-0.75, -0.5);

    private double min;
    private double max;

    private MappingMode(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    public String toString() {
        return name() + "(" + min + "," + max + ")";
    }
}
