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
public class CrontabTimeDayOfMonthUnitTest {

    private String unitStr;
    private CrontabTimeValue value;
    private CrontabTimeValueParser parser;

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> CrontabTimeDayOfMonthUnitTest <<<");
    }

    @Before
    public void setUp() throws FormatException {
        unitStr = "unit";
        value = new CrontabTimeValue(CrontabTimeValueType.ASTERISK,
                Collections.<Integer>emptyList());
        parser = mock(CrontabTimeValueParser.class);
    }

    @Test
    public void testDelay_AsteriskNotChanged() throws FormatException {
        when(parser.parse(unitStr)).thenReturn(value);
        CrontabTimeDayOfMonthUnit unit
                = new CrontabTimeDayOfMonthUnit(unitStr, parser);

        assertEquals(1, unit.delay(LocalDateTime.now(), 1, false));
    }

    @Test
    public void testDelay_AsteriskChanged() throws FormatException {
        when(parser.parse(unitStr)).thenReturn(value);
        CrontabTimeDayOfMonthUnit unit
                = new CrontabTimeDayOfMonthUnit(unitStr, parser);

        assertEquals(0, unit.delay(LocalDateTime.now(), 1, true));
    }

    @Test
    public void testDelay_PeriodNotChanged() throws FormatException {
        CrontabTimeValue retValue = new CrontabTimeValue(
                CrontabTimeValueType.PERIOD, Arrays.asList(1));
        when(parser.parse(unitStr)).thenReturn(retValue);
        CrontabTimeDayOfMonthUnit unit
                = new CrontabTimeDayOfMonthUnit(unitStr, parser);

        assertEquals(1, unit.delay(LocalDateTime.now(), 1, false));
    }

    @Test
    public void testDelay_PeriodChanged() throws FormatException {
        CrontabTimeValue retValue = new CrontabTimeValue(
                CrontabTimeValueType.PERIOD, Arrays.asList(1));
        when(parser.parse(unitStr)).thenReturn(retValue);
        CrontabTimeDayOfMonthUnit unit
                = new CrontabTimeDayOfMonthUnit(unitStr, parser);

        assertEquals(0, unit.delay(LocalDateTime.now(), 1, true));
    }

    @Test
    public void testDelay_ListNotChanged() throws FormatException {
        CrontabTimeValue retValue = new CrontabTimeValue(
                CrontabTimeValueType.LIST, Arrays.asList(2));
        when(parser.parse(unitStr)).thenReturn(retValue);
        CrontabTimeDayOfMonthUnit unit
                = new CrontabTimeDayOfMonthUnit(unitStr, parser);

        unit.delay(LocalDateTime.now(), 1, false);
        assertEquals(1, unit.delay(LocalDateTime.now(), 1, false));
    }

    @Test
    public void testDelay_ListChanged() throws FormatException {
        CrontabTimeValue retValue = new CrontabTimeValue(
                CrontabTimeValueType.LIST, Arrays.asList(2));
        when(parser.parse(unitStr)).thenReturn(retValue);
        CrontabTimeDayOfMonthUnit unit
                = new CrontabTimeDayOfMonthUnit(unitStr, parser);

        assertEquals(1, unit.delay(LocalDateTime.now(), 1, true));
    }
}
