package org.shigglewitz.graphing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.shigglewitz.utils.ColorUtils;
import org.shigglewitz.utils.ImageMaker;

public class LightningGrapher implements Grapher {

    private Color backgroundColor = Color.BLACK;
    private Color lightningColor = Color.WHITE;
    private int decaySpeed = 1;
    private long seed = 0;

    private final List<Line2D> lines;

    public LightningGrapher() {
        this.lines = new ArrayList<>();
    }

    private void generateLines(int width, int height) {
        this.generateLines(null, width, height);
    }

    private void generateLines(Long seed, int width, int height) {
        Random random = null;
        if (seed != null) {
            random = new Random(seed.longValue());
        } else {
            random = new Random();
        }

        boolean horizontal = random.nextBoolean();
        int numDiversions = random.nextInt(3) + 1;
        int diversionBase = 0;
        Point2D endPoint = null;
        Point2D startPoint = null;

        if (horizontal) {
            endPoint = new Point(width - 1, random.nextInt(height));
            startPoint = new Point(0, random.nextInt(height));
            for (int i = 0; i < numDiversions; i++) {
                this.lines.add(this.percentOfLine((int) startPoint.getX(),
                        (int) startPoint.getY(), (int) endPoint.getX(),
                        (int) endPoint.getY(),
                        this.percentInRange(i + 2, numDiversions + 2, random)));
                startPoint = this.lines.get(this.lines.size() - 1).getP2();
                if (height - startPoint.getY() > height / 2) {
                    diversionBase = height - 1;
                } else {
                    diversionBase = 0;
                }
                this.lines.add(this.percentOfLine((int) startPoint.getX(),
                        (int) startPoint.getY(),
                        random.nextInt((int) startPoint.getX()), diversionBase,
                        this.percentInRange(1, 4, random)));
                startPoint = this.lines.get(this.lines.size() - 1).getP2();
            }
            this.lines.add(new Line2D.Double((int) startPoint.getX(),
                    (int) startPoint.getY(), (int) endPoint.getX(),
                    (int) endPoint.getY()));
        } else {
            endPoint = new Point(random.nextInt(width), height - 1);
            startPoint = new Point(random.nextInt(width), 0);
            this.lines.add(this.percentOfLine((int) startPoint.getX(),
                    (int) startPoint.getY(), (int) endPoint.getX(),
                    (int) endPoint.getY(), 1));
        }
    }

    private Line2D.Double percentOfLine(int x1, int y1, int x2, int y2,
            double percent) {
        return new Line2D.Double(x1, y1, x1 + ((x2 - x1) * percent), y1
                + ((y2 - y1) * percent));
    }

    private double percentInRange(int partition, int numPartitions,
            Random random) {
        int sizeOfPartition = 100 / numPartitions;
        return (random.nextInt(sizeOfPartition) + ((partition - 1) * sizeOfPartition)) / 100.0;
    }

    @Override
    public void draw(BufferedImage image) {
        Graphics2D graphics = image.createGraphics();

        if (this.seed != 0) {
            this.generateLines(this.seed, image.getWidth(), image.getHeight());
        } else {
            this.generateLines(image.getWidth(), image.getHeight());
        }

        graphics.setColor(this.lightningColor);
        for (Line2D line : this.lines) {
            // draw the line
            System.out.println("(" + (int) line.getX1() + ", "
                    + (int) line.getY1() + ") - (" + (int) line.getX2() + ", "
                    + (int) line.getY2() + ")");
            graphics.drawLine((int) line.getX1(), (int) line.getY1(),
                    (int) line.getX2(), (int) line.getY2());

            // add shading
            this.drawShading(image, line);
        }

        graphics.dispose();
    }

