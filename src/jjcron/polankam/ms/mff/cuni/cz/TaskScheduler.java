package jjcron.polankam.ms.mff.cuni.cz;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class which is responsible for creation of tasks and their scheduling.
 * Pool of tasks is maintaned and work with here,
 *   all tasks can be stopped through api or new set of tasks can be realoaded.
 * Parsing of tasks from crontab has to be done elsewhere
 *   only task meta info is processed here.
 * <p>After construction thread-safe structure.</p>
 * @author Neloop
 */
public class TaskScheduler {

    /**
     * Sleep interval which is used for checking on task pool termination.
     */
    private static final int SLEEP_INTERVAL = 1;
    /**
     * Standard Java logger.
     */
    private static final Logger logger =
            Logger.getLogger(TaskScheduler.class.getName());

    /**
     * If set to true, than @ref startCronning function was called
     *   and tasks are running.
     */
    private final AtomicBoolean running;
    /**
     * If set to true, then termination of task pool was requested.
     */
    private final AtomicBoolean exit;
    /**
     * Responsible for task scheduling.
     * Its initialized with processor count equal to real CPU processors
     *   (including HTT).
     */
    private ScheduledExecutorService scheduler;
    /**
     * Task pool which is managed by this class instance.
     */
    private final List<Task> tasks;
    /**
     * Helps with construction of proper Task children objects.
     */
    private final TaskFactory taskFactory;

    /**
     * Construct task manager with specified task factory,
     *   tasks are not executed yet. All internal structures are initialized.
     * @param taskFactory factory which helps constructing tasks
     */
    public TaskScheduler(TaskFactory taskFactory) {
        logger.log(Level.INFO, "TaskManager was created");

        this.exit = new AtomicBoolean(false);
        this.running = new AtomicBoolean(false);
        this.tasks = new ArrayList<>();
        this.scheduler = Executors.newScheduledThreadPool(
                Runtime.getRuntime().availableProcessors());
        this.taskFactory = taskFactory;
    }

    /**
     * Wait until tasks execution is terminated.
     * <p>Thread-safe function.</p>
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
     * Atomically sets termination of tasks execution
     *   and shutdown all current executing tasks.
     * <p>Thread-safe function.</p>
     */
    public final void exit() {
        logger.log(Level.INFO, "Exit function was called." +
                " All scheduled tasks should be terminated");

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
        private final Task task;
        /**
         * Extracted time structure from {@link Task}.
         */
        private final CrontabTime time;

        /**
         * Given task is stored and time structure is extracted from it.
         * @param task {@link Task} which will be executed in this object.
         */
        public RunTask(Task task) {
            this.task = task;
            this.time = task.time();
        }

        /**
         * Run method which is executed by scheduler.
         * Task is executed inside and after execution is rescheduled
         *   to next timepoint.
         */
        @Override
        public void run() {
            // run task itself
            try {
                task.run();
            } catch (Exception e) {
                logger.log(Level.WARNING, "Task: {0} throws exception" +
                        " while execution: {1}",
                        new Object[] { task.name(), e.getMessage() } );
            }

            // ... and reschedule task to another time point
            scheduleTask(task);
        }
    }

    /**
     * Schedule given task to its first execution point.
     * @param task task which will be scheduled
     */
    private synchronized void scheduleTask(Task task) {
        running.set(true);

        long delay = task.time().delay();
        logger.log(Level.INFO, "Task {0} was scheduled to {1}",
                new Object[] { task.name(),
                    LocalDateTime.now().plusSeconds(delay) }); // TODO: try this using TemporalUnit
        scheduler.schedule(new RunTask(task), delay, task.time().timeUnit());
    }

    /**
     * Create task from given <code>taskMeta</code> information
     *   and schedule it to its frist time point.
     * @param taskMeta information about task
     * @throws TaskException if task creation failed
     */
    private synchronized void loadTask(TaskMetadata taskMeta)
            throws TaskException {
        Task task = taskFactory.createTask(taskMeta);
        tasks.add(task);

        // schedule first execution
        scheduleTask(task);
    }

    /**
     * From {@link TaskMetadata} list create appropriate task and schedule them.
     * Tasks are scheduled for next execution timepoint,
     *   after execution they are rescheduled.
     * <p>Thread-safe function.</p>
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
     * Create task and schedule it, can be used to start croning
     *   or during execution.
     * @param taskMeta information about task
     * @throws TaskException if task creation failed
     */
    public final synchronized void addTask(TaskMetadata taskMeta)
            throws TaskException {
        loadTask(taskMeta);
    }

    /**
     * Add given task to currently executing ones, can be used to start croning
     *   or during execution.
     * @param task task which will be added to internal ones
     */
    public final synchronized void addTask(Task task) {
        tasks.add(task);
        scheduleTask(task);
    }

    /**
     * From given {@link TaskMetadata} list constructs all tasks
     *   and schedule them to their first execution timepoint.
     * Non-blocking function tasks are only created and scheduled.
     * If called second time, nothing will happen.
     * <p>Thread-safe function.</p>
     * @param tasksMeta list of task meta information
     * @throws TaskException if task creation failed
     */
    public final synchronized void startCroning(List<TaskMetadata> tasksMeta)
            throws TaskException {
        if (running.get() == false) {
            loadTasks(tasksMeta);
            running.set(true);
        }
    }

    /**
     * Function stop all currently running tasks and load new ones.
     * Should be used only during execution, not to start execution of cron.
     * <p>Thread-safe function.</p>
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
}
