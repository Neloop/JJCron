package jjcron;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Martin
 */
public abstract class CrontabTimeUnitBase {

    protected enum TimeValueType {
        SINGLE,
        LIST,
        ASTERISK,
        PERIOD
    }

    protected class TimeValue {
        public TimeValueType valueType;
        public List<Integer> values;

        public TimeValue(TimeValueType valueType, Integer[] values) {
            this.valueType = valueType;
            this.values = new ArrayList<>(Arrays.asList(values));
        }

        public TimeValue(TimeValueType valueType, List<Integer> values) {
            this.valueType = valueType;
            this.values = values;
        }
    }

    protected String unitStr;
    protected TimeValue unit;

    public CrontabTimeUnitBase(String unit) {
        this.unitStr = unit;
    }

    protected final TimeValue initValueCommon(String unit) throws FormatException {
        try {
            // just a number
            int number = Integer.parseUnsignedInt(unit);
            return new TimeValue(TimeValueType.SINGLE, new Integer[] { number });
        } catch (NumberFormatException e) {}

        // asterisk
        if (unit.equals("*")) {
            return new TimeValue(TimeValueType.ASTERISK, new Integer[] {});
        }

        // period
        if (unit.startsWith("*/") || unit.startsWith("/")) {
            String numberStr = unit.substring("/".length());
            if (unit.startsWith("*/")) {
                numberStr = unit.substring("*/".length());
            }

            try {
                int number = Integer.parseUnsignedInt(numberStr);
                return new TimeValue(TimeValueType.PERIOD, new Integer[] { number });
            } catch (NumberFormatException e) {}
        }

        // list of values
        if (unit.indexOf(',') >= 0) {
            String[] splitted = unit.split(",");
            List<Integer> numbers = new ArrayList<>();
            for (String number : splitted) {
                try {
                    numbers.add(Integer.parseUnsignedInt(number));
                } catch (NumberFormatException e) {}
            }
            Collections.sort(numbers);
            return new TimeValue(TimeValueType.LIST, numbers);
        }

        throw new FormatException("Unknown time format");
    }

    public abstract int delay(LocalDateTime localNow);
}
