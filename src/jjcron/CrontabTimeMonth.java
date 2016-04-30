package jjcron;

import java.time.LocalDateTime;

/**
 *
 * @author Martin
 */
public class CrontabTimeMonth extends CrontabTimeUnitBase {

    public CrontabTimeMonth(String month) throws FormatException {
        super(month);
        initValue();
    }

    private void isValueValid(int value) throws FormatException {
        if (value < 0 || value > 12) {
            throw new FormatException("Value is not valid months number");
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
                if (12 % (unit.values.get(0)) != 0) {
                    throw new FormatException("Months period value is not divisible");
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
    public int delay(LocalDateTime localNow) {
        // TODO
        return 1;
    }
}
