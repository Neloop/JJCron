package cz.polankam.jjcron;

import cz.polankam.jjcron.common.CrontabTime;
import cz.polankam.jjcron.common.FormatException;
import cz.polankam.jjcron.common.TaskMetadata;
import java.util.ArrayList;
import java.util.List;
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
public class TaskSchedulerTest {

    private Task printDotTask;
    private Task printDoubleDotTask;
    private CrontabTime everySecondTime;
    private CrontabTime everyMinuteTime;
    private TaskMetadata everySecondMeta;
    private TaskMetadata everyMinuteMeta;
    private TaskScheduler nullFactoryScheduler;
    private TaskScheduler scheduler;
    private List<TaskMetadata> emptyTasksList;
    private List<TaskMetadata> filledTasksList;

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> TaskSchedulerTest <<<");
    }

    @Before
    public void setUp() throws FormatException, TaskException {
        everySecondTime = new CrontabTime("*", "*", "*", "*", "*", "*");
        everySecondMeta = new TaskMetadata(everySecondTime, "sec");

        everyMinuteTime = new CrontabTime("0", "*", "*", "*", "*", "*");
        everyMinuteMeta = new TaskMetadata(everyMinuteTime, "min");

        printDotTask = new PrintDotTask();
        printDoubleDotTask = new PrintDoubleDotTask();
        nullFactoryScheduler = new TaskScheduler(null);
        scheduler = new TaskScheduler(new TaskFactoryImpl());

        emptyTasksList = new ArrayList<>();
        filledTasksList = new ArrayList<>();
        filledTasksList.add(everySecondMeta);
        filledTasksList.add(everyMinuteMeta);
    }

    @Test
    public void testExit_Correct() throws TaskException {
        scheduler.start(emptyTasksList);
        assertTrue(scheduler.isRunning());
        assertFalse(scheduler.isExited());

        scheduler.exit();
        assertFalse(scheduler.isRunning());
        assertTrue(scheduler.isExited());
    }

    @Test(expected = TaskException.class)
    public void testAddTask_NullTaskFactoryAtConstruction() throws TaskException {
        nullFactoryScheduler.addTask(everySecondMeta);
    }

    @Test(expected = TaskException.class)
    public void testAddTask_NullTask() throws TaskException {
        Task task = null;
        scheduler.addTask(task);
    }

    @Test(expected = TaskException.class)
    public void testAddTask_NullTaskMetadata() throws TaskException {
        TaskMetadata task = null;
        scheduler.addTask(task);
    }

    @Test
    public void testAddTask_TaskCorrect() throws TaskException {
        scheduler.addTask(printDotTask);
        assertEquals(scheduler.getTasks().size(), 1);
        scheduler.addTask(printDoubleDotTask);
        assertEquals(scheduler.getTasks().size(), 2);

        List<Task> tasks = scheduler.getTasks();
        assertTrue(tasks.contains(printDotTask));
        assertTrue(tasks.contains(printDoubleDotTask));
    }

    @Test
    public void testAddTask_TaskMetadataCorrect() throws TaskException {
        scheduler.addTask(everySecondMeta);
        assertEquals(scheduler.getTaskMetadatas().size(), 1);
        scheduler.addTask(everyMinuteMeta);
        assertEquals(scheduler.getTaskMetadatas().size(), 2);

        List<TaskMetadata> metadatas = scheduler.getTaskMetadatas();
        assertTrue(metadatas.contains(everySecondMeta));
        assertTrue(metadatas.contains(everyMinuteMeta));
    }

    @Test
    public void testDeleteTask_NullIdentification() {
        scheduler.deleteTask(null); // nothing should happen
    }

    @Test
    public void testDeleteTask_NonExistingTaskIdentification() {
        scheduler.deleteTask("identification"); // nothing should happen
    }

    @Test
    public void testDeleteTask_CorrectDelete() throws TaskException {
        String dotId = scheduler.addTask(printDotTask);
        assertEquals(scheduler.getTasks().size(), 1);
        String doubleDotId = scheduler.addTask(printDoubleDotTask);
        assertEquals(scheduler.getTasks().size(), 2);

       scheduler.deleteTask(dotId);
       assertEquals(scheduler.getTasks().size(), 1);
       scheduler.deleteTask(doubleDotId);
       assertEquals(scheduler.getTasks().size(), 0);
    }

    @Test(expected = TaskException.class)
    public void testStart_NullTasksList() throws TaskException {
        scheduler.start(null);
    }

    @Test
    public void testStart_Correct() throws TaskException {
        scheduler.start(emptyTasksList);
        assertTrue(scheduler.isRunning());
        assertFalse(scheduler.isExited());
    }

    @Test
    public void testStopStart_BadOrder() throws TaskException {
        scheduler.start(emptyTasksList);
        assertTrue(scheduler.isRunning());
        scheduler.start();
        assertTrue(scheduler.isRunning());
        scheduler.stop();
        assertFalse(scheduler.isRunning());
        scheduler.stop();
        assertFalse(scheduler.isRunning());
    }

    @Test
    public void testStopStart_Correct() throws TaskException {
        scheduler.start(emptyTasksList);
        assertTrue(scheduler.isRunning());
        scheduler.stop();
        assertFalse(scheduler.isRunning());
        scheduler.start();
        assertTrue(scheduler.isRunning());
    }

    @Test(expected = TaskException.class)
    public void testReloadTasks_NullTasksList() throws TaskException {
        scheduler.start(emptyTasksList);
        scheduler.reloadTasks(null);
    }

    @Test
    public void testReloadTasks_Correct() throws TaskException {
        scheduler.start(emptyTasksList);
        assertEquals(scheduler.getTasks().size(), 0);

        scheduler.reloadTasks(filledTasksList);
        assertEquals(scheduler.getTasks().size(), filledTasksList.size());
        for (TaskMetadata meta : scheduler.getTaskMetadatas()) {
            assertTrue(filledTasksList.contains(meta));
        }
    }
}
