package cz.polankam.jjcron.common;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
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
public class CrontabTimeTest {

    private CrontabTimeUnit second;
    private CrontabTimeUnit minute;
    private CrontabTimeUnit hour;
    private CrontabTimeUnit dayOfMonth;
    private CrontabTimeUnit month;
    private CrontabTimeUnit dayOfWeek;
    private CrontabTime time;

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> CrontabTimeTest <<<");
    }

    @Before
    public void setUp() throws FormatException {
        second = mock(CrontabTimeUnit.class);
        minute = mock(CrontabTimeUnit.class);
        hour = mock(CrontabTimeUnit.class);
        dayOfMonth = mock(CrontabTimeUnit.class);
        month = mock(CrontabTimeUnit.class);
        dayOfWeek = mock(CrontabTimeUnit.class);
        time = new CrontabTime(second, minute, hour, dayOfMonth,
                month, dayOfWeek);

        when(second.delay(any(LocalDateTime.class), anyInt(), anyBoolean())).thenReturn(0);
        when(minute.delay(any(LocalDateTime.class), anyInt(), anyBoolean())).thenReturn(0);
        when(hour.delay(any(LocalDateTime.class), anyInt(), anyBoolean())).thenReturn(0);
        when(dayOfMonth.delay(any(LocalDateTime.class), anyInt(), anyBoolean())).thenReturn(0);
        when(month.delay(any(LocalDateTime.class), anyInt(), anyBoolean())).thenReturn(0);
        when(dayOfWeek.delay(any(LocalDateTime.class), anyInt(), anyBoolean())).thenReturn(0);

        // note that every value of CrontabTimeUnit.isChanged() will be false
        // due to mocking framework default return values policy
    }

    @Test(expected = FormatException.class)
    public void testConstruction_NullString() throws FormatException {
        String nullStr = null;
        CrontabTime crontabTime = new CrontabTime(nullStr, nullStr, nullStr,
                nullStr, nullStr, nullStr);
    }

    @Test(expected = FormatException.class)
    public void testConstruction_NullTimeUnit() throws FormatException {
        CrontabTimeUnit nullUnit = null;
        CrontabTime crontabTime = new CrontabTime(nullUnit, nullUnit, nullUnit,
                nullUnit, nullUnit, nullUnit);
    }

    @Test
    public void testConstruction_Correct() {
        assertEquals(second, time.second);
        assertEquals(minute, time.minute);
        assertEquals(hour, time.hour);
        assertEquals(dayOfMonth, time.dayOfMonth);
        assertEquals(month, time.month);
        assertEquals(dayOfWeek, time.dayOfWeek);
    }

    @Test
    public void testTimeUnit_Correct() throws Exception {
        assertEquals(time.timeUnit(), TimeUnit.SECONDS);
    }

    @Test
    public void testDelay_OneSecondDelay() {
        LocalDateTime next = LocalDateTime.of(2012, 12, 21, 0, 0);
        when(second.delay(next, 0, false)).thenReturn(1);

        assertEquals(1, time.delay(next));
    }

    @Test
    public void testDelay_OneMinuteDelay() {
        LocalDateTime next = LocalDateTime.of(2012, 12, 21, 0, 0);
        when(second.delay(next, 0, false)).thenReturn(60);

        assertEquals(60, time.delay(next));
    }

    @Test
    public void testDelay_OneHourDelay() {
        LocalDateTime next = LocalDateTime.of(2012, 12, 21, 0, 0, 0);
        when(second.delay(next, 0, false)).thenReturn(60);
        when(minute.delay(next.plusSeconds(60), 1, false)).thenReturn(59);

        assertEquals(60 * 60, time.delay(next));
    }

    @Test
    public void testDelay_OneDayDelay() {
        LocalDateTime next = LocalDateTime.of(2012, 12, 21, 0, 0);
        when(second.delay(next, 0, false)).thenReturn(60);
        when(minute.delay(next.plusSeconds(60), 1, false)).thenReturn(59);
        when(hour.delay(next.plusSeconds(60).plusMinutes(59), 1, false)).thenReturn(23);

        assertEquals(60 * 60 * 24, time.delay(next));
    }
}
