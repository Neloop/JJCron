package cz.polankam.jjcron;

import cz.polankam.jjcron.common.TaskStats;
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
public class TaskHolderTest {

    private String id;
    private Task task;
    private TaskStats stats;

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> TaskHolderTest <<<");
    }

    @Before
    public void setUp() throws Exception {
        id = "id";
        task = mock(Task.class);
        stats = new TaskStats();
    }

    @Test
    public void test_CorrectValues() throws TaskException {
        TaskHolder holder = new TaskHolder(id, task, stats);
        assertEquals(id, holder.id);
        assertEquals(task, holder.task);
        assertEquals(stats, holder.stats);
    }

    @Test(expected = TaskException.class)
    public void test_NullIdentification() throws TaskException {
        TaskHolder holder = new TaskHolder(null, task, stats);
    }

    @Test(expected = TaskException.class)
    public void test_NullTask() throws TaskException {
        TaskHolder holder = new TaskHolder(id, null, stats);
    }

    @Test(expected = TaskException.class)
    public void test_NullTaskStats() throws TaskException {
        TaskHolder holder = new TaskHolder(id, task, null);
    }
}
