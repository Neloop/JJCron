package jjcron.polankam.ms.mff.cuni.cz;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Every task which will be scheduled within JJCron
 *   has to implement this interface.
 * @author Neloop
 */
public interface Task {

    /**
     * Returns this task name.
     * @return textual representation of command name (usually command name)
     */
    String name();

    /**
     * Compute delay from <code>localNow</code> to next execution point of task.
     * @param localNow current time point which will be used as base
     * @return number of units specified from {@link timeUnit()} function call
     */
    long delay(LocalDateTime localNow);

    /**
     * Time unit in which delay is returned.
     * @return time unit type
     */
    TimeUnit timeUnit();

    /**
     * Execute task job, should be implemented in {@link Task} implementations.
     * @throws Exception if task execution failed
     */
    void run() throws Exception;
}
