package jjcron;

import jjcron.polankam.ms.mff.cuni.cz.TaskMetadata;
import jjcron.polankam.ms.mff.cuni.cz.CrontabParser;
import java.io.InputStream;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

/**
 *
 * @author Neloop
 */
public class CrontabParserTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public CrontabParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parseFile method, of class CrontabParser.
     */
    @Test
    public void testParseFile() throws Exception {
        System.out.println("parseFile");
        String filename = "";
        List<TaskMetadata> expResult = null;
        List<TaskMetadata> result = CrontabParser.parseFile(filename);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parse method, of class CrontabParser.
     */
    @Test
    public void testParse_InputStream() throws Exception {
        System.out.println("parse");
        InputStream input = null;
        List<TaskMetadata> expResult = null;
        List<TaskMetadata> result = CrontabParser.parse(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parse method, of class CrontabParser.
     */
    @Test
    public void testParse_String() throws Exception {
        System.out.println("parse");
        String input = "";
        List<TaskMetadata> expResult = null;
        List<TaskMetadata> result = CrontabParser.parse(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
