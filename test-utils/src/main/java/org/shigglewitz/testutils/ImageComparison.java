package org.shigglewitz.testutils;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.shigglewitz.config.Constants;

public class ImageComparison {
    private static Random RANDOM = new Random();

    private static BufferedImage loadImage(String expectedImageFileName,
            String fileExtension) throws IOException {
        File file = new File("src/test/resources/images/"
                + expectedImageFileName + "." + fileExtension);
        BufferedImage control = ImageIO.read(file);
        return control;
    }

    public static void compareRandomPixels(String expectedImageFileName,
            BufferedImage actual) throws IOException {
        compareRandomPixels(expectedImageFileName, Constants.DEFAULT_IMAGE_EXTENSION,
                actual);
    }

    public static void compareRandomPixels(String expectedImageFileName,
            String fileExtension, BufferedImage actual) throws IOException {
        BufferedImage control = loadImage(expectedImageFileName, fileExtension);
        compareRandomPixels(control, actual);
    }

    public static void compareRandomPixels(String expectedImageFileName,
            String expectedImageFileExtension, BufferedImage actual,
            int numComparisons) throws IOException {
        BufferedImage control = loadImage(expectedImageFileName,
                expectedImageFileExtension);
        compareRandomPixels(control, actual, numComparisons);
    }

    public static void compareRandomPixels(BufferedImage expected,
            BufferedImage actual) {
        compareRandomPixels(expected, actual,
                expected.getHeight() + actual.getWidth());
    }

    public static void compareRandomPixels(BufferedImage expected,
            BufferedImage actual, int numComparisons) {
        compareSize(expected, actual);

        int x, y;
        for (int i = 0; i < numComparisons; i++) {
            x = RANDOM.nextInt(expected.getWidth());
            y = RANDOM.nextInt(expected.getHeight());
            assertEquals("Pixel at (" + x + ", " + y + ") was not equal!",
                    expected.getRGB(x, y), actual.getRGB(x, y));
        }
    }

    public static void compareWholeImage(String fileName, BufferedImage actual)
            throws IOException {
        compareWholeImage(fileName, Constants.DEFAULT_IMAGE_EXTENSION, actual);
    }

    public static void compareWholeImage(String fileName, String fileExtension,
            BufferedImage actual) throws IOException {
        compareWholeImage(loadImage(fileName, fileExtension), actual);
    }

    public static void compareWholeImage(BufferedImage expected,
            BufferedImage actual) {
        compareSize(expected, actual);

        int x, y;
        for (x = 0; x < expected.getWidth(); x++) {
            for (y = 0; y < expected.getHeight(); y++) {
                assertEquals("Pixel at (" + x + ", " + y + ") was not equal!",
                        expected.getRGB(x, y), actual.getRGB(x, y));
            }
        }
    }

    public static void compareSize(BufferedImage expected, BufferedImage actual) {
        assertEquals("Widths do not match!", expected.getWidth(),
                actual.getWidth());
        assertEquals("Heights do not match!", expected.getHeight(),
                actual.getHeight());
    }
}
