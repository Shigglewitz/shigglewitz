package org.shigglewitz.utils;

import java.util.Random;

public class ColorUtils {
    private static final Random RANDOM = new Random();

    public static int getRgbAsInt(int red, int green, int blue, int alpha) {
        return ((alpha & 0xfff) << 24 | (red & 0x0ff) << 16)
                | ((green & 0x0ff) << 8) | (blue & 0x0ff);
    }

    public static int getRgbAsInt(int[] rgb) {
        return getRgbAsInt(rgb[0], rgb[1], rgb[2], rgb.length > 3 ? rgb[3]
                : 0xfff);
    }

    public static int getRgbAsInt(int red, int green, int blue) {
        return getRgbAsInt(red, green, blue, 0xfff);
    }

    /**
     * 
     * @param rgb
     * @return [0] is red, [1] is green, [2] is blue
     */
    public static int[] getRgbFromInt(int rgb) {
        int red = (rgb >> 16) & 0x0ff;
        int green = (rgb >> 8) & 0x0ff;
        int blue = (rgb) & 0x0ff;

        return new int[] { red, green, blue };
    }

    /**
     * wrapper method for random.nextInt(256);
     * 
     * @return
     */
    public static int getRandomColor() {
        return RANDOM.nextInt(256);
    }

    public static enum NormalizationStrategy {
        CRUDE, CURVE, CEILING
    }

    private static final int COLOR_NORMALIZATION_MASK = 0x100;

    public static int normalizeColor(int input) {
        return normalize(input, NormalizationStrategy.CRUDE,
                COLOR_NORMALIZATION_MASK);
    }

    public static int normalizeColor(int input, NormalizationStrategy strategy) {
        return normalize(input, strategy, COLOR_NORMALIZATION_MASK);
    }

    private static final int ALPHA_NORMALIZATION_MASK = 0x1000;

    public static int normalizeAlpha(int input) {
        return normalize(input, NormalizationStrategy.CRUDE,
                ALPHA_NORMALIZATION_MASK);
    }

    public static int normalizeAlpha(int input, NormalizationStrategy strategy) {
        return normalize(input, strategy, ALPHA_NORMALIZATION_MASK);
    }

    private static int normalize(int input, NormalizationStrategy strategy,
            int mask) {
        switch (strategy) {
        case CRUDE:
            return Math.abs(input) % mask;
        case CURVE:
            int reduce = Math.abs(input) % (2 * mask);
            if (reduce == mask) {
                reduce--;
            } else if (reduce > mask) {
                reduce = mask - (reduce % mask);
            }
            return reduce;
        case CEILING:
            if (input >= mask) {
                return mask - 1;
            } else if (input <= 0) {
                return 0;
            } else {
                return input;
            }
        default:
            return input;
        }
    }
}
