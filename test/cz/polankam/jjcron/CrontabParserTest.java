package cz.polankam.jjcron;

import cz.polankam.jjcron.common.CrontabTime;
import cz.polankam.jjcron.common.FormatException;
import cz.polankam.jjcron.common.TaskMetadata;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

/**
 *
 * @author Neloop
 */
public class CrontabParserTest {

    private List<TaskMetadata> testTasks;

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> CrontabParserTest <<<");
    }

    @Before
    public void setUp() throws FormatException {
        CrontabTime first = new CrontabTime("0", "*", "*", "*", "*", "*");
        CrontabTime second = new CrontabTime("1", "*", "*", "*", "*", "*");

        testTasks = new ArrayList<>();
        testTasks.add(new TaskMetadata(first, "first"));
        testTasks.add(new TaskMetadata(second, "second"));
    }

    @Test(expected = ParserException.class)
    public void testParse_NullInputStream() throws ParserException {
        InputStream input = null;
        CrontabParser.load(input);
    }

    @Test(expected = ParserException.class)
    public void testParse_NullString() throws ParserException {
        String input = null;
        CrontabParser.load(input);
    }

    @Test
    public void testParse_EmptyString() throws ParserException {
        String input = "";
        List<TaskMetadata> expResult = new ArrayList<>();
        List<TaskMetadata> result = CrontabParser.load(input);
        assertEquals(expResult, result);
    }

    @Test(expected = ParserException.class)
    public void testParseFile_NullFilename() throws ParserException {
        String filename = null;
        CrontabParser.loadFile(filename);
    }

    @Test(expected = ParserException.class)
    public void testParseFile_EmptyFilename() throws ParserException {
        String filename = "";
        CrontabParser.loadFile(filename);
    }

    @Test(expected = ParserException.class)
    public void testParse_LessTimeUnits() throws ParserException {
        String input = "0 command\n1 command";
        CrontabParser.load(input);
    }

    @Test(expected = ParserException.class)
    public void testParse_TimeUnitsMissing() throws ParserException {
        String input = "first\nsecond\n0 0 0 0 0 0 ls";
        CrontabParser.load(input);
    }

    @Test(expected = ParserException.class)
    public void testParse_CommandMissing() throws ParserException {
        String input = "0 1 2 3 4 5\n1 2 3 4 5 6\n2 3 4 5 6 7";
        CrontabParser.load(input);
    }

    @Test
    public void testParse_CorrectFewEmptyLines() throws ParserException {
        String input = "\n";
        for (TaskMetadata meta : testTasks) {
            input += meta.time().toString() + " " + meta.command();
            input += "\n\n\n";
        }

        List<TaskMetadata> result = CrontabParser.load(input);
        assertEquals(result.size(), testTasks.size());
        for (int i = 0; i < testTasks.size(); ++i) {
            assertEquals(result.get(i), testTasks.get(i));
        }
    }

    @Test
    public void testParse_CorrectFormat() throws ParserException {
        String input = "";
        for (TaskMetadata meta : testTasks) {
            input += meta.time().toString() + " " + meta.command() + "\n";
        }

        List<TaskMetadata> result = CrontabParser.load(input);
        assertEquals(result.size(), testTasks.size());
        for (int i = 0; i < testTasks.size(); ++i) {
            assertEquals(result.get(i), testTasks.get(i));
        }
    }

    @Test(expected = ParserException.class)
    public void testSave_NullTasks() throws ParserException {
        OutputStream output = new ByteArrayOutputStream();
        CrontabParser.save(null, output);
    }

    @Test(expected = ParserException.class)
    public void testSave_NullOutputStream() throws ParserException {
        OutputStream output = null;
        CrontabParser.save(testTasks, output);
    }

    @Test(expected = ParserException.class)
    public void testSave_NullFilename() throws ParserException {
        String filename = null;
        CrontabParser.save(testTasks, filename);
    }

    @Test(expected = ParserException.class)
    public void testSave_EmptyFilename() throws ParserException {
        CrontabParser.save(testTasks, "");
    }

    @Test
    public void testSave_Correct() throws ParserException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CrontabParser.save(testTasks, output);

        // conversion to some more readable input
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(input));

        // asserting
        for (TaskMetadata meta : testTasks) {
            String expected = meta.time().toString() + " " + meta.command();
            assertEquals(reader.readLine(), expected);
        }
        assertEquals(reader.readLine(), null);
    }
}
