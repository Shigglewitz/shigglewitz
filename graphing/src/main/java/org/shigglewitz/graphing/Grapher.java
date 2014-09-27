package org.shigglewitz.graphing;

import java.awt.image.BufferedImage;

public interface Grapher {
    public static final int DEFAULT_WIDTH = 400;
    public static final int DEFAULT_HEIGHT = DEFAULT_WIDTH;

    public void draw(BufferedImage image);

    public void adjustWindow(double minX, double maxX, double minY, double maxY);

    public BufferedImage getGraph(int width, int height);
}
