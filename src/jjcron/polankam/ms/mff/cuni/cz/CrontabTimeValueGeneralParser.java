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
        try {
            // just a number
            int number = Integer.parseUnsignedInt(line);
            return new CrontabTimeValue(CrontabTimeValueType.LIST, new Integer[] { number });
        } catch (NumberFormatException e) {}

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
            } catch (NumberFormatException e) {}
        }

        // list of values
        if (line.indexOf(',') >= 0) {
            String[] splitted = line.split(",");
            List<Integer> numbers = new ArrayList<>();
            for (String number : splitted) {
                try {
                    numbers.add(Integer.parseUnsignedInt(number));
                } catch (NumberFormatException e) {}
            }
            Collections.sort(numbers);
            return new CrontabTimeValue(CrontabTimeValueType.LIST, numbers);
        }

        throw new FormatException("Unknown time format");
    }

}
