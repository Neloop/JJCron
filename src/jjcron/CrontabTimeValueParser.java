package jjcron;

/**
 *
 * @author Martin
 */
public interface CrontabTimeValueParser {
    CrontabTimeValue parse(String line) throws FormatException;
}
