package jjcron.polankam.ms.mff.cuni.cz;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Special implementation of parser which parse one column from crontab.
 * This parser can parse named values which can be placed in list
 *   or as single value.
 * @author Neloop
 */
public class CrontabTimeValueNamedParser implements CrontabTimeValueParser {

    /**
     * Named values which can be present as single value or as list.
     */
    private final Map<String, Integer> namedValues;

    /**
     * To this parser named values has to be provided for proper functionality.
     * @param namedValues names which can be used as values in list
     */
    public CrontabTimeValueNamedParser(Map<String, Integer> namedValues) {
        this.namedValues = namedValues;
    }

    /**
     * Special parsing method which can distinguish single value,
     *   list of values, named values, period of time and asterisk.
     * @param value parsed value
     * @return structure which can hold information about time value
     * @throws FormatException if parsing of time value failed
     */
    @Override
    public CrontabTimeValue parse(String value) throws FormatException {
        // asterisk
        if (value.equals("*")) {
            return new CrontabTimeValue(CrontabTimeValueType.ASTERISK,
                    new Integer[] {});
        }

        // period
        if (value.startsWith("*/") || value.startsWith("/")) {
            String numberStr = value.substring("/".length());
            if (value.startsWith("*/")) {
                numberStr = value.substring("*/".length());
            }

            try {
                int number = Integer.parseUnsignedInt(numberStr);
                return new CrontabTimeValue(CrontabTimeValueType.PERIOD,
                        new Integer[] { number });
            } catch (NumberFormatException e) {
                throw new FormatException("Unknown period time format");
            }
        }

        // list of values, single value is also list of values
        String[] splitted = value.split(",");
        Set<Integer> numbers = new TreeSet<>(); // sorted unique elements
        for (String number : splitted) {
            try {
                Integer numberVal = namedValues.get(number.toUpperCase());
                if (numberVal == null) {
                    numberVal = Integer.parseUnsignedInt(number);
                }
                numbers.add(numberVal);
            } catch (NumberFormatException e) {
                throw new FormatException("Unknown list time format");
            }
        }
        return new CrontabTimeValue(CrontabTimeValueType.LIST,
                new ArrayList<>(numbers));
    }

}
