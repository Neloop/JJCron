package jjcron.polankam.ms.mff.cuni.cz;

/**
 * Interface providing factory method for task creation.
 * @author Neloop
 */
public interface TaskFactory {
    /**
     * From given {@link TaskMetadata} create appropriate {@link Task} object.
     * Task can be anything which extends {@link Task} abstraction.
     * @param taskMeta information needed for {@link Task} creation
     * @return newly created task
     * @throws TaskException if task creation failed
     */
    public Task createTask(TaskMetadata taskMeta) throws TaskException;
}
