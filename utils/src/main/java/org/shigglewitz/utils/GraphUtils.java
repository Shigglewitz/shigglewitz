package org.shigglewitz.utils;

import java.awt.Color;
import java.awt.Graphics2D;

public class GraphUtils {
    /**
     * [0] is xaxis position, [1] is yaxis position
     * 
     * @param width
     * @param height
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @return
     */
    public static int[] calculateAxisPositions(int width, int height,
            double minX, double maxX, double minY, double maxY) {
        int[] ret = new int[2];

        int xAxisPosition = -1;
        int yAxisPosition = -1;

        if (minY == 0) {
            xAxisPosition = height - 1;
        } else if (maxY == 0) {
            xAxisPosition = 0;
        } else if (minY < 0 && maxY > 0) {
            xAxisPosition = getYpixel(height, minY, maxY, 0);
        }
        if (minX == 0) {
            yAxisPosition = 0;
        } else if (maxX == 0) {
            yAxisPosition = width - 1;
        } else if (minX < 0 && maxX > 0) {
            yAxisPosition = getXpixel(width, minX, maxX, 0);
        }

        ret[0] = xAxisPosition;
        ret[1] = yAxisPosition;

        return ret;
    }

    public static int getXpixel(int imageWidth, double minX, double maxX,
            double desiredLocation) {
        return calculateLocationOnGraph(imageWidth, minX, maxX, desiredLocation);
    }

    public static int getYpixel(int imageHeight, double minY, double maxY,
            double desiredLocation) {
        return imageHeight
                - calculateLocationOnGraph(imageHeight, minY, maxY,
                        desiredLocation);
    }

    public static int calculateLocationOnGraph(int dimensionLength,
            double minAxis, double maxAxis, double desiredLocation) {
        return (int) ((desiredLocation - minAxis) / (maxAxis - minAxis) * dimensionLength);
    }

    public static void drawInitialGraph(int width, int height, double minX,
            double maxX, double minY, double maxY, Graphics2D graphics,
            boolean drawGrid, boolean drawTicks, Color gridColor,
            Color axisColor, int unitsBetweenTicks, int pixelsPerTick) {
        Color initialColor = graphics.getColor();
        int[] axisPositions = GraphUtils.calculateAxisPositions(width, height,
                minX, maxX, minY, maxY);
        int xAxisPosition = axisPositions[0];
        int yAxisPosition = axisPositions[1];

        graphics.setColor(gridColor);
        if (drawGrid) {
            // draw grid
            GraphUtils.drawGrid(minX, maxX, minY, maxY, width, height,
                    graphics, unitsBetweenTicks);
        } else if (drawTicks) {
            // draw ticks
            GraphUtils.drawTicks(minX, maxX, minY, maxY, width, height,
                    graphics, xAxisPosition, yAxisPosition, unitsBetweenTicks,
                    pixelsPerTick);
        }

        // draw axes
        graphics.setColor(axisColor);
        GraphUtils.drawAxes(width, height, graphics, xAxisPosition,
                yAxisPosition);

        graphics.setColor(initialColor);
    }

    private static void drawAxes(int width, int height, Graphics2D graphics,
            int xAxisPosition, int yAxisPosition) {
        // x axis
        graphics.drawLine(0, xAxisPosition, width, xAxisPosition);
        // y axis
        graphics.drawLine(yAxisPosition, 0, yAxisPosition, height);
    }

    private static void drawTicks(double minX, double maxX, double minY,
            double maxY, int width, int height, Graphics2D graphics,
            int xAxisPosition, int yAxisPosition, int unitsBetweenTicks,
            int pixelsPerTick) {
        int tickPosition = -1;
        if (minY <= 0 && maxY >= 0) {
            for (int y = (int) Math.ceil(minY); y <= maxY; y += unitsBetweenTicks) {
                tickPosition = getYpixel(height, minY, maxY, y);
                graphics.drawLine(yAxisPosition - pixelsPerTick, tickPosition,
                        yAxisPosition + pixelsPerTick, tickPosition);
            }
        }
        if (minX <= 0 && maxX >= 0) {
            for (int x = (int) Math.ceil(minX); x <= maxX; x += unitsBetweenTicks) {
                tickPosition = getXpixel(width, minX, maxX, x);
                graphics.drawLine(tickPosition, xAxisPosition - pixelsPerTick,
                        tickPosition, xAxisPosition + pixelsPerTick);
            }
        }
    }

    private static void drawGrid(double minX, double maxX, double minY,
            double maxY, int width, int height, Graphics2D graphics,
            int unitsBetweenTicks) {
        int gridPosition = -1;
        for (int y = (int) Math.ceil(minY); y <= maxY; y += unitsBetweenTicks) {
            if (y != 0) {
                gridPosition = height
                        - Math.abs((int) ((y - minY) / (maxY - minY) * height));
                graphics.drawLine(0, gridPosition, width, gridPosition);
            }
        }
        for (int x = (int) Math.ceil(minX); x <= maxX; x += unitsBetweenTicks) {
            if (x != 0) {
                gridPosition = Math
                        .abs((int) ((x - minX) / (maxX - minX) * width));
                graphics.drawLine(gridPosition, 0, gridPosition, height);
            }
        }
    }
}
