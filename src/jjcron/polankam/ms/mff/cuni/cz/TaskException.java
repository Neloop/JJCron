package jjcron.polankam.ms.mff.cuni.cz;

/**
 *
 * @author Neloop
 */
public class TaskException extends Exception {

    /**
     *
     */
    public TaskException() {}

    /**
     *
     * @param message
     */
    public TaskException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param cause
     */
    public TaskException(Throwable cause) {
        super(cause);
    }
}