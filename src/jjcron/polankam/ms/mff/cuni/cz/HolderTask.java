package jjcron.polankam.ms.mff.cuni.cz;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * HolderTask should be used as holder for additional datas about task.
 * Its advised to extends this abstract class in all possible tasks.
 * HolderTask is abstract because of providing basic storage
 *   for {@link TaskMetadata}.
 * @author Neloop
 */
public abstract class HolderTask implements Task {

    /**
     * Stores all possible information about {@link HolderTask}.
     */
    protected final TaskMetadata taskMeta;

    /**
     * Structure which contains information about task
     *   and is accessible through all {@link Task} children.
     * @param taskMetadata task information
     */
    public HolderTask(TaskMetadata taskMetadata) {
        this.taskMeta = taskMetadata;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String name() {
        return taskMeta.command();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long delay(LocalDateTime localNow) {
        return taskMeta.time().delay(localNow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final TimeUnit timeUnit() {
        return taskMeta.time().timeUnit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void run() throws Exception;
}
