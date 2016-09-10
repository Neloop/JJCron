package cz.cuni.mff.ms.polankam.jjcron.remote.manager;

import cz.cuni.mff.ms.polankam.jjcron.common.TaskMetadata;
import cz.cuni.mff.ms.polankam.jjcron.remote.Client;
import cz.cuni.mff.ms.polankam.jjcron.remote.TaskDetail;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Neloop
 */
public class ClientWrapper {
    /**
     *
     */
    public final ObservableList<TaskDetail> tasksObservableList;
    /**
     *
     */
    private final Map<String, TaskDetail> tasksMap;

    /**
     *
     */
    private final ClientAddress clientAddress;
    /**
     *
     */
    private final Client client;

    /**
     *
     */
    private boolean isTaskListFetched = false;
    /**
     *
     */
    private boolean isListOpened = false;
    /**
     *
     */
    private boolean isPaused = false;

    /**
     *
     * @param force
     * @throws Exception
     */
    private void fetchTasks(boolean force) throws Exception {
        if (!force && isTaskListFetched) {
            return;
        }

        tasksMap.clear();
        isTaskListFetched = true;

        List<TaskDetail> tasks = client.getTasks();
        for (TaskDetail task : tasks) {
            tasksMap.put(task.id, task);
        }
    }

    /**
     *
     * @param addr
     * @param factory
     * @throws Exception
     */
    public ClientWrapper(ClientAddress addr, ClientFactory factory) throws Exception {
        tasksObservableList = FXCollections.observableArrayList();
        tasksMap = new HashMap<>();

        clientAddress = addr;
        client = factory.create(addr);
        isPaused = client.isPaused();
    }

    /**
     *
     * @return
     */
    public ClientAddress getClientAddress() {
        return clientAddress;
    }

    /**
     *
     * @param taskId
     * @return
     */
    public TaskDetail getTaskDetail(String taskId) {
        return tasksMap.get(taskId);
    }

    /**
     *
     * @param task
     * @throws Exception
     */
    public void addTask(TaskMetadata task) throws Exception {
        if (task == null) {
            return;
        }

        client.addTask(task);
        fetchTasks(true);
    }

    /**
     *
     * @param taskId
     * @throws Exception
     */
    public void deleteTask(String taskId) throws Exception {
        TaskDetail task = tasksMap.get(taskId);
        if (task == null) {
            return;
        }


        client.deleteTask(task);
        fetchTasks(true);
    }

    /**
     *
     */
    public void disconnect() {
        // TODO: actually disconnect from client
    }

    /**
     *
     * @throws Exception
     */
    public void shutdown() throws Exception {
        client.shutdown();
    }

    /**
     *
     * @return
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     *
     * @return
     */
    public boolean isListOpened() {
        return isListOpened;
    }

    /**
     *
     * @throws Exception
     */
    public void openTaskList() throws Exception {
        fetchTasks(false);
        isListOpened = true;
    }

    /**
     *
     */
    public void closeTaskList() {
        isListOpened = false;
    }

    /**
     * Fills ObservableList of tasks with internal task list.
     * This function has to be called after every change of tasks list.
     * @note Has to be used in JavaFX UI thread.
     */
    public void fillTaskObservableList() {
        tasksObservableList.clear();
        for (Entry<String, TaskDetail> entry : tasksMap.entrySet()) {
            tasksObservableList.add(entry.getValue());
        }
    }

    /**
     *
     * @throws Exception
     */
    public void refreshTasks() throws Exception {
        fetchTasks(true);
    }

    /**
     *
     * @throws Exception
     */
    public void pause() throws Exception {
        isPaused = true;
        client.pause();
    }

    /**
     *
     * @throws Exception
     */
    public void unpause() throws Exception {
        isPaused = false;
        client.unpause();
    }

    /**
     * 
     * @throws Exception
     */
    public void saveToCrontab() throws Exception {
        client.saveToCrontab();
    }
}
