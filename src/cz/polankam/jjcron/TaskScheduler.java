package cz.polankam.jjcron;

import cz.polankam.jjcron.common.TaskMetadata;
import cz.polankam.jjcron.common.TaskStats;
import cz.polankam.jjcron.remote.TaskDetail;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class which is responsible for creation of tasks and their scheduling. Pool
 * of tasks is maintaned and work with here, all tasks can be stopped through
 * api or new set of tasks can be realoaded. Parsing of tasks from crontab has
 * to be done elsewhere only task meta info is processed here.
 * <p>
 * After construction thread-safe structure.</p>
 *
 * @author Neloop
 */
public final class TaskScheduler {

    /**
     * Sleep interval which is used for checking on task pool termination.
     */
    private static final int SLEEP_INTERVAL = 1;
    /**
     * Standard Java logger.
     */
    private static final Logger logger
            = Logger.getLogger(TaskScheduler.class.getName());

    /**
     * If set to true, than {@link #start(List)} function was called and tasks
     * are running.
     */
    private final AtomicBoolean running;
    /**
     * If set to true, then termination of task pool was requested.
     */
    private final AtomicBoolean exit;
    /**
     * Responsible for task scheduling. Its initialized with processor count
     * equal to real CPU processors (including HTT).
     */
    private ScheduledExecutorService scheduler;
    /**
     * Task pool which is managed by this class instance.
     */
    private final Map<String, TaskHolder> tasks;
    /**
     * Helps with construction of proper Task children objects.
     */
    private final TaskFactory taskFactory;

    /**
     * Empty task factory which is used in case of unnecessary task factory.
     * Implemented due to gentle way of telling null dereference.
     */
    private class NullTaskFactory implements TaskFactory {

        /**
         * Not implemented version of method which should not be used.
         *
         * @param taskMeta none
         * @return none
         * @throws TaskException always thrown due to not implemented
         * functionality
         */
        @Override
        public Task createTask(TaskMetadata taskMeta) throws TaskException {
            throw new TaskException("TaskFactory cannot be used");
        }

    }

    /**
     * Construct task scheduler with specified task factory, tasks are not
     * executed yet. All internal structures are initialized.
     *
     * @param taskFactory factory which helps constructing tasks, can be null
     * @throws TaskException if {@link TaskFactory} was null
     */
    public TaskScheduler(TaskFactory taskFactory) throws TaskException {
        logger.log(Level.INFO, "TaskManager was created");

        this.exit = new AtomicBoolean(false);
        this.running = new AtomicBoolean(false);
        this.tasks = new HashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(
                Runtime.getRuntime().availableProcessors());

        if (taskFactory == null) {
            taskFactory = new NullTaskFactory();
        }
        this.taskFactory = taskFactory;
    }

    /**
     * Wait until tasks execution is terminated.
     * <p>
     * Thread-safe function.</p>
     */
    public final void justWait() {
        try {
            while (!exit.get()) {
                TimeUnit.SECONDS.sleep(SLEEP_INTERVAL);
            }
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Waiting thread was interrupted.");
        }
    }

    /**
     * Atomically sets termination of tasks execution and shutdown all current
     * executing tasks.
     * <p>
     * Thread-safe function.</p>
     */
    public final void exit() {
        logger.log(Level.INFO, "Exit function was called."
                + " All scheduled tasks should be terminated");

        scheduler.shutdownNow();
        exit.set(true);
    }

    /**
     * Helper class which allows to run task and reschedule it after execution.
     */
    private class RunTask implements Runnable {

        /**
         * {@link Task} associated with this object.
         */
        private final TaskHolder taskHolder;

        /**
         * Given task is stored and time structure is extracted from it.
         *
         * @param task {@link TaskHolder} which will be executed in this object.
         */
        public RunTask(TaskHolder task) {
            this.taskHolder = task;
        }

        /**
         * Run method which is executed by scheduler. Task is executed inside
         * and after execution is rescheduled to next timepoint.
         */
        @Override
        public void run() {
            // run task itself
            try {
                long start = System.nanoTime();
                taskHolder.task.run();
                long end = System.nanoTime();
                taskHolder.stats.record(LocalDateTime.now(), end - start);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Task: {0} throws exception"
                        + " while execution: {1}",
                        new Object[]{taskHolder.task.name(), e.getMessage()});
            }

