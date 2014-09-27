package org.shigglewitz.graphing;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.shigglewitz.config.Constants;
import org.shigglewitz.testutils.ImageComparison;
import org.shigglewitz.utils.ImageMaker;
import org.junit.Test;
import org.shigglewitz.graphing.DataGrapher;
import org.shigglewitz.graphing.Grapher;
import org.shigglewitz.graphing.DataGrapher.DrawType;

public class DataGrapherTest {
    @Test
    public void testLineGraph() throws IOException {
        Point[] data = { new Point(0, 0), new Point(1, 1), new Point(2, 2),
                new Point(3, 3), new Point(3, 4), new Point(4, 4) };
        DataGrapher dg = new DataGrapher();
        dg.addPoints(data, Color.RED);
        BufferedImage experiment = dg.getGraph(Grapher.DEFAULT_WIDTH,
                Grapher.DEFAULT_HEIGHT);

        try {
            ImageComparison.compareWholeImage("data-graph1",
                    Constants.DEFAULT_IMAGE_EXTENSION, experiment);
        } catch (AssertionError e) {
            ImageMaker.saveImage(experiment, "data-graph");
            throw e;
        }
    }

    @Test
    public void testFillGraph() throws IOException {
        Point[] data = { new Point(0, 0), new Point(1, 1), new Point(2, 2),
                new Point(3, 3), new Point(3, 4), new Point(4, 4) };
        DataGrapher dg = new DataGrapher();
        dg.addPoints(data, Color.RED);
        dg.setDrawType(DrawType.FILL);
        BufferedImage experiment = dg.getGraph(Grapher.DEFAULT_WIDTH,
                Grapher.DEFAULT_HEIGHT);

        try {
            ImageComparison.compareWholeImage("data-graph2",
                    Constants.DEFAULT_IMAGE_EXTENSION, experiment);
        } catch (AssertionError e) {
            ImageMaker.saveImage(experiment, "data-graph");
            throw e;
        }
    }
}
