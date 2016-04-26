package jjcron;

/**
 *
 * @author Martin
 */
public abstract class TaskBase {

    private final TaskMetadata taskMeta;

    public TaskBase(TaskMetadata taskMetadata) {
        this.taskMeta = taskMetadata;
    }

    public CrontabTime getTime() {
        return taskMeta.time();
    }

    public abstract void run();
}
