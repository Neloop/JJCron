package cz.polankam.jjcron.common;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

/**
 *
 * @author Neloop
 */
public class CrontabTimeValueNamedParserTest {

    private Map<String, Integer> namedMap;
    private CrontabTimeValueNamedParser emptyParser;
    private CrontabTimeValueNamedParser namedParser;

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> CrontabTimeValueNamedParserTest <<<");
    }

    @Before
    public void setUp() {
        namedMap = new HashMap<>();
        namedMap.put("first", 1);
        namedMap.put("second", 2);
        namedMap.put("third", 3);

        emptyParser = new CrontabTimeValueNamedParser(null);
        namedParser = new CrontabTimeValueNamedParser(namedMap);
    }

    @Test
    public void test_NullValues() {
        // nothing should happen
        CrontabTimeValueNamedParser parser
                = new CrontabTimeValueNamedParser(null);
    }

    @Test(expected = FormatException.class)
    public void testParse_NullString() throws FormatException {
        emptyParser.parse(null);
    }

    @Test(expected = FormatException.class)
    public void testParse_BadFormat_1() throws FormatException {
        emptyParser.parse("bad value");
    }

    @Test(expected = FormatException.class)
    public void testParse_BadFormat_2() throws FormatException {
        emptyParser.parse("* 1 */2");
    }

    @Test(expected = FormatException.class)
    public void testParse_BadFormatAsterisk() throws FormatException {
        emptyParser.parse("**");
    }

    @Test
    public void testParse_CorrectAsterisk() throws FormatException {
        CrontabTimeValue value = emptyParser.parse("  *");
        assertEquals(CrontabTimeValueType.ASTERISK, value.valueType);
    }

    @Test(expected = FormatException.class)
    public void testParse_BadFormatPeriod_1() throws FormatException {
        emptyParser.parse("**/");
    }

    @Test(expected = FormatException.class)
    public void testParse_BadFormatPeriod_2() throws FormatException {
        emptyParser.parse("/");
    }

    @Test(expected = FormatException.class)
    public void testParse_BadFormatPeriod_3() throws FormatException {
        emptyParser.parse("/*2");
    }

    @Test
    public void testParse_CorrectPeriod() throws FormatException {
        CrontabTimeValue value = emptyParser.parse("  */2");
        assertEquals(CrontabTimeValueType.PERIOD, value.valueType);
        assertEquals(1, value.values.size());
        assertEquals((Integer) 2, value.values.get(0));
    }

    @Test(expected = FormatException.class)
    public void testParse_BadFormatList_1() throws FormatException {
        emptyParser.parse("1,5.4");
    }

    @Test(expected = FormatException.class)
    public void testParse_BadFormatList_2() throws FormatException {
        emptyParser.parse("1, *, 5");
    }

    @Test(expected = FormatException.class)
    public void testParse_BadFormatList_3() throws FormatException {
        emptyParser.parse("1, */2, 5");
    }

    @Test
    public void testParse_CorrectList() throws FormatException {
        CrontabTimeValue value = emptyParser.parse(" 1,  5, 4,  7");
        assertEquals(CrontabTimeValueType.LIST, value.valueType);
        assertEquals(4, value.values.size());
        assertTrue(value.values.contains((Integer) 1));
        assertTrue(value.values.contains((Integer) 5));
        assertTrue(value.values.contains((Integer) 4));
        assertTrue(value.values.contains((Integer) 7));
    }

    @Test(expected = FormatException.class)
    public void testParse_BadFormatNamedList() throws FormatException {
        namedParser.parse("first, firstt");
    }

    @Test
    public void testParse_CorrectNamedList() throws FormatException {
        CrontabTimeValue value = namedParser.parse(" 1,  second, 4,  third");
        assertEquals(CrontabTimeValueType.LIST, value.valueType);
        assertEquals(4, value.values.size());
        assertTrue(value.values.contains((Integer) 1));
        assertTrue(value.values.contains((Integer) 2));
        assertTrue(value.values.contains((Integer) 4));
        assertTrue(value.values.contains((Integer) 3));
    }
}
