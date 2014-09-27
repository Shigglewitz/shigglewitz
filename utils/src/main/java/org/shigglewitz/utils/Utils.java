package org.shigglewitz.utils;

import java.io.File;

/**
 * to count the number of lines in a project, use file search and use this
 * regex: \n[\s]*
 * 
 * source:
 * http://stackoverflow.com/questions/1043666/counting-line-numbers-in-eclipse
 * 
 * @author Daniel
 * 
 */

public class Utils {
    private static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    public static String removeAllWhiteSpace(String s) {
        if (s == null) {
            return null;
        }
        return s.replaceAll("\\s+", "");
    }

    public static String[] splitWithDelimiter(String s, String delimiter,
            int limit) {
        String[] ret = s.split(String.format(WITH_DELIMITER, delimiter), limit);
        return ret;
    }

    public static String[] splitWithDelimiter(String s, String delimiter) {
        return splitWithDelimiter(s, delimiter, 0);
    }

    public static boolean containsRegex(String input, String regex) {
        return input.matches(".*" + regex + ".*");
    }

    /**
     * 
     * @param input
     * @param begin
     * @param end
     * @return [0] is the beginning, [1] is the end
     */
    public static int[] getMatchedGroup(String input, char begin, char end) {
        if (input == null) {
            return null;
        }
        int firstGroupMark = input.indexOf(begin);
        int firstEndMark = input.indexOf(end);
        if (firstGroupMark == -1 || firstEndMark == -1
                || firstGroupMark > firstEndMark) {
            return null;
        }
        int[] ret = new int[2];
        ret[0] = -1;
        ret[1] = -1;
        int matches = 0;
        char[] chars = input.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == begin) {
                if (ret[0] < 0) {
                    ret[0] = i;
                }
                matches++;
            } else if (chars[i] == end) {
                matches--;
                if (matches == 0) {
                    ret[1] = i;
                    break;
                }
            }
        }

        return ret;
    }

    public static void cleanDirectory(String dirName) {
        File dir = new File(dirName);
        for (File file : dir.listFiles()) {
            file.delete();
        }
    }
}