            // ... and reschedule task to another time point
            scheduleTask(taskHolder);
        }
    }

    /**
     * Schedule given task to its first execution point.
     * <p>
     * Thread-safe function.</p>
     *
     * @param holder task which will be scheduled
     */
    private synchronized void scheduleTask(TaskHolder holder) {
        running.set(true);

        long delay = holder.task.delay(LocalDateTime.now());
        logger.log(Level.INFO, "Task {0} was scheduled to {1}",
                new Object[]{holder.task.name(),
                    LocalDateTime.now().plusSeconds(
                            holder.task.timeUnit().toSeconds(delay))});
        scheduler.schedule(new RunTask(holder), delay, holder.task.timeUnit());
    }

    /**
     * Create task from given <code>taskMeta</code> information and schedule it
     * to its frist time point.
     * <p>
     * Thread-safe function.</p>
     *
     * @param taskMeta information about task
     * @throws TaskException if task creation failed
     */
    private synchronized void loadTask(TaskMetadata taskMeta)
            throws TaskException {
        String id = UUID.randomUUID().toString();
        Task task = taskFactory.createTask(taskMeta);
        TaskHolder holder = new TaskHolder(id, task, new TaskStats());
        tasks.put(id, holder);

        // schedule first execution
        scheduleTask(holder);
    }

    /**
     * From {@link TaskMetadata} list create appropriate task and schedule them.
     * Tasks are scheduled for next execution timepoint, after execution they
     * are rescheduled.
     * <p>
     * Thread-safe function.</p>
     *
     * @param tasksMeta list of task meta information
     * @throws TaskException if task creation failed
     */
    private synchronized void loadTasks(List<TaskMetadata> tasksMeta)
            throws TaskException {
        for (TaskMetadata taskMeta : tasksMeta) {
            loadTask(taskMeta);
        }
    }

    /**
     * Create task and schedule it, can be used to start croning or during
     * execution.
     * <p>
     * Thread-safe function.</p>
     *
     * @param taskMeta information about task
     * @throws TaskException if task creation failed
     */
    public final synchronized void addTask(TaskMetadata taskMeta)
            throws TaskException {
        loadTask(taskMeta);
    }

    /**
     * Add given task to currently executing ones, can be used to start croning
     * or during execution.
     * <p>
     * Thread-safe function.</p>
     *
     * @param task task which will be added to internal ones
     */
    public final synchronized void addTask(Task task) {
        String id = UUID.randomUUID().toString();
        TaskHolder holder = new TaskHolder(id, task, new TaskStats());
        tasks.put(id, holder);
        scheduleTask(holder);
    }

    /**
     * Deletes tasks according to given unique identification. There is no way
     * how to stop scheduled task, so this function will stop whole execution
     * and after deletion starts it again.
     * <p>
     * Thread-safe function.</p>
     *
     * @param id unique identification of task
     */
    public final synchronized void deleteTask(String id) {
        scheduler.shutdownNow();
        scheduler = Executors.newScheduledThreadPool(
                Runtime.getRuntime().availableProcessors());

        // actual deletion
        tasks.remove(id);

        for (Entry<String, TaskHolder> entry : tasks.entrySet()) {
            scheduleTask(entry.getValue());
        }
    }

    /**
     * From given {@link TaskMetadata} list constructs all tasks and schedule
     * them to their first execution timepoint. Non-blocking function tasks are
     * only created and scheduled. If called second time, nothing will happen.
     * <p>
     * Thread-safe function.</p>
     *
     * @param tasksMeta list of task meta information
     * @throws TaskException if task creation failed
     */
    public final synchronized void start(List<TaskMetadata> tasksMeta)
            throws TaskException {
        if (running.get() == false) {
            loadTasks(tasksMeta);
            running.set(true);
        }
    }

    /**
     * Reschedules all currently loaded tasks. Should be used only as
     * counterpart of {@link #stop()} function. Cannot be used as restart
     * running state is checked before any actions.
     * <p>
     * Thread-safe function.</p>
     */
    public final synchronized void start() {
        if (running.get() == true) {
            return;
        }

        running.set(true);
        scheduler = Executors.newScheduledThreadPool(
                Runtime.getRuntime().availableProcessors());

        for (Entry<String, TaskHolder> entry : tasks.entrySet()) {
            scheduleTask(entry.getValue());
        }
    }

    /**
     * Stop all currenty executing tasks. It is counterpart of {@link #start()}
     * function. Multiple calls have no effect.
     * <p>
     * Thread-safe function.</p>
     */
    public final synchronized void stop() {
        if (running.get() == false) {
            return;
        }

        running.set(false);
        scheduler.shutdownNow();
        logger.log(Level.INFO, "Stop was requested, all task are stopped now");
    }

    /**
     * Function stop all currently running tasks and load new ones. Should be
     * used only during execution, not to start execution of cron.
     * <p>
     * Thread-safe function.</p>
     *
     * @param tasksMeta list of task meta information
     * @throws TaskException if task creation failed
     */
    public final synchronized void reloadTasks(List<TaskMetadata> tasksMeta)
            throws TaskException {
        logger.log(Level.INFO, "Task reload requested...");

        scheduler.shutdownNow();
        scheduler = Executors.newScheduledThreadPool(
                Runtime.getRuntime().availableProcessors());
        tasks.clear();

        loadTasks(tasksMeta);

        logger.log(Level.INFO, "Task reload done");
    }

    /**
     * Determines if client is running or not.
     * <p>
     * Thread-safe function.</p>
     *
     * @return true if client is running, false otherwise
     */
    public final boolean isRunning() {
        return running.get();
    }

    /**
     * Gets list of {@link TaskMetadata} structures from currently
     * active/scheduled cron tasks.
     *
     * @return list of {@link TaskMetadata} structures
     */
    public final synchronized List<TaskMetadata> getTaskMetadatas() {
        List<TaskMetadata> result = new ArrayList<>();
        for (Entry<String, TaskHolder> entry : tasks.entrySet()) {
            result.add(entry.getValue().task.metadata());
        }
        return result;
    }

    /**
     * Gets list of {@link TaskDetail} structures which represents currently
     * scheduled cron tasks.
     *
     * @return list of {@link TaskDetail} structures
     */
    public final synchronized List<TaskDetail> getTaskDetails() {
        List<TaskDetail> result = new ArrayList<>();
        for (Entry<String, TaskHolder> entry : tasks.entrySet()) {
            TaskHolder holder = entry.getValue();
            Task task = holder.task;
            LocalDateTime next = LocalDateTime.now();
            next = next.plusSeconds(task.timeUnit().toSeconds(task.delay(next)));
            result.add(new TaskDetail(holder.id, task.name(), next,
                    holder.stats, task.metadata()));
        }
        return result;
    }
}
