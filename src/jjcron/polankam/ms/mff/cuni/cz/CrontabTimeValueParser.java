package jjcron.polankam.ms.mff.cuni.cz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Neloop
 */
public interface CrontabTimeValueParser {

    public static CrontabTimeValueParser createGeneral() {
        return new CrontabTimeValueGeneralParser();
    }

    public static CrontabTimeValueParser createDayOfWeek() {
        List<String> dayOfWeeks = new ArrayList<>(Arrays.asList(
                "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"
        ));
        return new CrontabTimeValueNamedParser(dayOfWeeks);
    }

    public static CrontabTimeValueParser createMonth() {
        List<String> months = new ArrayList<>(Arrays.asList(
                "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
        ));
        return new CrontabTimeValueNamedParser(months);
    }

    CrontabTimeValue parse(String line) throws FormatException;
}
