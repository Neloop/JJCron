package cz.cuni.mff.ms.polankam.jjcron;

/**
 * Thrown in {@link CrontabParser} if something went wrong.
 * Further description should be provided.
 * @author Neloop
 */
public class ParserException extends Exception {

    /**
     * Standard constructor.
     */
    public ParserException() {}

    /**
     * Constructor which specify error message.
     * @param message description of error
     */
    public ParserException(String message) {
        super(message);
    }

    /**
     * Specify error message and exception which caused this particular one.
     * @param message description of error
     * @param cause exception which cause this one to be thrown
     */
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Contruct exception which was thrown as reaction to given one.
     * @param cause exception which cause this one to be thrown
     */
    public ParserException(Throwable cause) {
        super(cause);
    }
}
