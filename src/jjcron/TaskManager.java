package jjcron;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class TaskManager {

    private static final int SLEEP_INTERVAL = 1;
    private static final Logger logger = Logger.getLogger(TaskManager.class.getName());

    private final AtomicBoolean exit;
    private ScheduledExecutorService scheduler;
    private final List<TaskBase> tasks;
    private final TaskFactory taskFactory;

    public TaskManager(TaskFactory taskFactory) {
        this.exit = new AtomicBoolean(false);
        this.tasks = new ArrayList<>();
        this.scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        this.taskFactory = taskFactory;
    }

    public void justWait() {
        try {
            while (!exit.get()) {
                TimeUnit.SECONDS.sleep(SLEEP_INTERVAL);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void exit() {
        exit.set(true);
    }

    private synchronized void loadTasks(List<TaskMetadata> tasksMeta) throws Exception {
        for (TaskMetadata taskMeta : tasksMeta) {
            TaskBase task = taskFactory.createTask(taskMeta);
            CrontabTime time = task.getTime();
            tasks.add(task);

            // run task
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    task.run();
                } catch (Exception e) {}
            }, time.initialDelay(), time.period(), time.timeUnit());
        }
    }

    public synchronized void startCroning(List<TaskMetadata> tasksMeta) throws Exception {
        loadTasks(tasksMeta);
    }

    public synchronized void reloadTasks(List<TaskMetadata> tasksMeta) throws Exception {
        scheduler.shutdownNow();
        scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        tasks.clear();

        loadTasks(tasksMeta);
    }
}
