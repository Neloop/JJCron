package jjcron;

/**
 *
 * @author Martin
 */
public class CrontabTimeGeneralUnit extends CrontabTimeUnitBase {

    private final int minValue;
    private final int maxValue;

    public static CrontabTimeGeneralUnit createCrontabTimeSecond(String unit) throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 0, 60);
    }

    public static CrontabTimeGeneralUnit createCrontabTimeMinute(String unit) throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 0, 60);
    }

    public static CrontabTimeGeneralUnit createCrontabTimeHour(String unit) throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 0, 24);
    }

    public static CrontabTimeGeneralUnit createCrontabTimeMonth(String unit) throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 1, 12);
    }

    public CrontabTimeGeneralUnit(String unit, int minValue, int maxValue) throws FormatException {
        super(unit);

        this.minValue = minValue;
        this.maxValue = maxValue;

        initValue();
    }

    private void isValueValid(int value) throws FormatException {
        if (value < minValue || value > maxValue) {
            throw new FormatException("GeneralUnit value is not valid number");
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
                    throw new FormatException("GeneralUnit period value is not divisible");
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
        switch (unit.valueType) {
            case SINGLE:
                int toMinute = maxValue - localNow;
                int delayS = (toMinute + unit.values.get(0)) % maxValue;
                if (delayS > minValue) {
                    return delayS;
                } else {
                    return maxValue;
                }
            case PERIOD:
                int remainder = localNow % unit.values.get(0);
                return unit.values.get(0) - remainder;
            case LIST:
                int minimum = maxValue;
                for (Integer val : unit.values) {
                    int toMinuteL = maxValue - localNow;
                    int minTemp = (toMinuteL + val) % maxValue;
                    if (minTemp > minValue && minTemp < minimum) {
                        minimum = minTemp;
                    }
                }
                return minimum;
            default:
                return 0;
        }
    }

}