    private void drawShading(BufferedImage image, Line2D line) {
        int[] brightestRgb = ColorUtils.getRgbFromInt(this.lightningColor
                .getRGB());
        double slopeCalc = line.getX2() - line.getX1();
        boolean horizontal = false;
        boolean increasing = false;
        int startPoint = 0;
        int pixelsAdjusted = 0;
        if (slopeCalc == 0) {
            horizontal = false;
        } else {
            slopeCalc = (line.getY2() - line.getY1()) / slopeCalc;
            if (slopeCalc >= -1 && slopeCalc <= 1) {
                horizontal = true;
            } else {
                horizontal = false;
            }
        }
        System.out.println(horizontal);
        for (int i = 0; i < 255 / this.decaySpeed; i++) {
            for (int j = 0; j < brightestRgb.length; j++) {
                brightestRgb[j] = Math
                        .max(brightestRgb[j] - this.decaySpeed, 0);
            }
            if (horizontal) {
                int x = (int) line.getX1();
                increasing = line.getX2() > line.getX1();
                while ((increasing && x < line.getX2())
                        || (!increasing && x > line.getX2())) {
                    startPoint = (int) (line.getY1() + (x - line.getX1())
                            * slopeCalc);
                    if (startPoint + i < image.getHeight()) {
                        image.setRGB(
                                x,
                                startPoint + i,
                                brightenPixel(image.getRGB(x, startPoint + i),
                                        brightestRgb));
                    }
                    if (startPoint - i >= 0) {
                        image.setRGB(
                                x,
                                startPoint - i,
                                brightenPixel(image.getRGB(x, startPoint - i),
                                        brightestRgb));
                    }
                    pixelsAdjusted += 2;
                    if (increasing) {
                        x++;
                    } else {
                        x--;
                    }
                }
            } else {
                int y = (int) line.getY1();
                increasing = line.getY2() > line.getY1();
                while ((increasing && y < line.getY2())
                        || (!increasing && y > line.getY2())) {
                    startPoint = (int) (line.getX1() + (y - line.getY1())
                            * (1 / slopeCalc));
                    if (startPoint + i < image.getWidth()) {
                        image.setRGB(
                                startPoint + i,
                                y,
                                brightenPixel(image.getRGB(startPoint + i, y),
                                        brightestRgb));
                    }
                    if (startPoint - i > 0) {
                        image.setRGB(
                                startPoint - i,
                                y,
                                brightenPixel(image.getRGB(startPoint - i, y),
                                        brightestRgb));
                    }
                    pixelsAdjusted += 2;
                    if (increasing) {
                        y++;
                    } else {
                        y--;
                    }
                }
            }

        }
        System.out.println("Adjusted " + pixelsAdjusted + " pixels!");
    }

    private static int adjustPixel(int initialValue, int[] maximumAdjustment,
            boolean brighten) {
        int[] rgb = ColorUtils.getRgbFromInt(initialValue);

        for (int i = 0; i < rgb.length; i++) {
            if (brighten) {
                rgb[i] = Math.max(rgb[i], maximumAdjustment[i]);
            } else {
                rgb[i] = Math.min(rgb[i], maximumAdjustment[i]);
            }
        }

        return ColorUtils.getRgbAsInt(rgb);
    }

    public static int darkenPixel(int initialValue, int[] darkestPossible) {
        return adjustPixel(initialValue, darkestPossible, false);
    }

    public static int brightenPixel(int initialValue, int[] brightestPossible) {
        return adjustPixel(initialValue, brightestPossible, true);
    }

    @Override
    public void adjustWindow(double minX, double maxX, double minY, double maxY) {
        return;
    }

    @Override
    public BufferedImage getGraph(int width, int height) {
        BufferedImage image = ImageMaker.baseImage(width, height,
                this.backgroundColor);
        this.draw(image);
        return image;
    }

    public int getDecaySpeed() {
        return this.decaySpeed;
    }

    public void setDecaySpeed(int decaySpeed) {
        this.decaySpeed = decaySpeed;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getLightningColor() {
        return this.lightningColor;
    }

    public void setLightningColor(Color lightningColor) {
        this.lightningColor = lightningColor;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

}
