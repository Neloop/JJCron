package jjcron;

import java.time.LocalDateTime;

/**
 *
 * @author Martin
 */
public class CrontabTimeMinute extends CrontabTimeUnitBase {

    public CrontabTimeMinute(String minute) throws FormatException {
        super(minute);
        initValue();
    }

    private void isValueValid(int value) throws FormatException {
        if (value < 0 || value > 60) {
            throw new FormatException("Value is not valid minutes number");
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
                if (60 % (unit.values.get(0)) != 0) {
                    throw new FormatException("Minutes period value is not divisible");
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
        return 0;
    }
}
