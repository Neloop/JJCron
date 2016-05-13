package jjcron.polankam.ms.mff.cuni.cz;

/**
 *
 * @author Neloop
 */
public class TaskMetadata {
    /**
     *
     */
    private final CrontabTime time;
    /**
     *
     */
    private final String command;

    // no logger!!!

    public TaskMetadata(CrontabTime time, String command)
            throws FormatException {
        if (time == null) {
            throw new FormatException("Time is not defined (null)");
        }
        if (command == null) {
            throw new FormatException("Command is not defined (null)");
        }

        this.time = time;
        this.command = command;
    }

    public final CrontabTime time() {
        return time;
    }

    public final String command() {
        return command;
    }
}
