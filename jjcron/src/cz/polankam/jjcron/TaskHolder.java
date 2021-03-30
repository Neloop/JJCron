package cz.polankam.jjcron;

import cz.polankam.jjcron.common.TaskStats;

/**
 * Simple holder structure for task information which is used by
 * {@link TaskScheduler} class.
 *
 * @author Neloop
 */
public class TaskHolder {

    /**
     * Unique identification of task on this cron instance.
     */
    public final String id;

    /**
     * Task itself.
     */
    public final Task task;

    /**
     * Bit of statistics about this task execution.
     */
    public final TaskStats stats;

    /**
     * Classical constructor with all needed variables.
     *
     * @param id unique identification of given task
     * @param task task itself
     * @param stats statistic data of task
     * @throws TaskException if any of parameters was null
     */
    public TaskHolder(String id, Task task, TaskStats stats)
            throws TaskException {
        if (id == null) {
            throw new TaskException("Identification of task cannot be null");
        }
        if (task == null) {
            throw new TaskException("Task cannot be null");
        }
        if (stats == null) {
            throw new TaskException("TaskStats cannot be null");
        }

        this.id = id;
        this.task = task;
        this.stats = stats;
    }
}
