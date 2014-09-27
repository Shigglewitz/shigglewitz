package org.shigglewitz.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.shigglewitz.utils.Utils;

public class UtilsTest {
    @Test
    public void testRemoveWhiteSpace() {
        String[] tests = { "           ", String.format(" \t "), "asdf 123",
                "  asdf  ", "The lazy brown fox jumped over the quick dog." };

        for (String s : tests) {
            this.assertHasWhiteSpace(s);
            this.assertHasNoWhiteSpace(Utils.removeAllWhiteSpace(s));
        }

        assertNull(Utils.removeAllWhiteSpace(null));
    }

    @Test
    public void testSplitWithDelimiter() {
        String input = "asdf;asdf";
        String delimiter = ";";
        String[] split = Utils.splitWithDelimiter(input, delimiter);
        assertEquals("Delimiter not found", delimiter, split[1]);
        input = "asdfasdf";
        split = Utils.splitWithDelimiter(input, delimiter);
        assertEquals("Incorrect number of strings found", 1, split.length);
    }

    @Test
    public void testSplitWithDelimiterAndLimit() {
        String input = "asdf;asdf;asdf";
        String delimiter = ";";
        String[] split = Utils.splitWithDelimiter(input, delimiter, 2);
        assertEquals("Incorrect number of strings found", 2, split.length);
        split = Utils.splitWithDelimiter(input, delimiter, 3);
        assertEquals("Incorrect number of strings found", 3, split.length);
        assertEquals("Delimiter not found", delimiter, split[1]);
        split = Utils.splitWithDelimiter(input, delimiter);
        assertEquals("Incorrect number of strings found", 5, split.length);
        assertEquals("Delimiter not found", delimiter, split[1]);
    }

    @Test
    public void testContainsRegex() {
        String regex = "a";
        String[] tests = { "asdf", "sdfasdf", "sdfa", "aaaaaaaa" };
        for (String s : tests) {
            assertTrue(s + " does not contain regex " + regex,
                    Utils.containsRegex(s, regex));
        }
    }

    @Test
    public void testGetMatchedGroup() {
        String[] tests = { "123(456)789", "[123[456[789]]asdf[456]]asdf" };
        char[][] delimiters = { { '(', ')' }, { '[', ']' } };
        int[][] expected = { { 3, 7 }, { 0, 23 } };

        int[] ret;
        for (int i = 0; i < tests.length; i++) {
            ret = Utils.getMatchedGroup(tests[i], delimiters[i][0],
                    delimiters[i][1]);
            assertEquals(expected[i][0], ret[0]);
            assertEquals(expected[i][1], ret[1]);
        }
    }

    @Test
    public void testProperUseOfMatchedGroup() {
        String test = "123(456)789";
        String expectedBefore = "123";
        String expected = "456";
        String expectedAfter = "789";
        int[] range = Utils.getMatchedGroup(test, '(', ')');
        assertEquals(expectedBefore, test.substring(0, range[0]));
        assertEquals(expected, test.substring(range[0] + 1, range[1]));
        assertEquals(expectedAfter, test.substring(range[1] + 1));
    }

    private static final String WHITE_SPACE_REGEX = ".*\\s.*";

    private void assertHasWhiteSpace(String s) {
        assertTrue("String \"" + s + "\" does not contain whitespace",
                s.matches(WHITE_SPACE_REGEX));
    }

    private void assertHasNoWhiteSpace(String s) {
        assertTrue("String \"" + s + "\" contains whitespace",
                !s.matches(WHITE_SPACE_REGEX));
    }
}
