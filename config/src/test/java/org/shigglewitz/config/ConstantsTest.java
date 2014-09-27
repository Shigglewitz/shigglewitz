package org.shigglewitz.config;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.shigglewitz.config.Constants;

public class ConstantsTest {
    @Test
    public void testImageExtension() {
        assertFalse("Extension should not contain dot",
                Constants.DEFAULT_IMAGE_EXTENSION.contains("."));
    }
}
