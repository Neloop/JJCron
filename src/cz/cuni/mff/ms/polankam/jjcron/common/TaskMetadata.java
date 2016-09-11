package cz.cuni.mff.ms.polankam.jjcron.common;

import java.io.Serializable;

/**
 * Structure which represents all information about one task given in crontab.
 * It stores {@link CrontabTime} structure with information about time
 * scheduling and then textual description of command which will be executed on
 * scheduled timepoint.
 *
 * @author Neloop
 */
public class TaskMetadata implements Serializable {

    /**
     * Stores information about time which was parsed from crontab.
     */
    private final CrontabTime time;
    /**
     * Stores textual description of command which will be executed. Can be
     * further processed, for example it can contain xml tags which can alter
     * default way of execution.
     */
    private final String command;

    /**
     * Should be constructed with proper information.
     *
     * @param time {@link CrontabTime} structure representing whole time
     * @param command command which will be executed
     * @throws FormatException if time or command is null
     */
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

    /**
     * Gets internal structure representing whole crontab time.
     *
     * @return {@link CrontabTime} structure which stores all needed units.
     */
    public final CrontabTime time() {
        return time;
    }

    /**
     * Gets textual description of command executed in scheduled timpoint.
     *
     * @return text which will be interpreted as command to execute
     */
    public final String command() {
        return command;
    }
}
