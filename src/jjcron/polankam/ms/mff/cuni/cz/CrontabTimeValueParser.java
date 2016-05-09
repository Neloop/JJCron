package jjcron.polankam.ms.mff.cuni.cz;

/**
 *
 * @author Neloop
 */
public interface CrontabTimeValueParser {
    CrontabTimeValue parse(String line) throws FormatException;
}
