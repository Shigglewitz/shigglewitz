package ork.shigglewitz.testutils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;

import org.junit.Test;
import org.shigglewitz.testutils.ImageComparison;

public class ImageComparisonTest {
    @Test(expected = WrappedAssertionException.class)
    public void testCompareWidth() throws WrappedAssertionException {
        BufferedImage large = new BufferedImage(200, 300,
                BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage small = new BufferedImage(20, 300,
                BufferedImage.TYPE_4BYTE_ABGR);
        try {
            ImageComparison.compareSize(large, small);
            assertFalse("Size comparison didn't throw expected exception.",
                    false);
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("Widths"));
            throw new WrappedAssertionException(e);
        }
    }

    @Test(expected = WrappedAssertionException.class)
    public void testCompareHeight() throws WrappedAssertionException {
        BufferedImage large = new BufferedImage(200, 300,
                BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage small = new BufferedImage(200, 30,
                BufferedImage.TYPE_4BYTE_ABGR);
        try {
            ImageComparison.compareSize(large, small);
            assertFalse("Size comparison didn't throw expected exception.",
                    false);
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("Heights"));
            throw new WrappedAssertionException(e);
        }
    }
}
