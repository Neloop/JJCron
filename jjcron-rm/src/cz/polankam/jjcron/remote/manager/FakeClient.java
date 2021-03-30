package cz.polankam.jjcron.remote.manager;

import cz.polankam.jjcron.common.CrontabTime;
import cz.polankam.jjcron.common.TaskMetadata;
import cz.polankam.jjcron.common.TaskStats;
import cz.polankam.jjcron.remote.Client;
import cz.polankam.jjcron.remote.TaskDetail;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * Imitates behaviour of remote cron client.
 * <p>
 * This class is only for debugging purposes.</p>
 *
 * @author Neloop
 */
public class FakeClient implements Client {

    /**
     * Count of tasks which will be constructed on initialization.
     */
    private static final int TASKS_COUNT = 10;
    /**
     * Latency which is used on every command request.
     */
    private static final long LATENCY = 500;

    /**
     * True if client is paused, false otherwise.
     */
    private boolean paused = false;
    /**
     * Collection of tasks with their identifiers.
     */
    private final Map<String, TaskDetail> tasks;

    /**
     * Construct fake client and initialize some default tasks.
     *
     * @throws Exception in case of any error
     */
    public FakeClient() throws Exception {
        tasks = new HashMap<>();

        for (int i = 0; i < TASKS_COUNT; ++i) {
            String id = UUID.randomUUID().toString();
            CrontabTime time = new CrontabTime("0", "0", "0", "*", "*", "*");
            tasks.put(id, new TaskDetail(id, id, LocalDateTime.now(),
                    new TaskStats(), new TaskMetadata(time, id)));
        }
    }

    @Override
    public void pause() throws Exception {
        Thread.sleep(LATENCY);
        paused = true;
    }

    @Override
    public void unpause() throws Exception {
        Thread.sleep(LATENCY);
        paused = false;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public List<TaskDetail> getTasks() throws Exception {
        Thread.sleep(LATENCY);
        List<TaskDetail> tasksList = new ArrayList<>();
        for (Entry<String, TaskDetail> task : tasks.entrySet()) {
            tasksList.add(task.getValue());
        }
        return tasksList;
    }

    @Override
    public void deleteTask(TaskDetail task) throws Exception {
        Thread.sleep(LATENCY);
        tasks.remove(task.id);
    }

    @Override
    public void addTask(TaskMetadata task) throws Exception {
        Thread.sleep(LATENCY);
        String id = UUID.randomUUID().toString();
        CrontabTime time = task.time();
        LocalDateTime next = LocalDateTime.now();
        next.plusSeconds(time.delay(next));
        tasks.put(id, new TaskDetail(id, task.command(), next, new TaskStats(),
                new TaskMetadata(time, task.command())));
    }

    @Override
    public void shutdown() throws Exception {
        Thread.sleep(LATENCY);
    }

    @Override
    public void saveToCrontab() throws Exception {
        Thread.sleep(LATENCY);
    }

    @Override
    public void reloadCrontab() throws Exception {
        Thread.sleep(LATENCY);
    }
}
