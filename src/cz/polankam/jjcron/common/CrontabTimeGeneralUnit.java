package cz.polankam.jjcron.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * General implementation of {@link CrontabTimeUnit} interface which should be
 * sufficient for most of time unit in crontab aka. second, minutes, hours, etc.
 * It includes minimal and maximal value which can unit have and also period
 * with which specified unit operates.
 *
 * @author Neloop
 */
public class CrontabTimeGeneralUnit implements CrontabTimeUnit, Serializable {

    /**
     * Minimal value which can be in single value, list of values or in period.
     */
    protected final int minValue;
    /**
     * Maximal value which can be in single value, list of values or in period.
     */
    protected final int maxValue;
    /**
     * Period with which this unit operates.
     */
    protected final int period;

    /**
     * Textual description of unit extracted from column in crontab.
     */
    protected final String unitStr;
    /**
     * Structure parsed and constructed from <code>unitStr</code>. Which stores
     * all needed info about time unit.
     */
    protected final CrontabTimeValue unit;

    /**
     * Cached information about value change in last delay computation.
     * Accessible through <code>isChanged()</code> method.
     */
    protected boolean valueChanged;

    /**
     * Initialization of this structure, unit parsing and checking is performed.
     *
     * @param unit textual description of unit
     * @param minValue minimal value of unit
     * @param maxValue maximal value of unit
     * @param period period of unit
     * @param parser implementation of parser which is able to extract needed
     * information from given <code>unit</code>
     * @throws FormatException if unit cannot be parsed
     */
    public CrontabTimeGeneralUnit(String unit, int minValue, int maxValue,
            int period, CrontabTimeValueParser parser)
            throws FormatException {
        this.unitStr = unit;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.period = period;
        valueChanged = false;

        this.unit = parser.parse(unitStr);

        checkValue();
    }

    /**
     * Check if given value is valid according to minimal and maximal values.
     *
     * @param value checked value
     * @throws FormatException if value is not valid
     */
    private void isValueValid(int value) throws FormatException {
        if (value < minValue || value > maxValue) {
            throw new FormatException(
                    "CrontabTimeUnit value is not valid number");
        }
    }

    /**
     * Check parsed {@link CrontabTimeValue} structure for proper valid values.
     *
     * @throws FormatException if value did not pass check
     */
    private void checkValue() throws FormatException {
        switch (unit.valueType) {
            case ASTERISK:
                // nothing to do here
                break;
            case PERIOD:
                isValueValid(unit.values.get(0));
                if (unit.values.get(0) == 0
                        || maxValue % (unit.values.get(0)) != 0) {
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

    @Override
    public boolean isChanged() {
        return valueChanged;
    }

    @Override
    public int delay(LocalDateTime current, int currentValue,
            boolean previousChanged) {
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

    @Override
    public CrontabTimeValue value() {
        return unit;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof CrontabTimeGeneralUnit)) {
            return false;
        }

        CrontabTimeGeneralUnit cast = (CrontabTimeGeneralUnit) other;
        return cast.minValue == minValue && cast.maxValue == maxValue
                && cast.period == period && cast.unit.equals(unit);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.minValue;
        hash = 89 * hash + this.maxValue;
        hash = 89 * hash + this.period;
        hash = 89 * hash + Objects.hashCode(this.unit);
        return hash;
    }
}
