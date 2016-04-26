package jjcron;

/**
 *
 * @author Martin
 */
public class TaskMetadata {
    private final CrontabTime time;
    private final String command;

    public TaskMetadata(CrontabTime time, String command) throws FormatException {
        if (time == null) {
            throw new FormatException("Time is not defined (null)");
        }
        if (command == null) {
            throw new FormatException("Command is not defined (null)");
        }

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
