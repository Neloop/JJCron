package jjcron;

/**
 *
 * @author Martin
 */
public class TaskMetadata {
    private final CrontabTime time;
    private final String command;

    public TaskMetadata(CrontabTime time, String command) {
        this.time = time;
        this.command = command;
    }

    public CrontabTime time() {
        return time;
    }

    public String command() {
        return command;
    }
}
