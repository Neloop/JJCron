package jjcron.polankam.ms.mff.cuni.cz;

/**
 * Every task which will be scheduled within JJCron
 *   has to extends this abstract class.
 * Task is abstract because of providing basic storage for {@link TaskMetadata}.
 * @author Neloop
 */
public abstract class Task {

    /**
     * Stores all possible information about {@link Task}.
     */
    protected final TaskMetadata taskMeta;

    /**
     * Structure which contains information about task
     *   and is accessible through all {@link Task} children.
     * @param taskMetadata task information
     */
    public Task(TaskMetadata taskMetadata) {
        this.taskMeta = taskMetadata;
    }

    /**
     * Returns this task name.
     * @return textual representation of command name (usually command name)
     */
    public final String name() {
        return taskMeta.command();
    }

    /**
     * Returns internal {@link CrontabTime} structure
     *   which is used for scheduling.
     * @return information about task time and scheduling
     */
    public final CrontabTime time() {
        return taskMeta.time();
    }

    /**
     * Execute task job, should be implemented in {@link Task} children.
     * @throws Exception if task execution failed
     */
    public abstract void run() throws Exception;
}
