package jjcron.polankam.ms.mff.cuni.cz;

/**
 * Used if something went wrong in task creation.
 * @author Neloop
 */
public class TaskException extends Exception {

    /**
     * Standard constructor.
     */
    public TaskException() {}

    /**
     * Constructor which specify error message.
     * @param message description of error
     */
    public TaskException(String message) {
        super(message);
    }

    /**
     * Specify error message and exception which caused this particular one.
     * @param message description of error
     * @param cause exception which cause this one to be thrown
     */
    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Contruct exception which was thrown as reaction to given one.
     * @param cause exception which cause this one to be thrown
     */
    public TaskException(Throwable cause) {
        super(cause);
    }
}