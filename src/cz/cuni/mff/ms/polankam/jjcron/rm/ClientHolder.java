package cz.cuni.mff.ms.polankam.jjcron.rm;

import cz.cuni.mff.ms.polankam.jjcron.common.Client;
import cz.cuni.mff.ms.polankam.jjcron.common.TaskDetail;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Neloop
 */
public class ClientHolder {
    private final ObservableList<String> tasksList;
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

        isTaskListFetched = true;
        tasksList.clear();

        List<TaskDetail> tasks = client.getTasks();
        for (TaskDetail task : tasks) {
            tasksList.add(task.getId());
            tasksMap.put(task.getId(), task);
        }
    }

    public ClientHolder(ClientAddress addr, ClientFactory factory) {
        tasksList = FXCollections.observableArrayList();
        tasksMap = new HashMap<>();

        clientAddress = addr;
        clientFactory = factory;
        client = factory.create(addr);
    }

    public ClientAddress getClientAddress() {
        return clientAddress;
    }

    public ObservableList<String> getTasksObservableList()
    {
        fetchTasks(false);
        return tasksList;
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
        tasksMap.remove(taskId);
        tasksList.remove(taskId);

        if (task != null) {
            client.deleteTask(task);
        }
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
        isListOpened = true;
    }

    public void closeTaskList() {
        isListOpened = false;
    }

    public void refreshTaskList() {
        fetchTasks(true);
    }

    public void pause() {
        client.pause();
    }

    public void unpause() {
        client.unpause();
    }
}
