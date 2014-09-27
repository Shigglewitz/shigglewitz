package org.shigglewitz.graphing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.shigglewitz.equations.Equation;
import org.shigglewitz.equations.exceptions.InsufficientVariableInformationException;
import org.shigglewitz.equations.exceptions.InvalidEquationException;
import org.shigglewitz.utils.GraphUtils;
import org.shigglewitz.utils.ImageMaker;

public class EquationGrapher implements Grapher {
    private static final double DEFAULT_MIN_X = -10.0;
    private static final double DEFAULT_MAX_X = 10.0;
    private static final double DEFAULT_MIN_Y = -10.0;
    private static final double DEFAULT_MAX_Y = 10.0;

    private double minX = 0;
    private double maxX = 0;
    private double minY = 0;
    private double maxY = 0;

    private final Equation e;
    private double[] values;

    public EquationGrapher(String equation) throws InvalidEquationException {
        this.e = new Equation(equation);
        this.values = null;
    }

    public void calculateValues(BigDecimal minRange, BigDecimal maxRange,
            BigDecimal delta, String var)
            throws InsufficientVariableInformationException,
            InvalidEquationException {
        Map<String, BigDecimal> vars = new HashMap<>();
        this.values = new double[(int) Math.floor(maxRange.subtract(minRange)
                .divide(delta, RoundingMode.CEILING).add(new BigDecimal(1))
                .doubleValue())];

        int i = 0;
        for (BigDecimal eval = minRange; eval.compareTo(maxRange) < 0; eval = eval
                .add(delta), i++) {
            vars.put(var, eval);
            this.values[i] = this.e.evaluate(vars);
        }
    }

    public double[] getValues() {
        return this.values;
    }

    @Override
    public BufferedImage getGraph(int width, int height) {
        if (this.minX == 0 && this.maxX == 0 && this.minY == 0
                && this.maxY == 0) {
            this.adjustWindow(DEFAULT_MIN_X, DEFAULT_MAX_X, DEFAULT_MIN_Y,
                    DEFAULT_MAX_Y);
        }
        BufferedImage image = ImageMaker.baseImage(width, height,
                this.graphBackground);
        this.draw(image);
        return image;
    }

    private Color graphBackground = Color.WHITE;
    private Color axisColor = Color.BLACK;
    private Color gridColor = new Color(200, 200, 200);
    private Color graphColor = Color.GREEN;

    private boolean drawGrid = false;
    private boolean drawTicks = true;

    private int pixelsPerTick = 3;
    private int unitsBetweenTicks = 1;

    private int extraGraphPadding = 0;

    @Override
    public void draw(BufferedImage image) {
        Graphics2D graphics = image.createGraphics();
        GraphUtils.drawInitialGraph(image.getWidth(), image.getHeight(),
                this.minX, this.maxX, this.minY, this.maxY, graphics,
                this.drawGrid, this.drawTicks, this.gridColor, this.axisColor,
                this.unitsBetweenTicks, this.pixelsPerTick);

        // calculate values
        if (this.values == null) {
            try {
                this.calculateValues(new BigDecimal(this.minX), new BigDecimal(
                        this.maxX), new BigDecimal((this.maxX - this.minX)
                        / image.getWidth()), "X");
            } catch (InvalidEquationException
                    | InsufficientVariableInformationException e) {
                e.printStackTrace();
                graphics.drawString("Exception calculating values", 1, 10);
            }
        }
        // draw graphs
        int y = GraphUtils.getYpixel(image.getHeight(), this.minY, this.maxY,
                this.values[0]);
        int pastY = y;
        int pastX = 0;
        int temp = -1;
        graphics.setColor(this.graphColor);
        for (int x = 0; x < image.getWidth(); x++) {
            y = GraphUtils.getYpixel(image.getHeight(), this.minY, this.maxY,
                    this.values[x]);
            if ((y >= 0 && y < image.getHeight())
                    || (pastY >= 0 && pastY < image.getHeight())) {
                temp = y;
                if (y < 0) {
                    y = 0;
                }
                if (y >= image.getHeight()) {
                    y = image.getHeight() - 1;
                }
                if (pastY < 0) {
                    pastY = 0;
                }
                if (pastY >= image.getHeight()) {
                    pastY = image.getHeight() - 1;
                }
                graphics.drawLine(pastX, pastY, x, y);
                for (int i = 0; i < this.extraGraphPadding; i++) {
                    if (pastX > 0) {
                        graphics.drawLine(pastX - 1, pastY, x - 1, y);
                    }
                    if (x < image.getWidth() - 1) {
                        graphics.drawLine(pastX + 1, pastY, x + 1, y);
                    }
                }

                y = temp;
            }
            pastY = y;
            pastX = x;
        }

        graphics.dispose();

    }

    @Override
    public void adjustWindow(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public void setGraphBackground(Color graphBackground) {
        this.graphBackground = graphBackground;
    }

    public void setAxisColor(Color axisColor) {
        this.axisColor = axisColor;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    public void setGraphColor(Color graphColor) {
        this.graphColor = graphColor;
    }

    public void setDrawGrid(boolean drawGrid) {
        this.drawGrid = drawGrid;
    }

    public void setDrawTicks(boolean drawTicks) {
        this.drawTicks = drawTicks;
    }

    public void setPixelsPerTick(int pixelsPerTick) {
        this.pixelsPerTick = pixelsPerTick;
    }

    public void setUnitsBetweenTicks(int unitsBetweenTicks) {
        this.unitsBetweenTicks = unitsBetweenTicks;
    }

    public void setExtraGraphPadding(int extraGraphPadding) {
        this.extraGraphPadding = extraGraphPadding;
    }
}
