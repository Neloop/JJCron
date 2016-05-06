package jjcron;

import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public abstract class TaskBase {

    private static final Logger logger = Logger.getLogger(TaskBase.class.getName());

    protected final TaskMetadata taskMeta;

    public TaskBase(TaskMetadata taskMetadata) {
        this.taskMeta = taskMetadata;
    }

    public final CrontabTime getTime() {
        return taskMeta.time();
    }

    public abstract void run() throws Exception;
}
