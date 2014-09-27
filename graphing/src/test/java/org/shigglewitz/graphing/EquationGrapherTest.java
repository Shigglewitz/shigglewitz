package org.shigglewitz.graphing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.AfterClass;
import org.junit.Test;
import org.shigglewitz.config.Constants;
import org.shigglewitz.equations.exceptions.InsufficientVariableInformationException;
import org.shigglewitz.equations.exceptions.InvalidEquationException;
import org.shigglewitz.testutils.ImageComparison;
import org.shigglewitz.testutils.TestConstants;
import org.shigglewitz.utils.Utils;

public class EquationGrapherTest {
    private static final double DELTA = 0.0000001;

    @AfterClass
    public static void cleanUp() {
        Utils.cleanDirectory(TestConstants.DEFAULT_IMAGE_DIRECTORY);
    }

    @Test
    public void testCalculateValues() throws InvalidEquationException,
            InsufficientVariableInformationException {
        EquationGrapher g = new EquationGrapher("X^2");
        assertNull(g.getValues());
        g.calculateValues(new BigDecimal(-2), new BigDecimal(2.005),
                new BigDecimal(0.01), "X");
        assertNotNull(g.getValues());
        assertEquals("Calculated the wrong number of values", 401,
                g.getValues().length);
        assertEquals(0, g.getValues()[200], DELTA);
        assertEquals(4, g.getValues()[0], DELTA);
        assertEquals(4, g.getValues()[400], DELTA);
        assertEquals(3.0625, g.getValues()[375], DELTA);
    }

    @Test
    public void testGetSimpleGraph() throws InvalidEquationException,
            IOException {
        EquationGrapher g = new EquationGrapher("X^2");
        BufferedImage experiment = g.getGraph(Grapher.DEFAULT_WIDTH,
                Grapher.DEFAULT_HEIGHT);
        ImageComparison.compareWholeImage("graph1",
                Constants.DEFAULT_IMAGE_EXTENSION, experiment);
    }

    @Test
    public void testGetGraphWithOptions() throws InvalidEquationException,
            IOException {
        EquationGrapher g = new EquationGrapher("-(X^2)");
        g.setGraphBackground(Color.CYAN);
        g.setAxisColor(Color.DARK_GRAY);
        g.setGridColor(Color.WHITE);
        g.setGraphColor(Color.RED);
        g.setExtraGraphPadding(1);
        g.setDrawGrid(true);
        g.adjustWindow(-1, 10, -5, 2);
        BufferedImage experiment = g.getGraph(Grapher.DEFAULT_WIDTH,
                Grapher.DEFAULT_HEIGHT);
        ImageComparison.compareWholeImage("graph2", experiment);
    }
}
