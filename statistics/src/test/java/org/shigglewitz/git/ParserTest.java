package org.shigglewitz.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.shigglewitz.graphing.DataGrapher;
import org.shigglewitz.graphing.DataGrapher.DrawType;
import org.shigglewitz.graphing.Grapher;
import org.shigglewitz.testutils.ImageComparison;
import org.shigglewitz.utils.ImageMaker;
import org.junit.Test;
import org.shigglewitz.git.Parser;
import org.shigglewitz.git.commit.Commit;

public class ParserTest {
    private static final File testDir = getRootDirectory();
    private static final String COMMITS_FOLDER = "testGitCommits/";
    private static final String STATS_FOLDER = "testGraphGitStats/";

    public static File getRootDirectory() {
        try {
            return new File(Thread.currentThread().getContextClassLoader()
                    .getResource("").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testParseCommitHistory() {
        Parser p = new Parser();
        List<Commit> commitHistory = p.parseCommitHistory(new File(testDir
                .getAbsolutePath() + "/" + COMMITS_FOLDER + "gitlog.txt"));
        assertEquals("Wrong number of commits parsed", 70, commitHistory.size());
        assertEquals("Wrong hash for beginning of commit history",
                "6df90d8db099a57e25a2173f6fa30471eaa002bb", commitHistory
                        .get(0).getHash());
        assertEquals("Wrong hash for end of commit history",
                "a67d6d293e1eec8e94d198c590eade1e5fe14a1f",
                commitHistory.get(commitHistory.size() - 1).getHash());
        assertEquals(
                "Wrong commit comment found",
                "Merge branch 'feature/refactor-modules' into develop"
                        + System.lineSeparator(),
                commitHistory.get(commitHistory.size() - 2).getComments());
        assertEquals("Wrong author found",
                "U-Daniel-PC\\Daniel <Daniel@Daniel-PC.(none)>", commitHistory
                        .get(31).getAuthor());
        assertEquals("Wrong author found",
                "Shigglewitz <shigglewitz@gmail.com>", commitHistory.get(32)
                        .getAuthor());
        for (Commit c : commitHistory) {
            if (c.getComments().startsWith("Merge branch")) {
                assertTrue("Merge attribute misisng for commit " + c.getHash(),
                        c.getMerge() != null && !c.getMerge().equals(""));
            }
        }
    }

    @Test
    public void testGraphCommitHistory() throws IOException {
        Parser p = new Parser();
        List<Commit> commitHistory = p.parseCommitHistory(new File(testDir
                .getAbsolutePath() + "/" + COMMITS_FOLDER + "gitlog.txt"));
        List<Point> totalTestsRun = new ArrayList<>();
        List<Point> totalTestsFail = new ArrayList<>();
        List<Point> totalTestsError = new ArrayList<>();
        List<Point> totalTestsSkip = new ArrayList<>();
        int i = 0;
        for (Commit c : commitHistory) {
            c.loadCommitData(STATS_FOLDER);
            totalTestsRun.add(new Point(i, c.getMavenResults()
                    .getTotalTestsRun()));
            totalTestsFail.add(new Point(i, c.getMavenResults()
                    .getTotalTestsFail()));
            totalTestsError.add(new Point(i, c.getMavenResults()
                    .getTotalTestsError()));
            totalTestsSkip.add(new Point(i, c.getMavenResults()
                    .getTotalTestsSkip()));
            i++;
        }
        DataGrapher dg = new DataGrapher();
        dg.addPoints(totalTestsRun, Color.GREEN);
        dg.addPoints(totalTestsError, Color.RED);
        dg.addPoints(totalTestsFail, Color.BLUE);
        dg.addPoints(totalTestsSkip, Color.BLACK);
        dg.setDrawType(DrawType.FILL);
        BufferedImage experiment = dg.getGraph(Grapher.DEFAULT_WIDTH,
                Grapher.DEFAULT_HEIGHT);

        try {
            ImageComparison.compareWholeImage("commit-history1", experiment);
        } catch (AssertionError e) {
            ImageMaker.saveImage(experiment, "commit-history");
            throw e;
        }
    }
}
