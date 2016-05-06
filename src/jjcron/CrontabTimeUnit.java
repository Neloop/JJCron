package jjcron;

/**
 *
 * @author Martin
 */
public class CrontabTimeUnit {

    private final int minValue;
    private final int maxValue;
    private final String unitStr;
    private final CrontabTimeValue unit;
    private final CrontabTimeValueParser parser;

    private long lastStamp;
    private boolean valueChanged;

    public static CrontabTimeUnit createCrontabTimeSecond(String unit) throws FormatException {
        return new CrontabTimeUnit(unit, 0, 60, new CrontabTimeValueGeneralParser());
    }

    public static CrontabTimeUnit createCrontabTimeMinute(String unit) throws FormatException {
        return new CrontabTimeUnit(unit, 0, 60, new CrontabTimeValueGeneralParser());
    }

    public static CrontabTimeUnit createCrontabTimeHour(String unit) throws FormatException {
        return new CrontabTimeUnit(unit, 0, 24, new CrontabTimeValueGeneralParser());
    }

    public static CrontabTimeUnit createCrontabTimeMonth(String unit) throws FormatException {
        return new CrontabTimeUnit(unit, 1, 12, new CrontabTimeValueGeneralParser());
    }

    public CrontabTimeUnit(String unit, int minValue, int maxValue,
            CrontabTimeValueParser parser) throws FormatException {
        this.unitStr = unit;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.parser = parser;

        this.unit = this.parser.parse(unitStr);

        valueChanged = false;

        checkValue();
    }

    private void isValueValid(int value) throws FormatException {
        if (value < minValue || value > maxValue) {
            throw new FormatException("CrontabTimeUnit value is not valid number");
        }
    }

    public final void checkValue() throws FormatException {
        switch (unit.valueType) {
            case SINGLE:
                isValueValid(unit.values.get(0));
                break;
            case ASTERISK:
                // nothing to do here
                break;
            case PERIOD:
                isValueValid(unit.values.get(0));
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

    public boolean lastChanged()
    {
        return valueChanged;
    }

    public int delay(int currentValue, long currentStamp, boolean previousChanged)
    {
        int result;

        valueChanged = false;

        switch (unit.valueType) {
            case SINGLE:
                int toMinute = maxValue - currentValue;
                int delayS = (toMinute + unit.values.get(0)) % maxValue;
                if (delayS == minValue && currentStamp == lastStamp) {
                    result = maxValue;
                } else {
                    result = delayS;
                }
                valueChanged = true;
                break;
            case PERIOD:
                int remainder = currentValue % unit.values.get(0);
                result = unit.values.get(0) - remainder;
                break;
            case LIST:
                int minimum = maxValue;
                for (Integer val : unit.values) {
                    int toMinuteL = maxValue - currentValue;
                    int minTemp = (toMinuteL + val) % maxValue;
                    if (minTemp > minValue && minTemp < minimum) {
                        minimum = minTemp;
                    }
                }
                result = minimum;
                break;
            default:
                result = 0;
                break;
        }

        lastStamp = currentStamp;
        return result;
    }
}
