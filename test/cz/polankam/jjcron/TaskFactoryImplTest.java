package cz.polankam.jjcron;

import cz.polankam.jjcron.common.CrontabTime;
import cz.polankam.jjcron.common.FormatException;
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
public class TaskFactoryImplTest {

    private CrontabTime testTime;
    private TaskFactoryImpl factory;

    public class NoSuitableConstructorTask extends Task {

        NoSuitableConstructorTask(int nothing)
                throws TaskException, FormatException {
            super(new TaskMetadata(
                    new CrontabTime("*", "*", "*", "*", "*", "*"), ""));
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
        System.out.println(">>> TaskFactoryImplTest <<<");
    }

    @Before
    public void setUp() throws FormatException {
        testTime = new CrontabTime("*", "*", "*", "*", "*", "*");
        factory = new TaskFactoryImpl();
    }

    @Test(expected = TaskException.class)
    public void testCreateTask_NullTaskMetadata() throws TaskException {
        factory.createTask(null);
    }

    @Test(expected = TaskException.class)
    public void testCreateTask_FromClassWithoutClassImplementation()
            throws TaskException, FormatException {
        factory.createTask(new TaskMetadata(testTime,
                "<class>NonExistentClass</class>"));
    }

    @Test(expected = TaskException.class)
    public void testCreateTask_FromClassWithoutTaskAsParent()
            throws TaskException, FormatException {
        factory.createTask(new TaskMetadata(testTime,
                "<class>cz.polankam.jjcron.TaskFactoryImplTest</class>"));
    }

    @Test
    public void testCreateTask_FromClass() throws Exception {
        Task task = factory.createTask(new TaskMetadata(testTime,
                "<class>cz.polankam.jjcron.PrintDotTask</class>"));

        assertTrue(task instanceof PrintDotTask);
    }

    @Test
    public void testCreateTask_FromCommand() throws Exception {
        String cmd = "echo \"Hello World!\"";
        Task task = factory.createTask(new TaskMetadata(testTime, cmd));

        assertTrue(task instanceof CmdTask);
        assertEquals(task.metadata().time(), testTime);
        assertEquals(task.metadata().command(), cmd);
    }
}
