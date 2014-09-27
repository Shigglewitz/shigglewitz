package org.shigglewitz.equations;

import static org.junit.Assert.assertEquals;

import org.shigglewitz.utils.Utils;
import org.junit.Test;
import org.shigglewitz.equations.operations.Operation;
import org.shigglewitz.equations.operations.trigonometry.TrigonometricOperation;

public class OperationTest {
    @Test
    public void testOperationDelimiterSplit() {
        String input = "3.0*4.5/7.6";
        String delimiter = Operation.OPERATOR_REGEX;
        String[] split = Utils.splitWithDelimiter(input, delimiter);
        assertEquals("Incorrect number of strings found", 5, split.length);
    }

    @Test
    public void testTrigonometricOperationDelimiterSplit() {
        String input = "1+sin(x)+tan(x)*cos(x)+asdf";
        String delimiter = TrigonometricOperation.OPERATOR_REGEX;
        String[] split = Utils.splitWithDelimiter(input, delimiter);
        assertEquals("Incorrect number of strings found", 7, split.length);
    }
}
