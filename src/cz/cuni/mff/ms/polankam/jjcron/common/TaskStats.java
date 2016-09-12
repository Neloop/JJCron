package cz.cuni.mff.ms.polankam.jjcron.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author Neloop
 */
public class TaskStats implements Serializable {

    private static final int HISTORY_LENGTH = 10;

    /**
     *
     */
    private final Deque<LocalDateTime> lastExecutions;
    /**
     * Duration of last executions in nanoseconds.
     */
    private final Deque<Long> lastExecutionsDuration;

    /**
     *
     */
    public TaskStats() {
        lastExecutions = new ArrayDeque<>();
        lastExecutionsDuration = new ArrayDeque<>();
    }

    /**
     *
     * @param time
     * @param duration
     */
    public void record(LocalDateTime time, long duration) {
        lastExecutions.add(time);
        lastExecutionsDuration.add(duration);

        if (lastExecutions.size() > HISTORY_LENGTH) {
            lastExecutions.pollFirst();
            lastExecutionsDuration.pollFirst();
        }
    }

    /**
     *
     * @return
     */
    public LocalDateTime getLastExecution() {
        return lastExecutions.peekLast();
    }

    /**
     *
     * @return
     */
    public Long getLastDuration() {
        return lastExecutionsDuration.peekLast();
    }
}
