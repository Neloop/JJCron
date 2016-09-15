package cz.polankam.jjcron.remote.manager;

import cz.polankam.jjcron.common.TaskMetadata;
import cz.polankam.jjcron.remote.Client;
import cz.polankam.jjcron.remote.TaskDetail;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Wrapper for client which should implement all client methods, also it serves
 * as holder for all kinds of clients tasks collections. Wrapper is used instead
 * of {@link Client} iself because there can be some internal logic including
 * caching or any other mechanism.
 *
 * @author Neloop
 */
public class ClientWrapper {

    /**
     * Observable list of tasks which can be used in UI and is automatically
     * updated.
     */
    public final ObservableList<TaskDetail> tasksObservableList;
    /**
     * Uniquely indexed task details collection.
     */
    private final Map<String, TaskDetail> tasksMap;

    /**
     * Address of this particular client.
     */
    private final ClientAddress clientAddress;
    /**
     * Client representation itself.
     */
    private final Client client;
    /**
     * Factory which should connect and disconnect client instances.
     */
    private final ClientFactory clientFactory;

    /**
     * Caching mechanism for tasks, if true tasks were fetched from client
     * instance and does not have to be fetched again.
     */
    private boolean isTaskListFetched = false;
    /**
     * If true than task list of this client should be opened in UI.
     */
    private boolean isListOpened = false;
    /**
     * If true than client instance is paused and does not have any scheduled
     * tasks.
     */
    private boolean isPaused = false;

    /**
     * Try to fetch tasks from client, if tasks are already fetched and force
     * parameter is false than nothing is done. If force parameter is true than
     * tasks are always fetched on this call.
     *
     * @param force true if tasks has to be fetched
     * @throws Exception in case of any error
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
     * Construction which should receive all needed information. Client address
     * and client factory cannot be null.
     *
     * @param addr address of client instance
     * @param factory factory which should connect and return connected client
     * @throws Exception in case of any error
     */
    public ClientWrapper(ClientAddress addr, ClientFactory factory)
            throws Exception {
        if (addr == null) {
            throw new ManagerException("ClientAddress cannot be null");
        }
        if (factory == null) {
            throw new ManagerException("ClientFactory cannot be null");
        }

        tasksObservableList = FXCollections.observableArrayList();
        tasksMap = new HashMap<>();

        clientAddress = addr;
        client = factory.connect(addr);
        clientFactory = factory;
        isPaused = client.isPaused();
    }

    /**
     * Gets address of wrapped client.
     *
     * @return structure which holds all information about client instance
     * address
     */
    public ClientAddress getClientAddress() {
        return clientAddress;
    }

    /**
     * Gets details about task with given identification.
     *
     * @param id unique identification of task
     * @return structure with all possible task information
     */
    public TaskDetail getTaskDetail(String id) {
        return tasksMap.get(id);
    }

    /**
     * Adds task to client instance, if task is null then nothing will happen.
     *
     * @param task metadata which should contain all needed information for task
     * creation
     * @throws Exception in case of any error
     */
    public void addTask(TaskMetadata task) throws Exception {
        if (task == null) {
            return;
        }

        client.addTask(task);
        fetchTasks(true);
    }

    /**
     * Deletes task from client instance, if task with given ID does not exist
     * then nothing will happen.
     *
     * @param id unique identification of task
     * @throws Exception in case of any error
     */
    public void deleteTask(String id) throws Exception {
        TaskDetail task = tasksMap.get(id);
        if (task == null) {
            return;
        }

        client.deleteTask(task);
        fetchTasks(true);
    }

    /**
     * Disconnect manager from remote instance (but does not shutdown it).
     *
     * @throws Exception in case of any error
     */
    public void disconnect() throws Exception {
        clientFactory.disconnect(client);
    }

    /**
     * Shutdown connected client instance and disconnect it.
     *
     * @throws Exception in case of any error
     */
    public void shutdown() throws Exception {
        client.shutdown();
    }

    /**
     * Determines if client is paused or not.
     *
     * @return true if client is paused, false otherwise
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Determines if task list in UI is opened on this client or not.
     *
     * @return true if task list is opened, false otherwise
     */
    public boolean isListOpened() {
        return isListOpened;
    }

    /**
     * Try to fetch task if not cached and log that task list is opened.
     *
     * @throws Exception in case of any error
     */
    public void openTaskList() throws Exception {
        fetchTasks(false);
        isListOpened = true;
    }

    /**
     * Tell client wrapper that task list on this client was closed.
     */
    public void closeTaskList() {
        isListOpened = false;
    }

    /**
     * Fills ObservableList of tasks with internal task list. This function has
     * to be called after every change of tasks list.
     * <p>
     * Has to be used in JavaFX UI thread.</p>
     */
    public void fillTaskObservableList() {
        tasksObservableList.clear();
        for (Entry<String, TaskDetail> entry : tasksMap.entrySet()) {
            tasksObservableList.add(entry.getValue());
        }
    }

    /**
     * Force fetch tasks from remote client.
     *
     * @throws Exception in case of any error
     */
    public void refreshTasks() throws Exception {
        fetchTasks(true);
    }

    /**
     * Pause all execution on remote client.
     *
     * @throws Exception in case of any error
     */
    public void pause() throws Exception {
        isPaused = true;
        client.pause();
    }

    /**
     * Unpause and starts all execution on remote client.
     *
     * @throws Exception in case of any error
     */
    public void unpause() throws Exception {
        isPaused = false;
        client.unpause();
    }

    /**
     * Reload tasks from initialy loaded crontab file.
     *
     * @throws Exception in case of any error
     */
    public void reloadCrontab() throws Exception {
        client.reloadCrontab();
        fetchTasks(true);
    }

    /**
     * Save current state of tasks into original crontab file.
     *
     * @throws Exception in case of any error
     */
    public void saveToCrontab() throws Exception {
        client.saveToCrontab();
    }
}
