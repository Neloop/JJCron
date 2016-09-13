package cz.polankam.jjcron.common;

import java.time.LocalDateTime;

/**
 * Defines interface which should every crontab time unit complement. Crontab
 * time unit is for instance second, minute, basically everything which can be
 * in time column in crontab. Interface defines only methods needed for
 * scheduling, every time unit can handle this differently. Also this interface
 * defines factory methods for all generally needed types.
 *
 * @author Neloop
 */
public interface CrontabTimeUnit {

    // *** FACTORY METHODS ***
    /**
     * Creates second unit and its delay computation.
     *
     * @param unit textual description of unit as seen in crontab
     * @return implementation of {@link CrontabTimeUnit} representing second
     * @throws FormatException if unit is in wrong format
     */
    public static CrontabTimeUnit createSecond(String unit)
            throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 0, 59, 60,
                CrontabTimeValueParser.createGeneral());
    }

    /**
     * Creates minute unit and its delay computation.
     *
     * @param unit textual description of unit as seen in crontab
     * @return implementation of {@link CrontabTimeUnit} representing minute
     * @throws FormatException if unit is in wrong format
     */
    public static CrontabTimeUnit createMinute(String unit)
            throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 0, 59, 60,
                CrontabTimeValueParser.createGeneral());
    }

    /**
     * Creates hour unit and its delay computation.
     *
     * @param unit textual description of unit as seen in crontab
     * @return implementation of {@link CrontabTimeUnit} representing hour
     * @throws FormatException if unit is in wrong format
     */
    public static CrontabTimeUnit createHour(String unit)
            throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 0, 23, 24,
                CrontabTimeValueParser.createGeneral());
    }

    /**
     * Creates day of month unit and its delay computation.
     *
     * @param unit textual description of unit as seen in crontab
     * @return implementation representing day of month
     * @throws FormatException if unit is in wrong format
     */
    public static CrontabTimeUnit createDayOfMonth(String unit)
            throws FormatException {
        return new CrontabTimeDayOfMonthUnit(unit,
                CrontabTimeValueParser.createGeneral());
    }

    /**
     * Creates month unit and its delay computation.
     *
     * @param unit textual description of unit as seen in crontab
     * @return implementation of {@link CrontabTimeUnit} representing month
     * @throws FormatException if unit is in wrong format
     */
    public static CrontabTimeUnit createMonth(String unit)
            throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 1, 12, 12,
                CrontabTimeValueParser.createMonth());
    }

    /**
     * Creates day of week unit and its delay computation.
     *
     * @param unit textual description of unit as seen in crontab
     * @return implementation representing day of week
     * @throws FormatException if unit is in wrong format
     */
    public static CrontabTimeUnit createDayOfWeek(String unit)
            throws FormatException {
        return new CrontabTimeGeneralUnit(unit, 1, 7, 7,
                CrontabTimeValueParser.createDayOfWeek());
    }

    // *** INTERFACE DEFINITION ***
    /**
     * Tells whether previous delay computation returned non-zero value. That
     * means if last delay computation made difference in scheduled timepoint.
     *
     * @return true if unit changed last time false otherwise
     */
    public boolean isChanged();

    /**
     * Computes delay which should be added to <code>current</code> in order to
     * properly schedule next task execution.
     *
     * @param current should be very near to LocalDateTime.now()
     * @param currentValue current value of specified unit which should
     * correspond in rank with actual time unit implementation
     * <p>
     * <b>Example:</b> if implementation of unit is second and actual time is
     * 01:02:03 (hh:mm:ss) than <code>currentValue</code> will be equal to
     * 3.</p>
     * @param previousChanged determines if previous unit changed
     * <p>
     * <b>Example:</b> If we have implementation of minutes then previous unit
     * is seconds and previousChanged will have value of
     * <code>seconds.isChanged()</code>.</p>
     * @return delay which should be added to current timestamp in order to
     * execute task in right time
     */
    public int delay(LocalDateTime current, int currentValue,
            boolean previousChanged);

    public CrontabTimeValue value();
}
