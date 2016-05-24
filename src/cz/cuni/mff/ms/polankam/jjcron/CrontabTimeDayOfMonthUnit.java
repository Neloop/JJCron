package cz.cuni.mff.ms.polankam.jjcron;

import java.time.LocalDateTime;

/**
 * Special time unit representing day of month which has different functionality
 *   than {@link CrontabTimeGeneralUnit} implementation.
 * If next timepoint should be on 31st day of a month but the month
 *   has fewer days than 31, than this timepoint will be scheduled to last day
 *   of specified month.
 * @author Neloop
 */
public class CrontabTimeDayOfMonthUnit extends CrontabTimeGeneralUnit {

    /**
     * Construct also parent class with given information
     *   and fill missing by myself.
     * @param unit textual representation of unit from crontab
     * @param parser parser which handles parsing of <code>unit</code> into
     *   appropriate structure.
     * @throws FormatException if parsing failed due to bad format of data
     */
    public CrontabTimeDayOfMonthUnit(String unit, CrontabTimeValueParser parser)
            throws FormatException {
        super(unit, 1, 31, 31, parser);
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

        int currentMonthMax = current.getMonth().length(
                current.toLocalDate().isLeapYear());

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
                        // if val is bigger than number of days in current month
                        //   then val from list should be downsized to max
                        int valTemp = val;
                        if (val > currentMonthMax) {
                            valTemp = currentMonthMax;
                        }

                        int toMinuteL = currentMonthMax - currentValue;
                        int minTemp = (toMinuteL + valTemp) % period;
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
