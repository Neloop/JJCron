package cz.polankam.jjcron.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Collects bunch of statistics about some cron task. This mainly includes
 * execution times and execution duration. Only limited number of statistic data
 * from last executions is stored.
 *
 * @author Neloop
 */
public class TaskStats implements Serializable {

    /**
     * Defines max number of entries in statistics history containers.
     */
    private static final int HISTORY_LENGTH = 10;

    /**
     * Times of some count of last executions.
     */
    private final Deque<LocalDateTime> lastExecutions;
    /**
     * Duration of some count of last executions in nanoseconds.
     */
    private final Deque<Long> lastExecutionsDuration;

    /**
     * Classical parameterless contructor.
     */
    public TaskStats() {
        lastExecutions = new ArrayDeque<>();
        lastExecutionsDuration = new ArrayDeque<>();
    }

    /**
     * Record last execution of task and its relevant statistics values.
     *
     * @param time time of last execution
     * @param duration duration of last execution
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
     * Gets time of last execution.
     *
     * @return last execution timepoint
     */
    public LocalDateTime getLastExecution() {
        return lastExecutions.peekLast();
    }

    /**
     * Gets duration in nanoseconds of last execution.
     *
     * @return integral value of duration in nanoseconds
     */
    public Long getLastDuration() {
        return lastExecutionsDuration.peekLast();
    }
}
