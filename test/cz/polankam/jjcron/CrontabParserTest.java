package cz.polankam.jjcron;

import cz.polankam.jjcron.CrontabParser;
import cz.polankam.jjcron.ParserException;
import cz.polankam.jjcron.common.TaskMetadata;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Neloop
 */
public class CrontabParserTest {

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> CrontabParserTest <<<");
    }

    /**
     * Test of parseFile method, of class CrontabParser.
     */
    @Test(expected=ParserException.class)
    public void testParseFile_EmptyFilename() throws Exception {
        System.out.println("parseFile");
        String filename = "";
        CrontabParser.loadFile(filename);
    }

    /**
     * Test of parse method, of class CrontabParser.
     */
    @Test(expected=ParserException.class)
    public void testParse_NullInputStream() throws Exception {
        System.out.println("parse");
        InputStream input = null;
        CrontabParser.load(input);
    }

    /**
     * Test of parse method, of class CrontabParser.
     */
    @Test
    public void testParse_EmptyString() throws Exception {
        System.out.println("parse");
        String input = "";
        List<TaskMetadata> expResult = new ArrayList<>();
        List<TaskMetadata> result = CrontabParser.load(input);
        assertEquals(expResult, result);
    }

}
