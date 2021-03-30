package cz.polankam.jjcron;

import cz.polankam.jjcron.common.TaskMetadata;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Every task which will be scheduled within JJCron has to extends this class.
 * Serves also as holder for {@link TaskMetadata} structure which has to be
 * provided on construction.
 *
 * @author Neloop
 */
public abstract class Task {

    /**
     * Metadata associated with this task.
     */
    protected final TaskMetadata taskMeta;

    /**
     * On construction {@link TaskMetadata} structure has to be provided.
     *
     * @param meta information about this task
     * @throws TaskException if metadata was null
     */
    public Task(TaskMetadata meta) throws TaskException {
        if (meta == null) {
            throw new TaskException("TaskMetadata cannot be null");
        }

        taskMeta = meta;
    }

    /**
     * Returns information about this task.
     *
     * @return structure with all needed information
     */
    public final TaskMetadata metadata() {
        return taskMeta;
    }

    /**
     * Returns this task name.
     *
     * @return textual representation of command name (usually command name)
     */
    public String name() {
        return taskMeta.command();
    }

    /**
     * Compute delay from <code>localNow</code> to next execution point of task.
     *
     * @param localNow current time point which will be used as base
     * @return number of units specified from timeUnit() function call
     */
    public long delay(LocalDateTime localNow) {
        return taskMeta.time().delay(localNow);
    }

    /**
     * Time unit in which delay is returned.
     *
     * @return time unit type
     */
    public TimeUnit timeUnit() {
        return taskMeta.time().timeUnit();
    }

    /**
     * Execute task job, should be implemented in {@link Task} implementations.
     *
     * @throws Exception if task execution failed
     */
    public abstract void run() throws Exception;
}
