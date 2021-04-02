package cz.polankam.jjcron.common;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import static org.mockito.Mockito.*;

/**
 *
 * @author Neloop
 */
public class CrontabTimeGeneralUnitTest {

    private int minValue;
    private int maxValue;
    private int period;
    private String unitStr;
    private CrontabTimeValue value;
    private CrontabTimeValueParser parser;

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> CrontabTimeGeneralUnitTest <<<");
    }

    @Before
    public void setUp() throws FormatException {
        unitStr = "unit";
        minValue = 0;
        maxValue = 59;
        period = 60;
        value = new CrontabTimeValue(CrontabTimeValueType.ASTERISK,
                Collections.<Integer>emptyList());
        parser = mock(CrontabTimeValueParser.class);
        when(parser.parse(anyString())).thenReturn(value);
    }

    @Test
    public void testConstruction_Correct() throws FormatException {
        CrontabTimeGeneralUnit unit = new CrontabTimeGeneralUnit(unitStr,
                minValue, maxValue, period, parser);
        verify(parser).parse(unitStr);
    }

    @Test
    public void testIsChanged_Correct() throws FormatException {
        CrontabTimeGeneralUnit unit = new CrontabTimeGeneralUnit(unitStr,
                minValue, maxValue, period, parser);
        assertFalse(unit.isChanged());
        unit.delay(LocalDateTime.now(), 0, true); // force change
        assertTrue(unit.isChanged());
    }

    @Test
    public void testValue_Correct() throws FormatException {
        CrontabTimeGeneralUnit unit = new CrontabTimeGeneralUnit(unitStr,
                minValue, maxValue, period, parser);
        assertEquals(value, unit.value());
    }

    @Test
    public void testEquals_False() throws FormatException {
        CrontabTimeGeneralUnit firstUnit = new CrontabTimeGeneralUnit(unitStr,
                minValue, maxValue, period, parser);
        CrontabTimeGeneralUnit secondUnit = new CrontabTimeGeneralUnit(
                unitStr, minValue + 1, maxValue + 1, period + 1, parser);

        assertFalse(firstUnit.equals(secondUnit));
    }

    @Test
    public void testEquals_True() throws FormatException {
        CrontabTimeGeneralUnit firstUnit = new CrontabTimeGeneralUnit(unitStr,
                minValue, maxValue, period, parser);
        CrontabTimeGeneralUnit secondUnit = new CrontabTimeGeneralUnit(
                unitStr + " hello", minValue, maxValue, period, parser);

        // given string can be different but parsed values have to be equal
        assertTrue(firstUnit.equals(secondUnit));
    }

    @Test
    public void testDelay_AsteriskNotChanged() throws FormatException {
        when(parser.parse(unitStr)).thenReturn(value);
        CrontabTimeGeneralUnit unit = new CrontabTimeGeneralUnit(unitStr,
                minValue, maxValue, period, parser);

        assertEquals(1, unit.delay(LocalDateTime.MIN, 0, false));
    }

    @Test
    public void testDelay_AsteriskChanged() throws FormatException {
        when(parser.parse(unitStr)).thenReturn(value);
        CrontabTimeGeneralUnit unit = new CrontabTimeGeneralUnit(unitStr,
                minValue, maxValue, period, parser);

        assertEquals(0, unit.delay(LocalDateTime.MIN, 0, true));
    }

    @Test
    public void testDelay_PeriodNotChanged() throws FormatException {
        CrontabTimeValue retValue = new CrontabTimeValue(
                CrontabTimeValueType.PERIOD, Arrays.asList(2));
        when(parser.parse(unitStr)).thenReturn(retValue);
        CrontabTimeGeneralUnit unit = new CrontabTimeGeneralUnit(unitStr,
                minValue, maxValue, period, parser);

        assertEquals(2, unit.delay(LocalDateTime.MIN, 0, false));
    }

    @Test
    public void testDelay_PeriodChanged() throws FormatException {
        CrontabTimeValue retValue = new CrontabTimeValue(
                CrontabTimeValueType.PERIOD, Arrays.asList(2));
        when(parser.parse(unitStr)).thenReturn(retValue);
        CrontabTimeGeneralUnit unit = new CrontabTimeGeneralUnit(unitStr,
                minValue, maxValue, period, parser);

        assertEquals(0, unit.delay(LocalDateTime.MIN, 0, true));
    }

    @Test
    public void testDelay_ListNotChanged() throws FormatException {
        CrontabTimeValue retValue = new CrontabTimeValue(
                CrontabTimeValueType.LIST, Arrays.asList(2));
        when(parser.parse(unitStr)).thenReturn(retValue);
        CrontabTimeGeneralUnit unit = new CrontabTimeGeneralUnit(unitStr,
                minValue, maxValue, period, parser);

        assertEquals(2, unit.delay(LocalDateTime.MIN, 0, false));
    }

    @Test
    public void testDelay_ListChanged() throws FormatException {
        CrontabTimeValue retValue = new CrontabTimeValue(
                CrontabTimeValueType.LIST, Arrays.asList(2));
        when(parser.parse(unitStr)).thenReturn(retValue);
        CrontabTimeGeneralUnit unit = new CrontabTimeGeneralUnit(unitStr,
                minValue, maxValue, period, parser);

        assertEquals(2, unit.delay(LocalDateTime.MIN, 0, true));
    }
}
