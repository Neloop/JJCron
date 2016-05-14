package jjcron.polankam.ms.mff.cuni.cz;

/**
 *
 * @author Neloop
 */
public class ParserException extends Exception {

    /**
     *
     */
    public ParserException() {}

    /**
     *
     * @param message
     */
    public ParserException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param cause
     */
    public ParserException(Throwable cause) {
        super(cause);
    }
}
