package org.shigglewitz.graphing;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.shigglewitz.config.Constants;
import org.shigglewitz.equations.exceptions.InvalidEquationException;
import org.shigglewitz.testutils.ImageComparison;
import org.shigglewitz.utils.ImageMaker;
import org.junit.Ignore;
import org.junit.Test;
import org.shigglewitz.graphing.ColorGrapher;
import org.shigglewitz.graphing.Grapher;

public class ColorGrapherTest {
    private static final boolean DIAGNOSTIC = false;

    @Test
    public void testGetGraph() throws InvalidEquationException, IOException {
        this.testColorGraph("X", "Y", "X+Y", "color-graph-1", false, "");
    }

    @Test
    public void testTrigonometricGraph() throws InvalidEquationException,
            IOException {
        this.testColorGraph("X^2+Y^2", "sin(Y*X)*128", "X",
                "trigonometric-graph-1", false, "");
    }

    @Test
    public void testGraphZoom() throws InvalidEquationException, IOException {
        this.testColorGraph("X^2+Y^2", "sin(Y*X)*128", "X", 2, 0, 200,
                DIAGNOSTIC, null, true, "graph-5");
    }

    @Ignore
    @Test
    public void testGraph() throws InvalidEquationException, IOException {
        this.testColorGraph("X^2+Y", "0", "0", null, true, "graph-4");
    }

    public void testColorGraph(String redEq, String greenEq, String blueEq,
            String compareFile, boolean save, String fileName)
            throws InvalidEquationException, IOException {
        this.testColorGraph(redEq, greenEq, blueEq, 1, compareFile, save,
                fileName);
    }

    public void testColorGraph(String redEq, String greenEq, String blueEq,
            int zoomFactor, String compareFile, boolean save, String fileName)
            throws InvalidEquationException, IOException {
        this.testColorGraph(redEq, greenEq, blueEq, zoomFactor, DIAGNOSTIC,
                compareFile, save, fileName);
    }

    public void testColorGraph(String redEq, String greenEq, String blueEq,
            int zoomFactor, boolean diagnostic, String compareFile,
            boolean save, String fileName) throws InvalidEquationException,
            IOException {
        this.testColorGraph(redEq, greenEq, blueEq, zoomFactor, 0, 0,
                diagnostic, compareFile, save, fileName);
    }

    public void testColorGraph(String redEq, String greenEq, String blueEq,
            int zoomFactor, int xShift, int yShift, boolean diagnostic,
            String compareFile, boolean save, String fileName)
            throws InvalidEquationException, IOException {
        ColorGrapher cg = new ColorGrapher(redEq, greenEq, blueEq);
        if (diagnostic) {
            cg.enableDiagnostics();
        }
        cg.setZoomFactor(zoomFactor);
        cg.setPixelShift(xShift, yShift);
        BufferedImage experiment = cg.getGraph(Grapher.DEFAULT_WIDTH,
                Grapher.DEFAULT_HEIGHT);
        if (compareFile != null) {
            ImageComparison.compareRandomPixels(compareFile, experiment);
        }
        if (save) {
            ImageMaker.saveImage(experiment, fileName,
                    Constants.DEFAULT_IMAGE_EXTENSION);
        }
    }
}
