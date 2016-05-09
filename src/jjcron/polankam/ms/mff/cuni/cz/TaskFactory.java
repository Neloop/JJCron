package jjcron.polankam.ms.mff.cuni.cz;

/**
 *
 * @author Neloop
 */
public interface TaskFactory {
    public TaskBase createTask(TaskMetadata taskMeta) throws TaskException;
}
