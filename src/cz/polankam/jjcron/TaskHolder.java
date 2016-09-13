package cz.polankam.jjcron;

import cz.polankam.jjcron.common.TaskStats;

/**
 *
 * @author Neloop
 */
public class TaskHolder {

    public final String id;
    public final Task task;
    public final TaskStats stats;

    public TaskHolder(String id, Task task, TaskStats stats) {
        this.id = id;
        this.task = task;
        this.stats = stats;
    }
}
