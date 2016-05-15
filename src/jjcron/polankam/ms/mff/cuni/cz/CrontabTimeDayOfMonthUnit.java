package jjcron.polankam.ms.mff.cuni.cz;

import java.time.LocalDateTime;

// ***********************
// TODO: implement this!!!
// ***********************

/**
 *
 * @author Neloop
 */
public class CrontabTimeDayOfMonthUnit implements CrontabTimeUnit {

    /**
     *
     */
    private final int minValue;
    /**
     *
     */
    private final int maxValue;
    /**
     *
     */
    private final int period;

    /**
     *
     */
    private final String unitStr;
    /**
     *
     */
    private final CrontabTimeValue unit;
    /**
     *
     */
    private final CrontabTimeValueParser parser;

    /**
     *
     */
    private boolean valueChanged;

    /**
     *
     * @param unit
     * @param parser
     * @throws FormatException
     */
    public CrontabTimeDayOfMonthUnit(String unit, CrontabTimeValueParser parser)
            throws FormatException {
        this.unitStr = unit;
        this.minValue = 1;
        this.maxValue = 31;
        this.period = 31;
        this.parser = parser;
        valueChanged = false;

        this.unit = this.parser.parse(unitStr);

        checkValue();
    }

    /**
     *
     * @param value
     * @throws FormatException
     */
    private void isValueValid(int value) throws FormatException {
        if (value < minValue || value > maxValue) {
            throw new FormatException(
                    "CrontabTimeUnit value is not valid number");
        }
    }

    /**
     *
     * @throws FormatException
     */
    private void checkValue() throws FormatException {
        switch (unit.valueType) {
            case ASTERISK:
                // nothing to do here
                break;
            case PERIOD:
                isValueValid(unit.values.get(0));
                if (maxValue % (unit.values.get(0)) != 0) {
                    throw new FormatException(
                            "GeneralUnit period value is not divisible");
                }
                break;
            case LIST:
                for (Integer number : unit.values) {
                    isValueValid(number);
                }
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChanged()
    {
        return valueChanged;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delay(LocalDateTime current, int currentValue,
            boolean previousChanged)
    {
        int result;

        valueChanged = previousChanged;

        switch (unit.valueType) {
            case PERIOD:
                int remainder = currentValue % unit.values.get(0);
                if (previousChanged && remainder == 0) {
                    result = 0;
                } else {
                    result = unit.values.get(0) - remainder;
                }
                valueChanged = true;
                break;
            case LIST:
                result = period; // minimum from list of values
                for (Integer val : unit.values) {
                    if (previousChanged && currentValue == val) {
                        result = 0;
                    } else {
                        int toMinuteL = period - currentValue;
                        int minTemp = (toMinuteL + val) % period;
                        if (minTemp > 0 && minTemp < result) {
                            result = minTemp;
                        }
                    }
                }
                valueChanged = true;
                break;
            default: // asterisk
                if (previousChanged) {
                    result = 0;
                } else {
                    result = 1;
                }
                valueChanged = true;
                break;
        }

        return result;
    }
}
