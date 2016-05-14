package jjcron.polankam.ms.mff.cuni.cz;

/**
 *
 * @author Neloop
 */
public class FormatException extends Exception {

    /**
     *
     */
    public FormatException() {}

    /**
     *
     * @param message
     */
    public FormatException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public FormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param cause
     */
    public FormatException(Throwable cause) {
        super(cause);
    }
}