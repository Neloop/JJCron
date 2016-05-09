package jjcron.polankam.ms.mff.cuni.cz;

/**
 *
 * @author Neloop
 */
public class ParserException extends Exception {
    
    public ParserException() {}

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }
}
