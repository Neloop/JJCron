package cz.polankam.jjcron.remote.manager;

/**
 * General exception used in JJCronRM if something went wrong. Further
 * description should be provided.
 *
 * @author Neloop
 */
public class ManagerException extends Exception {

    /**
     * Constructor which specify error message.
     *
     * @param message description of error
     */
    public ManagerException(String message) {
        super(message);
    }

    /**
     * Specify error message and exception which caused this particular one.
     *
     * @param message description of error
     * @param cause exception which cause this one to be thrown
     */
    public ManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Contruct exception which was thrown as reaction to given one.
     *
     * @param cause exception which cause this one to be thrown
     */
    public ManagerException(Throwable cause) {
        super(cause);
    }
}
