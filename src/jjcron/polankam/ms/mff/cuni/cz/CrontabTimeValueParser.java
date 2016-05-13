package jjcron.polankam.ms.mff.cuni.cz;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Neloop
 */
public interface CrontabTimeValueParser {

    /**
     *
     * @return
     */
    public static CrontabTimeValueParser createGeneral() {
        return new CrontabTimeValueGeneralParser();
    }

    /**
     *
     * @return
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
     *
     * @return
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
     * 
     * @param value
     * @return
     * @throws FormatException
     */
    CrontabTimeValue parse(String value) throws FormatException;
}
