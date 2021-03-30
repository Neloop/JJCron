package cz.polankam.jjcron.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Interface for parsing values representing time in crontab.
 *
 * @author Neloop
 */
public interface CrontabTimeValueParser {

    /**
     * Factory method which will create and return general time value parser.
     *
     * @return structure which implements this interface
     */
    public static CrontabTimeValueParser createGeneral() {
        Map<String, Integer> empty = new HashMap<>();
        return new CrontabTimeValueNamedParser(empty);
    }

    /**
     * Factory method which will create special day of week time value parser.
     *
     * @return structure which implements this interface
     */
    public static CrontabTimeValueParser createDayOfWeek() {
        Map<String, Integer> dayOfWeeks = new HashMap<>();
        dayOfWeeks.put("MON", 1);
        dayOfWeeks.put("TUE", 2);
        dayOfWeeks.put("WED", 3);
        dayOfWeeks.put("THU", 4);
        dayOfWeeks.put("FRI", 5);
        dayOfWeeks.put("SAT", 6);
        dayOfWeeks.put("SUN", 7);

        return new CrontabTimeValueNamedParser(dayOfWeeks);
    }

    /**
     * Factory method which will create special month time value parser.
     *
     * @return structure which implements this interface
     */
    public static CrontabTimeValueParser createMonth() {
        Map<String, Integer> months = new HashMap<>();
        months.put("JAN", 1);
        months.put("FEB", 2);
        months.put("MAR", 3);
        months.put("APR", 4);
        months.put("MAY", 5);
        months.put("JUN", 6);
        months.put("JUL", 7);
        months.put("AUG", 8);
        months.put("SEP", 9);
        months.put("OCT", 10);
        months.put("NOV", 11);
        months.put("DEC", 12);

        return new CrontabTimeValueNamedParser(months);
    }

    /**
     * Parse given value into {@link CrontabTimeValue} structure.
     *
     * @param value textual representation of time value which will be parsed
     * @return parsed info about crontab time value
     * @throws FormatException if given string is in bad format
     */
    CrontabTimeValue parse(String value) throws FormatException;
}
