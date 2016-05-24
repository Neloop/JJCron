package cz.cuni.mff.ms.polankam.jjcron;

/**
 * Internal exception which is thrown in case of bad format of given data.
 * Should not be logged before throwing this exception.
 * Should be catched and then logged.
 * @author Neloop
 */
public class FormatException extends Exception {

    /**
     * Standard constructor.
     */
    public FormatException() {}

    /**
     * Constructor which specify error message.
     * @param message description of error
     */
    public FormatException(String message) {
        super(message);
    }

    /**
     * Specify error message and exception which caused this particular one.
     * @param message description of error
     * @param cause exception which cause this one to be thrown
     */
    public FormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Contruct exception which was thrown as reaction to given one.
     * @param cause exception which cause this one to be thrown
     */
    public FormatException(Throwable cause) {
        super(cause);
    }
}