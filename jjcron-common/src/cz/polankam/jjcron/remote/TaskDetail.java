package cz.polankam.jjcron.remote;

import cz.polankam.jjcron.common.TaskMetadata;
import cz.polankam.jjcron.common.TaskStats;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Used as task data holder which is supposed to be distributed through remote
 * connection.
 *
 * @author Neloop
 */
public class TaskDetail implements Serializable {

    /**
     * Unique identification of task.
     */
    public final String id;

    /**
     * Human readable task name.
     */
    public final String name;
    /**
     * Time of next execution of this task.
     */
    public final LocalDateTime nextExecutionTime;

    /**
     * Statistic data which belongs to this task.
     */
    public final TaskStats stats;
    /**
     * Task metadata which hold everything needed for scheduling and execution.
     */
    public final TaskMetadata metadata;

    /**
     * Classical holder constructor with all stored values.
     *
     * @param id unique task identification
     * @param name human readable task name
     * @param next next execution timepoint
     * @param stats task statistics
     * @param metadata task metadata
     */
    public TaskDetail(String id, String name, LocalDateTime next,
            TaskStats stats, TaskMetadata metadata) {
        this.id = id;
        this.name = name;
        this.nextExecutionTime = next;
        this.stats = stats;
        this.metadata = metadata;
    }
}
