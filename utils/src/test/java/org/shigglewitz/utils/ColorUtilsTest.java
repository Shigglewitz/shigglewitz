package org.shigglewitz.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Random;

import org.junit.Test;
import org.shigglewitz.utils.ColorUtils;
import org.shigglewitz.utils.ColorUtils.NormalizationStrategy;

public class ColorUtilsTest {
    private static final Random RANDOM = new Random();

    @Test
    public void testGetRgbAsInt() {
        int red = 0;
        int blue = 0;
        int green = 0;

        int numTests = 15;

        Random random = new Random();

        for (int i = 0; i < numTests; i++) {
            red = random.nextInt(256);
            blue = random.nextInt(256);
            green = random.nextInt(256);
            assertEquals(new Color(red, green, blue).getRGB(),
                    ColorUtils.getRgbAsInt(red, green, blue));
        }
    }

    @Test
    public void testGetRgbAsIntWithAlpha() {
        int red = 0;
        int blue = 0;
        int green = 0;
        int alpha = 0;

        int numTests = 15;

        Random random = new Random();

        for (int i = 0; i < numTests; i++) {
            red = random.nextInt(256);
            blue = random.nextInt(256);
            green = random.nextInt(256);
            alpha = random.nextInt(256);
            assertEquals(new Color(red, green, blue, alpha).getRGB(),
                    ColorUtils.getRgbAsInt(red, green, blue, alpha));
        }
    }

    @Test
    public void testNormalizeColorCrude() {
        int[] tests = { -1, 256, 34, 257 };
        int[] expected = { 1, 0, 34, 1 };
        int numRandomTests = 20;

        for (int i = 0; i < tests.length; i++) {
            assertEquals(expected[i], ColorUtils.normalizeColor(tests[i],
                    NormalizationStrategy.CRUDE));
        }

        for (int i = 0; i < numRandomTests; i++) {
            int toTrim = RANDOM.nextInt();
            int trimmed = ColorUtils.normalizeColor(toTrim);
            assertTrue(toTrim + " was trimmed to " + trimmed
                    + " instead of 0-255", trimmed >= 0 && trimmed <= 0xfff);
        }
    }

    @Test
    public void testNormalizeColorCurve() {
        int pastValue = -1;
        int normalized = 0;

        for (int test = 0; test < 0x222; test++) {
            normalized = ColorUtils.normalizeColor(test,
                    NormalizationStrategy.CURVE);
            assertTrue("Too much difference for " + test,
                    Math.abs(pastValue - normalized) < 2);
            pastValue = normalized;
        }
    }

    @Test
    public void testNormalizeColorCeiling() {
        int[] tests = { -5, 300, 256, -1, 0, 255 };
        int[] expected = { 0, 255, 255, 0, 0, 255 };

        int numRandomTests = 20;

        for (int i = 0; i < tests.length; i++) {
            assertEquals(expected[i], ColorUtils.normalizeColor(tests[i],
                    NormalizationStrategy.CEILING));
        }

        for (int i = 0; i < numRandomTests; i++) {
            int toTrim = RANDOM.nextInt();
            int trimmed = ColorUtils.normalizeColor(toTrim,
                    NormalizationStrategy.CEILING);
            assertTrue(toTrim + " was trimmed to " + trimmed
                    + " instead of 0-255", trimmed >= 0 && trimmed <= 0xfff);
        }
    }
}
