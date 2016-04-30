package jjcron;

import java.time.LocalDateTime;

/**
 *
 * @author Martin
 */
public class CrontabTimeSecond extends CrontabTimeUnitBase {

    public CrontabTimeSecond(String second) throws FormatException {
        super(second);
        initValue();
    }

    private void isValueValid(int value) throws FormatException {
        if (value < 0 || value > 60) {
            throw new FormatException("Value is not valid seconds number");
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
                    throw new FormatException("Seconds period value is not divisible");
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
        switch (unit.valueType) {
            case SINGLE:
                // TODO: more sofisticated
            case PERIOD:
                // TODO: more sofisticated
            case LIST:
                // TODO: more sofisticated
            default:
                return 0;
        }
    }
}
