package jjcron;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class TaskManager {

    private static final Logger logger = Logger.getLogger(TaskManager.class.getName());

    private final ScheduledExecutorService scheduler;

    public TaskManager() {
        scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void justWait() {
        while (true) {
            try {
                if (scheduler.awaitTermination(1, TimeUnit.DAYS)) {
                    break;
                }
            } catch (Exception e) {}
        }
    }

    public void reload(List<TaskMetadata> tasks) throws Exception {
        scheduler.shutdownNow();
    }
}
