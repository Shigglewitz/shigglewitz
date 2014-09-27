package org.shigglewitz.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.shigglewitz.testutils.TestConstants;

public class ImageMakerTest {
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = DEFAULT_WIDTH;

    @BeforeClass
    public static void beforeClass() {
        cleanUp();
    }

    @AfterClass
    public static void afterClass() {
        cleanUp();
    }

    public static void cleanUp() {
        Utils.cleanDirectory(TestConstants.DEFAULT_IMAGE_DIRECTORY);
    }

    @Test
    public void testBaseImage() {
        BufferedImage image = ImageMaker.baseImage(DEFAULT_WIDTH,
                DEFAULT_HEIGHT);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                assertEquals(Color.WHITE.getRGB(), image.getRGB(x, y));
            }
        }
    }

    @Test
    public void testBaseImageWithColor() {
        int red = ColorUtils.getRandomColor();
        int green = ColorUtils.getRandomColor();
        int blue = ColorUtils.getRandomColor();

        BufferedImage image = ImageMaker.baseImage(DEFAULT_WIDTH,
                DEFAULT_HEIGHT, new Color(red, green, blue));
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                assertEquals(ColorUtils.getRgbAsInt(red, green, blue),
                        image.getRGB(x, y));
            }
        }
    }

    @Test
    public void testSaveImage() throws IOException {
        Random random = new Random();
        Color randomColor = new Color(ColorUtils.getRandomColor(),
                ColorUtils.getRandomColor(), ColorUtils.getRandomColor());
        BufferedImage image = ImageMaker.baseImage(
                random.nextInt(DEFAULT_WIDTH) + 50,
                random.nextInt(DEFAULT_HEIGHT) + 50, randomColor);
        String fileName = RandomStringUtils.randomAlphabetic(15);
        ImageMaker.saveImage(image, fileName, "png");

        File file = new File(TestConstants.DEFAULT_IMAGE_DIRECTORY + fileName
                + ".png");
        assertTrue("File was not created", file.exists());
        BufferedImage testImage = ImageIO.read(file);
        assertNotNull(
                "Could not load image to test from file " + file.getPath(),
                testImage);
        assertEquals("Widths do not match!", image.getWidth(),
                testImage.getWidth());
        assertEquals("Heights do not match!", image.getHeight(),
                testImage.getHeight());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                assertEquals("Pixel (" + x + ", " + y + ") did not match!",
                        image.getRGB(x, y), testImage.getRGB(x, y));
            }
        }

    }
}
