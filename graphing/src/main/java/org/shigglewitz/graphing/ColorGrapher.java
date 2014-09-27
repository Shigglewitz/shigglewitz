package org.shigglewitz.graphing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.shigglewitz.equations.Equation;
import org.shigglewitz.equations.exceptions.InsufficientVariableInformationException;
import org.shigglewitz.equations.exceptions.InvalidEquationException;
import org.shigglewitz.utils.ColorUtils;
import org.shigglewitz.utils.ColorUtils.NormalizationStrategy;
import org.shigglewitz.utils.ImageMaker;

public class ColorGrapher implements Grapher {
    public static final int DEFAULT_WIDTH = 400;
    public static final int DEFAULT_HEIGHT = 400;

    private final Equation red, green, blue, alpha;
    private int[][] values;
    private double zoomFactor = 1;
    private int pixelShiftX = 0;
    private int pixelShiftY = 0;

    private ColorUtils.NormalizationStrategy strategy = NormalizationStrategy.CURVE;

    public ColorGrapher(String red, String green, String blue)
            throws InvalidEquationException {
        this(red, green, blue, null);
    }

    public ColorGrapher(String red, String green, String blue, String alpha)
            throws InvalidEquationException {
        this.red = new Equation(red);
        this.blue = new Equation(blue);
        this.green = new Equation(green);
        if (alpha == null) {
            this.alpha = null;
        } else {
            this.alpha = new Equation(alpha);
        }
    }

    public void setZoomFactor(int zoom) {
        this.zoomFactor = 1.0 / zoom;
    }

    public void setPixelShift(int x, int y) {
        this.pixelShiftX = x;
        this.pixelShiftY = y;
    }

    public void calculateValues(int width, int height)
            throws InsufficientVariableInformationException,
            InvalidEquationException {
        Map<String, BigDecimal> vars = new HashMap<>();
        this.values = new int[width][height];

        int redValue;
        int greenValue;
        int blueValue;
        int alphaValue = 0xfff;
        for (int x = 0; x < width; x++) {
            vars.put("X", new BigDecimal((x + this.pixelShiftX)
                    * this.zoomFactor));
            for (int y = 0; y < height; y++) {
                vars.put("Y", new BigDecimal((y + this.pixelShiftY)
                        * this.zoomFactor));
                redValue = ColorUtils.normalizeColor(
                        (int) this.red.evaluate(vars), this.strategy);
                greenValue = ColorUtils.normalizeColor(
                        (int) this.green.evaluate(vars), this.strategy);
                blueValue = ColorUtils.normalizeColor(
                        (int) this.blue.evaluate(vars), this.strategy);
                if (this.alpha != null) {
                    alphaValue = ColorUtils.normalizeAlpha(
                            (int) this.alpha.evaluate(vars), this.strategy);
                }

                this.values[x][y] = ColorUtils.getRgbAsInt(redValue,
                        greenValue, blueValue, alphaValue);
            }
        }
    }

    @Override
    public void draw(BufferedImage image) {
        Graphics2D graphics = image.createGraphics();

        long beforeCalc = System.currentTimeMillis();

        if (this.values == null || this.values.length != image.getWidth()
                || this.values[0].length != image.getHeight()) {
            try {
                this.calculateValues(image.getWidth(), image.getHeight());
            } catch (InvalidEquationException e) {
                e.printStackTrace();
                graphics.setColor(Color.BLACK);
                graphics.drawString(
                        "An error occurred while calculating values", 1, 10);
            }
        }

        long afterCalc = System.currentTimeMillis();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                image.setRGB(x, y, this.values[x][y]);
            }
        }

        long afterDraw = System.currentTimeMillis();

        graphics.dispose();

        if (this.diagnostics) {
            System.out.println("Took " + (afterCalc - beforeCalc)
                    + "ms to calculate.");
            System.out.println("Took " + (afterDraw - afterCalc)
                    + "ms to draw.");
        }

    }

    /**
     * maybe figure out a way to work this into it later, but for now not
     * applicable
     */
    @Override
    public void adjustWindow(double minX, double maxX, double minY, double maxY) {
        return;
    }

    @Override
    public BufferedImage getGraph(int width, int height) {
        BufferedImage image = ImageMaker.baseImage(width, height);
        this.draw(image);
        return image;
    }

    private boolean diagnostics = false;

    public void enableDiagnostics() {
        this.diagnostics = true;
    }

    public void setStrategy(ColorUtils.NormalizationStrategy strategy) {
        this.strategy = strategy;
    }

    public String getRedEquation() {
        return this.red.getOriginalEquation();
    }

    public String getGreenEquation() {
        return this.green.getOriginalEquation();
    }

    public String getBlueEquation() {
        return this.blue.getOriginalEquation();
    }
}
