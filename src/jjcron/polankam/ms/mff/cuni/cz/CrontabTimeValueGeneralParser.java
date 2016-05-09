package jjcron.polankam.ms.mff.cuni.cz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Neloop
 */
public class CrontabTimeValueGeneralParser implements CrontabTimeValueParser {

    @Override
    public CrontabTimeValue parse(String line) throws FormatException {
        // asterisk
        if (line.equals("*")) {
            return new CrontabTimeValue(CrontabTimeValueType.ASTERISK, new Integer[] {});
        }

        // period
        if (line.startsWith("*/") || line.startsWith("/")) {
            String numberStr = line.substring("/".length());
            if (line.startsWith("*/")) {
                numberStr = line.substring("*/".length());
            }

            try {
                int number = Integer.parseUnsignedInt(numberStr);
                return new CrontabTimeValue(CrontabTimeValueType.PERIOD, new Integer[] { number });
            } catch (NumberFormatException e) {
                throw new FormatException("Unknown period time format");
            }
        }

        // list of values, single value is also list of values
        String[] splitted = line.split(",");
        List<Integer> numbers = new ArrayList<>();
        for (String number : splitted) {
            try {
                numbers.add(Integer.parseUnsignedInt(number));
            } catch (NumberFormatException e) {
                throw new FormatException("Unknown list time format");
            }
        }
        Collections.sort(numbers); // do not actually needed
        return new CrontabTimeValue(CrontabTimeValueType.LIST, numbers);
    }
}
