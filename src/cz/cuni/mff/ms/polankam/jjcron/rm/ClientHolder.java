package cz.cuni.mff.ms.polankam.jjcron.rm;

import cz.cuni.mff.ms.polankam.jjcron.common.Client;
import cz.cuni.mff.ms.polankam.jjcron.common.TaskDetail;
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
public class ClientHolder {
    public final ObservableList<String> tasksObservableList;
    private final Map<String, TaskDetail> tasksMap;

    private final ClientAddress clientAddress;
    private final ClientFactory clientFactory;
    private final Client client;

    private boolean isTaskListFetched = false;
    private boolean isListOpened = false;

    private void fetchTasks(boolean force) {
        if (!force && isTaskListFetched) {
            return;
        }

        tasksMap.clear();
        isTaskListFetched = true;

        List<TaskDetail> tasks = client.getTasks();
        for (TaskDetail task : tasks) {
            tasksMap.put(task.getId(), task);
        }
    }

    public ClientHolder(ClientAddress addr, ClientFactory factory) {
        tasksObservableList = FXCollections.observableArrayList();
        tasksMap = new HashMap<>();

        clientAddress = addr;
        clientFactory = factory;
        client = factory.create(addr);
    }

    public ClientAddress getClientAddress() {
        return clientAddress;
    }

    public TaskDetail getTaskDetail(String taskId) {
        return tasksMap.get(taskId);
    }

    public void addTask(TaskDetail task) {
        if (task == null) {
            return;
        }

        client.addTask(task);
        fetchTasks(true);
    }

    public void deleteTask(String taskId) {
        TaskDetail task = tasksMap.get(taskId);
        if (task == null) {
            return;
        }


        client.deleteTask(task);
        fetchTasks(true);
    }

    public void disconnect() {
        // TODO: actually disconnect from client
    }

    public void shutdown() {
        client.shutdown();
    }

    public boolean isPaused() {
        return client.isPaused();
    }

    public boolean isListOpened() {
        return isListOpened;
    }

    public void openTaskList() {
        fetchTasks(false);
        isListOpened = true;
    }

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
            tasksObservableList.add(entry.getKey());
        }
    }

    public void refreshTasks() {
        fetchTasks(true);
    }

    public void pause() {
        client.pause();
    }

    public void unpause() {
        client.unpause();
    }
}
