package jjcron;

import java.time.LocalDateTime;

/**
 *
 * @author Martin
 */
public class CrontabTimeDayOfWeek extends CrontabTimeUnitBase {

    public CrontabTimeDayOfWeek(String dayOfWeek) throws FormatException {
        super(dayOfWeek);
        initValue();
    }

    private void isValueValid(int value) throws FormatException {
        if (value < 0 || value > 7) {
            throw new FormatException("Value is not valid DayOfWeeks number");
        }
    }

    public final void initValue() throws FormatException {
        unit = initValueCommon(unitStr);

        switch (unit.valueType) {
            case SINGLE:
                isValueValid(unit.values.get(0));
                break;
            case ASTERISK:
                // nothing to do here
                break;
            case PERIOD:
                if (7 % (unit.values.get(0)) != 0) {
                    throw new FormatException("DayOfWeeks period value is not divisible");
                }
                break;
            case LIST:
                for (Integer number : unit.values) {
                    isValueValid(number);
                }
                break;
        }
    }

    @Override
    public int delay(int localNow) {
        // TODO
        return 0;
    }
}
