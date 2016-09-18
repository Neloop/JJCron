package cz.polankam.jjcron.common;

import cz.polankam.jjcron.common.CrontabTime;
import java.util.concurrent.TimeUnit;
import org.junit.After;
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
public class CrontabTimeTest {

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> CrontabTimeTest <<<");
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTimeUnit_SecondsReturned() throws Exception {
        System.out.println("testTimeUnit");
        CrontabTime crontabTime = new CrontabTime("1", "1", "1", "1", "1", "1");
        assertEquals(crontabTime.timeUnit(), TimeUnit.SECONDS);
    }

    @Test
    public void testDelay_1() {
        System.out.println("testDelay");
    }
}
