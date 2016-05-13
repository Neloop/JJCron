package jjcron.polankam.ms.mff.cuni.cz;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Neloop
 */
public class CrontabTimeValueGeneralParser implements CrontabTimeValueParser {

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
        Set<Integer> numbers = new TreeSet<>();
        for (String number : splitted) {
            try {
                numbers.add(Integer.parseUnsignedInt(number));
            } catch (NumberFormatException e) {
                throw new FormatException("Unknown list time format");
            }
        }
        return new CrontabTimeValue(CrontabTimeValueType.LIST,
                new ArrayList<>(numbers));
    }
}
