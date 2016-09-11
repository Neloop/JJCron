package cz.cuni.mff.ms.polankam.jjcron.common;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author Neloop
 */
public class TaskStats {
    private static final int HISTORY_LENGTH = 10;

    private final Deque<LocalDateTime> lastExecutions;
    private final Deque<Long> lastExecutionsDuration;

    public TaskStats() {
        lastExecutions = new ArrayDeque<>();
        lastExecutionsDuration = new ArrayDeque<>();
    }

    public void record(LocalDateTime time, long duration) {
        lastExecutions.add(time);
        lastExecutionsDuration.add(duration);

        if (lastExecutions.size() > HISTORY_LENGTH) {
            lastExecutions.pollLast();
            lastExecutionsDuration.pollLast();
        }
    }

    public LocalDateTime getLastExecution() {
        return lastExecutions.peekFirst();
    }

    public Long getLastDuration() {
        return lastExecutionsDuration.peekFirst();
    }
}
