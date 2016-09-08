package cz.cuni.mff.ms.polankam.jjcron.remote;

import cz.cuni.mff.ms.polankam.jjcron.common.TaskMetadata;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Neloop
 */
public class FakeClient implements Client {

    private boolean paused = false;
    private final Map<String, TaskDetail> tasks;

    public FakeClient() {
        tasks = new HashMap<>();
        tasks.put("first", new TaskDetail("first"));
        tasks.put("second", new TaskDetail("second"));
        tasks.put("third", new TaskDetail("third"));
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void unpause() {
        paused = false;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public List<TaskDetail> getTasks() {
        List<TaskDetail> tasksList = new ArrayList<>();
        for (Entry<String, TaskDetail> task : tasks.entrySet()) {
            tasksList.add(task.getValue());
        }
        return tasksList;
    }

    @Override
    public void deleteTask(TaskDetail task) {
        tasks.remove(task.getId());
    }

    @Override
    public void addTask(TaskMetadata task) {
        tasks.put(task.command(), new TaskDetail(task.command()));
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void saveToCrontab() {
    }
}
