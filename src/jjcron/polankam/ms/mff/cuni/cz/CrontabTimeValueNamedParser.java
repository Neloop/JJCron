package jjcron.polankam.ms.mff.cuni.cz;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Neloop
 */
public class CrontabTimeValueNamedParser implements CrontabTimeValueParser {

    /**
     *
     */
    private final Map<String, Integer> namedValues;

    /**
     *
     * @param namedValues names which can be used as values in list
     */
    public CrontabTimeValueNamedParser(Map<String, Integer> namedValues) {
        this.namedValues = namedValues;
    }

    /**
     *
     * @param value
     * @return
     * @throws FormatException
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
