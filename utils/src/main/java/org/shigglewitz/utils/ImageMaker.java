package org.shigglewitz.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.shigglewitz.config.Constants;

public class ImageMaker {
    public static void saveImage(BufferedImage image) throws IOException {
        saveImage(image, "test-image");
    }

    public static void saveImage(BufferedImage image, String fileName)
            throws IOException {
        saveImage(image, fileName, Constants.DEFAULT_IMAGE_EXTENSION);
    }

    public static void saveImage(BufferedImage image, String fileName,
            String format) throws IOException {
        File outputfile = new File(Constants.DEFAULT_IMAGE_DIRECTORY + fileName + "."
                + format);
        ImageIO.write(image, format, outputfile);
        image.flush();
    }

    public static BufferedImage baseImage(int width, int height, Color baseColor) {
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(baseColor);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.dispose();

        return image;
    }

    public static BufferedImage baseImage(int width, int height) {
        return baseImage(width, height, Color.WHITE);
    }

    public static BufferedImage randomImage(int width, int height) {
        BufferedImage image = baseImage(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, ColorUtils.getRgbAsInt(
                        ColorUtils.getRandomColor(),
                        ColorUtils.getRandomColor(),
                        ColorUtils.getRandomColor()));
            }
        }

        return image;
    }
}
