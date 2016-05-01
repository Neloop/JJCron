package jjcron;

import java.time.LocalDateTime;

/**
 *
 * @author Martin
 */
public class CrontabTimeGeneralUnit extends CrontabTimeUnitBase {

    private final int minValue;
    private final int maxValue;

    public CrontabTimeGeneralUnit(String unit, int minValue, int maxValue) throws FormatException {
        super(unit);

        this.minValue = minValue;
        this.maxValue = maxValue;

        initValue();
    }

    private void isValueValid(int value) throws FormatException {
        if (value < minValue || value > maxValue) {
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
                if (maxValue % (unit.values.get(0)) != 0) {
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
                int toMinute = maxValue - localNow.getSecond();
                int delayS = (toMinute + unit.values.get(0)) % maxValue;
                if (delayS > minValue) {
                    return delayS;
                } else {
                    return maxValue;
                }
            case PERIOD:
                int remainder = localNow.getSecond() % unit.values.get(0);
                return unit.values.get(0) - remainder;
            case LIST:
                int minimum = maxValue;
                for (Integer val : unit.values) {
                    int toMinuteL = maxValue - localNow.getSecond();
                    int minTemp = (toMinuteL + val) % maxValue;
                    if (minTemp > minValue && minTemp < minimum) {
                        minimum = minTemp;
                    }
                }
                return minimum;
            default:
                return minValue;
        }
    }

}
