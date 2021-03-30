package cz.polankam.jjcron.remote.manager;

import cz.polankam.jjcron.common.TaskMetadata;
import cz.polankam.jjcron.remote.Client;
import cz.polankam.jjcron.remote.TaskDetail;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.mockito.InOrder;
import static org.mockito.Mockito.*;

/**
 *
 * @author Neloop
 */
public class ClientWrapperTest {

    private ClientAddress address;
    private ClientFactory factory;
    private Client client;
    private ClientWrapper wrapper;
    private List<TaskDetail> clientTasks;
    private TaskDetail firstTask;
    private TaskDetail secondTask;

    @Rule
    public TestWatcher watcher = new PrintMethodNameTestWatcher();

    @BeforeClass
    public static void setUpClass() {
        System.out.println(">>> ClientWrapperTest <<<");
    }

    @Before
    public void setUp() throws Exception {
        address = new ClientAddress("//localhost", "cron");
        factory = mock(ClientFactory.class);
        client = mock(Client.class);
        wrapper = new ClientWrapper(address, factory);
        firstTask = new TaskDetail("first", "first", null, null, null);
        secondTask = new TaskDetail("second", "second", null, null, null);
        clientTasks = new ArrayList<>();

        clientTasks.add(firstTask);
        clientTasks.add(secondTask);

        when(factory.connect(address)).thenReturn(client);
        when(client.getTasks()).thenReturn(clientTasks);
        wrapper.connect();
    }

    @Test(expected = ManagerException.class)
    public void test_NullAddress() throws ManagerException {
        ClientWrapper nullWrapper = new ClientWrapper(null, factory);
    }

    @Test(expected = ManagerException.class)
    public void test_NullFactory() throws ManagerException {
        ClientWrapper nullWrapper = new ClientWrapper(address, null);
    }

    @Test
    public void test_CorrectConstruction() {
        assertEquals(wrapper.getClientAddress(), address);
    }

    @Test
    public void testConnect_Correct() throws Exception {
        verify(factory).connect(address);
        verify(client).isPaused();
    }

    @Test(expected = ManagerException.class)
    public void testAddTask_NullTaskMetadata() throws Exception {
        wrapper.addTask(null);
    }

    @Test
    public void testAddTask_Correct() throws Exception {
        TaskMetadata meta = mock(TaskMetadata.class);
        wrapper.addTask(meta);

        verify(client).addTask(meta);
        verify(client).getTasks();
    }

    @Test
    public void testDeleteTask_NullIdentification() throws Exception {
        wrapper.deleteTask(null); // nothing should happen
    }

    @Test
    public void testDeleteTask_Correct() throws Exception {
        wrapper.deleteTask("first");
        verify(client).deleteTask(firstTask);
    }

    @Test
    public void testDisconnect_Correct() throws Exception {
        wrapper.disconnect();
        verify(factory).disconnect(client);
    }

    @Test
    public void testShutdown_Correct() throws Exception {
        wrapper.shutdown();
        verify(client).shutdown();
    }

    @Test
    public void testIsPaused_Correct() throws Exception {
        assertFalse(wrapper.isPaused());
        wrapper.pause();
        assertTrue(wrapper.isPaused());
        verify(client).pause();
    }

    @Test
    public void testIsListOpened_Correct() throws Exception {
        assertFalse(wrapper.isListOpened());
        wrapper.openTaskList();
        assertTrue(wrapper.isListOpened());
        wrapper.closeTaskList();
        assertFalse(wrapper.isListOpened());

        verify(client).getTasks();
    }

    @Test
    public void testTaskObservableList_Correct() throws Exception {
        wrapper.refreshTasks();
        wrapper.fillTaskObservableList();
        for (TaskDetail task : clientTasks) {
            assertTrue(wrapper.tasksObservableList.contains(task));
        }
    }

    @Test
    public void testPauseUnpause_Correct() throws Exception {
        assertFalse(wrapper.isPaused());
        wrapper.pause();
        assertTrue(wrapper.isPaused());
        wrapper.unpause();
        assertFalse(wrapper.isPaused());

        InOrder inOrder = inOrder(client);
        inOrder.verify(client).pause();
        inOrder.verify(client).unpause();
    }

    @Test
    public void testReloadCrontab_Correct() throws Exception {
        wrapper.reloadCrontab();
        verify(client).reloadCrontab();
    }

    @Test
    public void testSaveToCrontab_Correct() throws Exception {
        wrapper.saveToCrontab();
        verify(client).saveToCrontab();
    }
}
