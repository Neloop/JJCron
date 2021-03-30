package cz.polankam.jjcron.remote;

import cz.polankam.jjcron.common.PrintMethodNameTestWatcher;
import cz.polankam.jjcron.common.TaskMetadata;
import cz.polankam.jjcron.common.TaskStats;
import java.time.LocalDateTime;
import org.junit.After;
import org.junit.AfterClass;
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
public class TaskDetailTest {

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> TaskDetailTest <<<");
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

    @Test
    public void test_Correct() {
        String id = "id";
        String name = "name";
        LocalDateTime next = LocalDateTime.now();
        TaskStats stats = new TaskStats();
        TaskMetadata meta = mock(TaskMetadata.class);
        TaskDetail detail = new TaskDetail(id, name, next, stats, meta);

        assertEquals(detail.id, id);
        assertEquals(detail.name, name);
        assertEquals(detail.nextExecutionTime, next);
        assertEquals(detail.stats, stats);
        assertEquals(detail.metadata, meta);
    }
}
