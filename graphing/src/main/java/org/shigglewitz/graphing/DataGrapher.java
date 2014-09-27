package org.shigglewitz.graphing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.shigglewitz.utils.GraphUtils;
import org.shigglewitz.utils.ImageMaker;

public class DataGrapher implements Grapher {

    private static final Color DEFAULT_GRAPH_COLOR = Color.BLACK;

    private final List<List<Point>> data;
    private final List<Color> graphColors;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private Color graphBackground = Color.WHITE;
    private Color gridColor = Color.GRAY;
    private final Color axisColor = Color.BLACK;
    private final boolean drawGrid = false;
    private final int pixelsPerTick = 3;
    private final int unitsBetweenTicks = 1;
    private final boolean drawTicks = true;

    private DrawType drawType = DrawType.LINE;

    public enum DrawType {
        LINE, FILL
    }

    public DataGrapher() {
        this.data = new ArrayList<List<Point>>();
        this.graphColors = new ArrayList<>();
    }

    public void addPoints(List<Point> points) {
        this.addPoints(points.toArray(new Point[points.size()]));
    }

    public void addPoints(List<Point> points, Color color) {
        this.addPoints(points.toArray(new Point[points.size()]), color);
    }

    public void addPoints(Point[] points) {
        this.addPoints(points, DEFAULT_GRAPH_COLOR);
    }

    public void addPoints(Point[] points, Color color) {
        List<Point> pointList = new ArrayList<>();
        synchronized (this.data) {
            if (this.data.size() == 0) {
                this.minX = points[0].getX();
                this.maxX = points[0].getX();
                this.minY = points[0].getY();
                this.maxY = points[0].getY();
            }
        }

        for (Point p : points) {
            this.addPoint(p, pointList);
        }

        Collections.sort(pointList, new PointCompare());
        this.data.add(pointList);
        this.graphColors.add(color);
    }

    private void addPoint(Point p, List<Point> points) {
        if (p.getX() > this.maxX) {
            this.maxX = p.getX();
        }
        if (p.getX() < this.minX) {
            this.minX = p.getX();
        }
        if (p.getY() > this.maxY) {
            this.maxY = p.getY();
        }
        if (p.getY() < this.minY) {
            this.minY = p.getY();
        }
        points.add(p);
    }

    @Override
    public void adjustWindow(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public BufferedImage getGraph(int width, int height) {
        BufferedImage image = ImageMaker.baseImage(width, height,
                this.graphBackground);
        this.draw(image);
        return image;
    }

    @Override
    public void draw(BufferedImage image) {
        Graphics2D graphics = image.createGraphics();
        GraphUtils.drawInitialGraph(image.getWidth(), image.getHeight(),
                this.minX, this.maxX, this.minY, this.maxY, graphics,
                this.drawGrid, this.drawTicks, this.gridColor, this.axisColor,
                this.unitsBetweenTicks, this.pixelsPerTick);

        // draw graphs
        Point p;
        List<Point> l;
        int[] polyX = new int[4];
        int[] polyY = new int[4];
        for (int i = 0; i < this.data.size(); i++) {
            l = this.data.get(i);
            p = l.get(0);
            graphics.setColor(this.graphColors.get(i));
            for (int j = 1; j < l.size(); j++) {
                switch (this.drawType) {
                case LINE:
                    graphics.drawLine(GraphUtils.getXpixel(image.getWidth(),
                            this.minX, this.maxX, p.getX()), GraphUtils
                            .getYpixel(image.getHeight(), this.minY, this.maxY,
                                    p.getY()),
                            GraphUtils.getXpixel(image.getWidth(), this.minX,
                                    this.maxX, l.get(j).getX()), GraphUtils
                                    .getYpixel(image.getHeight(), this.minY,
                                            this.maxY, l.get(j).getY()));
                    break;
                case FILL:
                    polyX[0] = GraphUtils.getXpixel(image.getWidth(),
                            this.minX, this.maxX, p.getX());
                    polyX[1] = GraphUtils.getXpixel(image.getWidth(),
                            this.minX, this.maxX, l.get(j).getX());
                    polyX[2] = polyX[1];
                    polyX[3] = polyX[0];
                    polyY[0] = GraphUtils.getYpixel(image.getHeight(),
                            this.minY, this.maxY, p.getY());
                    polyY[1] = GraphUtils.getYpixel(image.getHeight(),
                            this.minY, this.maxY, l.get(j).getY());
                    polyY[2] = image.getHeight() - 1;
                    polyY[3] = image.getHeight() - 1;
                    graphics.fillPolygon(polyX, polyY, 4);
                    break;
                default:
                    break;
                }
                p = l.get(j);
            }
        }

        graphics.dispose();
    }

    public void setGraphBackground(Color graphBackground) {
        this.graphBackground = graphBackground;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    private class PointCompare implements Comparator<Point> {
        @Override
        public int compare(Point a, Point b) {
            if (a.x < b.x) {
                return -1;
            } else if (a.x > b.x) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public void setDrawType(DrawType drawType) {
        this.drawType = drawType;
    }
}
