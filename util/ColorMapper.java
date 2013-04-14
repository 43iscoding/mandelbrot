package mandelbrot.util;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorMapper {

    private static final String DEFAULT = "DEFAULT";
    private static final MappingMode defaultMappingMode = MappingMode.RGB16;

    private static final Map<String, Mapping> map = new HashMap<String, Mapping>();
    private static final Map<String, GradientMapping> gradientMap = new HashMap<String, GradientMapping>();

    private static String PROFILE = DEFAULT;
    private static String GRADIENT_PROFILE = DEFAULT;

    private static class GradientMapping {

        private Color from;
        private Color to;

        private GradientMapping(Color from, Color to) {
            this.from = from;
            this.to = to;
        }

        private Color map(int value) {
            if (value <= 0) {
                return from;
            }
            if (value >= 255) {
                return to;
            }
            double ratio = (value) / 255d;
            int red = (int) Math.round(from.getRed() + ratio * (to.getRed() - from.getRed()));
            int blue = (int) Math.round(from.getBlue() + ratio * (to.getBlue() - from.getBlue()));
            int green = (int) Math.round(from.getGreen() + ratio * (to.getGreen() - from.getGreen()));
            return new Color(red, blue, green);
        }
    }

    private static class Mapping {

        private double min;
        private double max;
        private double from;
        private double to;

        private Mapping(double min, double max, double from, double to) {
            this.min = min;
            this.max = max;
            this.from = from;
            this.to = to;
        }

        private double map(double value) {
            if (value < min) {
                return from;
            }
            if (value > max) {
                return to;
            }
            double ratio = (value - min) / (max - min);
            return from + ratio * (to - from);
        }

    }

    public static void registerMapping(double min, double max, String mapName) {
        registerMapping(min, max, mapName, defaultMappingMode);
    }

    public static void registerMapping(double min, double max, String mapName, MappingMode mode) {
        registerMapping(min, max, mapName, mode.getMin(), mode.getMax());
    }

    public static void registerMapping(double min, double max, String mapName, double from, double to) {
        map.put(mapName, new Mapping(min, max, from, to));
        switchProfile(mapName);
    }

    public static void registerGradientMapping(Color from, Color to) {
        registerGradientMapping(from, to, GRADIENT_PROFILE);
    }

    public static void registerGradientMapping(Color from, Color to, String mapName) {
        gradientMap.put(mapName, new GradientMapping(from, to));
        switchGradientProfile(mapName);
    }

    public static void registerMapping(double min, double max) {
        registerMapping(min, max, PROFILE);
    }

    public static double mapDouble(double value, String mapName) {
        Mapping mapping = map.get(mapName);
        if (mapping == null) {
            return 0;
        }
        return mapping.map(value);
    }

    public static void switchProfile(String profile) {
        Mapping mapping = map.get(profile);
        if (mapping != null) {
            PROFILE = profile;
        }
    }

    public static void switchGradientProfile(String profile) {
        GradientMapping mapping = gradientMap.get(profile);
        if (mapping != null) {
            GRADIENT_PROFILE = profile;
        }
    }

    public static double mapDouble(double value) {
        return mapDouble(value, PROFILE);
    }

    public static float mapFloat(double value, String mapName) {
        return (float) mapDouble(value, mapName);
    }

    public static float mapFloat(double value) {
        return mapFloat(value, PROFILE);
    }

    public static int mapInt(double value, String mapName) {
        return Math.round(mapFloat(value, mapName));
    }

    public static int mapInt(double value) {
        return mapInt(value, PROFILE);
    }

    public static Color mapGradient(int value) {
        return mapGradient(value, GRADIENT_PROFILE);
    }

    public static Color mapGradient(int value, String profile) {
        GradientMapping mapping = gradientMap.get(profile);
        if (mapping == null) {
            return new Color(value, value, value);
        }
        return mapping.map(value);
    }
}
