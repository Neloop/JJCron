package jjcron;

/**
 *
 * @author Martin
 */
public interface TaskFactory {
    public TaskBase createTask(TaskMetadata taskMeta) throws TaskException;
}
