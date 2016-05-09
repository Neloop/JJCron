package jjcron.polankam.ms.mff.cuni.cz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 *
 * @author Neloop
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

    public final void justWait() {
        try {
            while (!exit.get()) {
                TimeUnit.SECONDS.sleep(SLEEP_INTERVAL);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public final void exit() {
        exit.set(true);
    }

    private class RunTask implements Runnable {
        private final CrontabTime time;
        private final TaskBase task;

        public RunTask(TaskBase task) {
            this.task = task;
            this.time = task.getTime();
        }

        @Override
        public void run() {
            // run task itself
            try {
                task.run();
            } catch (Exception e) {}

            // ... and reschedule task to another time point
            scheduler.schedule(new RunTask(task), time.delay(), time.timeUnit());
        }
    }

    private synchronized void loadTasks(List<TaskMetadata> tasksMeta) throws TaskException {
        for (TaskMetadata taskMeta : tasksMeta) {
            TaskBase task = taskFactory.createTask(taskMeta);
            CrontabTime time = task.getTime();
            tasks.add(task);

            // schedule first execution
            scheduler.schedule(new RunTask(task), time.delay(), time.timeUnit());
        }
    }

    public final synchronized void startCroning(List<TaskMetadata> tasksMeta) throws TaskException {
        loadTasks(tasksMeta);
    }

    public final synchronized void reloadTasks(List<TaskMetadata> tasksMeta) throws TaskException {
        scheduler.shutdownNow();
        scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        tasks.clear();

        loadTasks(tasksMeta);
    }
}
