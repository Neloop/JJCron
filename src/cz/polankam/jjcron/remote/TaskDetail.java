package cz.polankam.jjcron.remote;

import cz.polankam.jjcron.common.TaskMetadata;
import cz.polankam.jjcron.common.TaskStats;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Used as data holder which is supposed to be distributed through connection.
 *
 * @author Neloop
 */
public class TaskDetail implements Serializable {

    /**
     *
     */
    public final String id;

    /**
     *
     */
    public final String name;
    /**
     *
     */
    public final TimeUnit timeUnit;
    /**
     *
     */
    public final LocalDateTime nextExecutionTime;

    /**
     *
     */
    public final TaskStats stats;
    /**
     *
     */
    public final TaskMetadata metadata;

    /**
     *
     * @param id
     * @param name
     * @param timeUnit
     * @param next
     * @param stats
     * @param metadata
     */
    public TaskDetail(String id, String name, TimeUnit timeUnit,
            LocalDateTime next, TaskStats stats, TaskMetadata metadata) {
        this.id = id;
        this.name = name;
        this.timeUnit = timeUnit;
        this.nextExecutionTime = next;
        this.stats = stats;
        this.metadata = metadata;
    }
}
