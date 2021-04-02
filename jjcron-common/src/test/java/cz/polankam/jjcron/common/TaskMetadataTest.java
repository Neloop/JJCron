package cz.polankam.jjcron.common;

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
public class TaskMetadataTest {

    private CrontabTime time;
    private String command;

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> TaskMetadataTest <<<");
    }

    @Before
    public void setUp() {
        time = mock(CrontabTime.class);
        command = "command";
    }

    @Test(expected = FormatException.class)
    public void test_NullTime() throws FormatException {
        TaskMetadata meta = new TaskMetadata(null, command);
    }

    @Test(expected = FormatException.class)
    public void test_NullCommand() throws FormatException {
        TaskMetadata meta = new TaskMetadata(time, null);
    }

    @Test
    public void test_Correct() throws FormatException {
        TaskMetadata meta = new TaskMetadata(time, command);
        assertEquals(meta.time(), time);
        assertEquals(meta.command(), command);
    }

    @Test
    public void testEquals_False() throws FormatException {
        TaskMetadata firstMeta = new TaskMetadata(time, command);
        TaskMetadata secondMeta = new TaskMetadata(time, command + " ");
        assertFalse(firstMeta.equals(secondMeta));
    }

    @Test
    public void testEquals_True() throws FormatException {
        TaskMetadata firstMeta = new TaskMetadata(time, command);
        TaskMetadata secondMeta = new TaskMetadata(time, command);
        assertTrue(firstMeta.equals(secondMeta));
    }
}
