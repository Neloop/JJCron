package cz.cuni.mff.ms.polankam.jjcron;

import cz.cuni.mff.ms.polankam.jjcron.common.TaskMetadata;
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
        CrontabParser.parseFile(filename);
    }

    /**
     * Test of parse method, of class CrontabParser.
     */
    @Test(expected=ParserException.class)
    public void testParse_NullInputStream() throws Exception {
        System.out.println("parse");
        InputStream input = null;
        CrontabParser.parse(input);
    }

    /**
     * Test of parse method, of class CrontabParser.
     */
    @Test
    public void testParse_EmptyString() throws Exception {
        System.out.println("parse");
        String input = "";
        List<TaskMetadata> expResult = new ArrayList<>();
        List<TaskMetadata> result = CrontabParser.parse(input);
        assertEquals(expResult, result);
    }

}
