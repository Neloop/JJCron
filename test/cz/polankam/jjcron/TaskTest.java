package cz.polankam.jjcron;

import cz.polankam.jjcron.common.CrontabTime;
import cz.polankam.jjcron.common.TaskMetadata;
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
public class TaskTest {

    private String command;
    private CrontabTime time;
    private TaskMetadata taskMeta;
    private Task task;

    private class FakeTask extends Task {
        FakeTask(TaskMetadata meta) throws TaskException {
            super(meta);
        }

        @Override
        public void run() throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> TaskTest <<<");
    }

    @Before
    public void setUp() throws Exception {
        command = "cmd";
        time = new CrontabTime("*", "*", "*", "*", "*", "*");
        taskMeta = new TaskMetadata(time, command);
        task = new FakeTask(taskMeta);
    }

    @Test
    public void test_CorrectValues() {
        assertEquals(task.metadata(), taskMeta);
        assertEquals(task.metadata().command(), command);
        assertEquals(task.metadata().time(), time);
        assertEquals(task.timeUnit(), time.timeUnit());
    }

    @Test(expected = TaskException.class)
    public void test_NullTaskMetadata() throws TaskException {
        Task fake = new FakeTask(null);
    }
}
